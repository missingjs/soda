package util

import "encoding/json"

type OptionalInt struct {
    value    int
    hasValue bool
}

func (this *OptionalInt) IsNull() bool {
    return !this.hasValue
}

func (this *OptionalInt) MakeNull() {
    this.value = 0
    this.hasValue = false
}

func (this *OptionalInt) Set(v int) {
    this.value = v
    this.hasValue = true
}

func (this *OptionalInt) Get() int {
    return this.value
}

type OptionalIntSlice []OptionalInt

func (this *OptionalIntSlice) UnmarshalJSON(data []byte) error {
    var temp []json.RawMessage
    if err := json.Unmarshal(data, &temp); err != nil {
        return err
    }
    var res OptionalIntSlice
    for _, v := range temp {
        var opInt OptionalInt
        if string(v) != "null" {
            if err := json.Unmarshal(v, &opInt.value); err != nil {
                return err
            }
            opInt.hasValue = true
        }
        res = append(res, opInt)
    }
    *this = res
    return nil
}

func (self OptionalIntSlice) MarshalJSON() ([]byte, error) {
    out := make([]interface{}, 0)
    for _, v := range self {
        if v.IsNull() {
            out = append(out, nil)
        } else {
            out = append(out, v.value)
        }
    }
    return json.Marshal(out)
}

// func main() {
//     jsonText := `[1,2,3,null,5,6,7,null,9,10]`
//     var ints OptionalIntSlice
//     if err := json.Unmarshal([]byte(jsonText), &ints); err != nil {
//         fmt.Printf("json unmarshaling error: %s\n", err)
//         return
//     }
//     for _, v := range ints {
//         if v.IsNull() {
//             fmt.Println(nil)
//         } else {
//             fmt.Println(v.Get())
//         }
//     }
//     if b, err := json.Marshal(ints); err != nil {
//         fmt.Printf("json marshaling error: %s\n", err)
//     } else {
//         fmt.Printf(string(b))
//     }
// }
