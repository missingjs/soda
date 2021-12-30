package unittest

import (
	"encoding/json"
	"fmt"
	"reflect"
)

type ObjectConverter struct {
	vFromFunc reflect.Value
	vToFunc   reflect.Value
}

func newObjectConverter(vFromFunc reflect.Value, vToFunc reflect.Value) *ObjectConverter {
	return &ObjectConverter{
		vFromFunc: vFromFunc,
		vToFunc:   vToFunc,
	}
}

func (c *ObjectConverter) FromRawBytes(raw json.RawMessage) interface{} {
	inType := c.vFromFunc.Type().In(0)
	v := reflect.New(inType)
	if err := json.Unmarshal(raw, v.Interface()); err != nil {
		panic(fmt.Sprintf("Failed to parse json: %s\n", err))
	}
	return c.vFromFunc.Call([]reflect.Value{v.Elem()})[0].Interface()
}

func (c *ObjectConverter) ToJsonSerializable(obj interface{}) interface{} {
	return c.vToFunc.Call([]reflect.Value{reflect.ValueOf(obj)})[0].Interface()
}
