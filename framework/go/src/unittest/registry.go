package unittest

import (
    "encoding/json"
    "fmt"
    lc "missingjs.com/soda/leetcode"
    "reflect"
)

type ObjectConverterFactory func() *ObjectConverter

var converterFactoryMap = make(map[reflect.Type]ObjectConverterFactory)

func init() {
    regConverter(lc.ListCreate, lc.ListDump)
    regConverter(lc.TreeCreate, lc.TreeDump)
    regConverter(DecodeByteSlice, EncodeByteSlice)
    regConverter(DecodeByteSlice2D, EncodeByteSlice2D)
    regConverter(lc.UnmarshalNestedInteger, lc.MarshalNestedInteger)
    regConverter(lc.ParseNestedIntegers, lc.SerializeNestedIntegers)
    regConverter(func(s string) byte { return s[0] }, func(c byte) string { return string([]byte{c}) })
    regConverter(lc.ListFactory.CreateSlice, lc.ListFactory.DumpSlice)
}

func checkFunction(v reflect.Value) {
    t := v.Type()
    if t.Kind() != reflect.Func {
        panic("not a function")
    }
    if t.NumIn() != 1 {
        panic("function must has only one input")
    }
    if t.NumOut() != 1 {
        panic("function must has only one output")
    }
}

func regConverter(funcFromJson interface{}, funcToJson interface{}) {
    vFromFunc := reflect.ValueOf(funcFromJson)
    checkFunction(vFromFunc)
    targetType := vFromFunc.Type().Out(0)

    vToFunc := reflect.ValueOf(funcToJson)
    checkFunction(vToFunc)
    if vToFunc.Type().In(0) != targetType {
        panic(fmt.Sprintf("need input type: %v", targetType))
    }

    converterFactoryMap[targetType] = func() *ObjectConverter {
        return newObjectConverter(vFromFunc, vToFunc)
    }
}

func getConverter(dataType reflect.Type) *ObjectConverter {
    if fact, ok := converterFactoryMap[dataType]; ok {
        return fact()
    }
    fromFunc := func(raw json.RawMessage) interface{} {
        v := reflect.New(dataType)
        if err := json.Unmarshal(raw, v.Interface()); err != nil {
            panic(fmt.Sprintf("Failed to parse json: %s\n", err))
        }
        return v.Elem().Interface()
    }
    toFunc := func(obj interface{}) interface{} {
        return obj
    }
    return newObjectConverter(reflect.ValueOf(fromFunc), reflect.ValueOf(toFunc))
}
