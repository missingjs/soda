package unittest

import (
	"encoding/json"
	"reflect"
	"strings"
)

type structTester struct {
	factoryFunc reflect.Value
}

func newStructTester(fn interface{}) *structTester {
	return &structTester{
		factoryFunc: reflect.ValueOf(fn),
	}
}

func (s *structTester) test(operations []string, parameters [][]json.RawMessage) []interface{} {
	objValue := invokeFunction(s.factoryFunc, parameters[0])
	ptr := reflect.New(objValue.Type())
	ptr.Elem().Set(objValue)
	res := make([]interface{}, len(parameters))
	for i := 1; i < len(parameters); i++ {
		methodName := strings.Title(operations[i])
		m := ptr.MethodByName(methodName)
		if !m.IsValid() {
			m = ptr.Elem().MethodByName(methodName)
		}
		r := invokeFunction(m, parameters[i])
		if r.IsValid() {
			resType := m.Type().Out(0)
			res[i] = marshal(r.Interface(), resType)
		}
	}
	return res
}

func invokeFunction(fn reflect.Value, args []json.RawMessage) reflect.Value {
	funcType := fn.Type()
	fnArgs := make([]reflect.Value, funcType.NumIn())
	for i := 0; i < funcType.NumIn(); i++ {
		argType := funcType.In(i)
		fnArgs[i] = reflect.ValueOf(unmarshal(args[i], argType))
	}
	r := fn.Call(fnArgs)
	if len(r) > 0 {
		return r[0]
	}
	return reflect.Value{}
}
