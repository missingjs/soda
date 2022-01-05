package unittest

import (
	"bufio"
	"bytes"
	"encoding/json"
	"fmt"
	"os"
	"reflect"
	"time"
)

type TestInput struct {
	Id       int               `json:"id"`
	Args     []json.RawMessage `json:"args"`
	Expected json.RawMessage   `json:"expected"`
}

func (t *TestInput) HasExpected() bool {
	return len(t.Expected) > 0 && string(t.Expected) != "null"
}

type TestOutput struct {
	Id      int         `json:"id"`
	Success bool        `json:"success"`
	Result  interface{} `json:"result"`
	Elapse  float64     `json:"elapse"`
}

type TestWork struct {
	Function      reflect.Value
	ArgumentTypes []reflect.Type
	ReturnType    reflect.Type
	CompareSerial bool
	validator     reflect.Value
}

func CreateWork(fn interface{}) *TestWork {
	work := new(TestWork)
	work.initialize(fn)
	return work
}

func CreateWorkForStruct(structFact interface{}) *TestWork {
	st := newStructTester(structFact)
	return CreateWork(st.test)
}

func (work *TestWork) SetValidator(fn interface{}) {
	work.validator = reflect.ValueOf(fn)
}

func (work *TestWork) initialize(fn interface{}) *TestWork {
	work.Function = reflect.ValueOf(fn)
	funcType := work.Function.Type()

	numArgs := funcType.NumIn()
	work.ArgumentTypes = make([]reflect.Type, numArgs)
	for i := 0; i < numArgs; i++ {
		work.ArgumentTypes[i] = funcType.In(i)
	}

	if funcType.NumOut() > 0 {
		work.ReturnType = funcType.Out(0)
	}
	valid := func(x, y interface{}) bool {
		return reflect.DeepEqual(x, y)
	}
	work.validator = reflect.ValueOf(valid)
	return work
}

func vals(values ...reflect.Value) []reflect.Value {
	return values
}

func compareByJsonSerial(a interface{}, b interface{}) bool {
	dataA, _ := json.Marshal(a)
	dataB, _ := json.Marshal(b)
	return reflect.DeepEqual(dataA, dataB)
}

func unmarshal(raw json.RawMessage, objType reflect.Type) interface{} {
	return getConverter(objType).FromRawBytes(raw)
}

func marshal(obj interface{}, objType reflect.Type) interface{} {
	return getConverter(objType).ToJsonSerializable(obj)
}

func (work *TestWork) Run() {
	var testInput TestInput
	input := bytesFromStdin()
	if err := json.Unmarshal(input, &testInput); err != nil {
		panic(fmt.Sprintf("Failed to unmarshal input: %s\n", err))
	}

	args := make([]reflect.Value, len(work.ArgumentTypes))
	for i := 0; i < len(testInput.Args); i++ {
		args[i] = reflect.ValueOf(unmarshal(testInput.Args[i], work.ArgumentTypes[i]))
	}

	startTime := time.Now()
	var resultValue reflect.Value
	retType := work.ReturnType
	if retType != nil {
		resultValue = work.Function.Call(args)[0]
	} else {
		work.Function.Call(args)
		retType = work.ArgumentTypes[0]
		resultValue = args[0]
	}
	duration := time.Since(startTime)
	elapseMillis := float64(duration.Microseconds()) / 1000.0
	serialObject := marshal(resultValue.Interface(), retType)

	out := TestOutput{}
	out.Id = testInput.Id
	out.Result = serialObject
	out.Elapse = elapseMillis

	success := true
	if testInput.HasExpected() {
		if !work.CompareSerial {
			expectValue := reflect.ValueOf(unmarshal(testInput.Expected, retType))
			success = work.validator.Call(vals(expectValue, resultValue))[0].Bool()
		} else {
			success = compareByJsonSerial(testInput.Expected, serialObject)
		}
	}

	out.Success = success

	jstring, err := json.Marshal(out)
	if err != nil {
		panic(fmt.Sprintf("JSON marshaling failed: %s\n", err))
	}
	fmt.Print(string(jstring))
}

func bytesFromStdin() []byte {
	var buffer bytes.Buffer
	reader := bufio.NewReader(os.Stdin)
	p := make([]byte, 1024*50)
	for {
		n, err := reader.Read(p)
		buffer.Write(p[:n])
		if n == 0 || err != nil {
			break
		}
	}
	return buffer.Bytes()
}
