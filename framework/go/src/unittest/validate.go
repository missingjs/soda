package unittest

import (
	"reflect"
)

type ValidatorFunc func(interface{}, interface{}) bool

type SliceEqual struct {
	ElementEqualFunc ValidatorFunc
}

func (s *SliceEqual) Validate(s1 interface{}, s2 interface{}) bool {
	v1 := reflect.ValueOf(s1)
	v2 := reflect.ValueOf(s2)

	if v1.Len() != v2.Len() {
		return false
	}

	N := v1.Len()
	visited := make([]bool, N)
	for i := 0; i < N; i++ {
		e1 := v1.Index(i)
		exist := false
		for j := 0; j < N; j++ {
			e2 := v2.Index(j)
			if !visited[j] && s.ElementEqualFunc(e1.Interface(), e2.Interface()) {
				visited[j] = true
				exist = true
				break
			}
		}
		if !exist {
			return false
		}
	}
	return true
}

type ValidatorsType struct{}

var Validators ValidatorsType

func (v *ValidatorsType) Slice1d() ValidatorFunc {
	se := &SliceEqual{
		ElementEqualFunc: func(i interface{}, j interface{}) bool {
			//fmt.Fprintf(os.Stderr, "%v, %v\n", i, j)
			return reflect.DeepEqual(i, j)
		},
	}
	return se.Validate
}

func (v *ValidatorsType) Slice2d() ValidatorFunc {
	se := &SliceEqual{
		ElementEqualFunc: v.Slice1d(),
	}
	return se.Validate
}
