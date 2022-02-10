package unittest

import (
    "bufio"
    "bytes"
    "encoding/json"
    "os"

    "missingjs.com/soda/util"
)

func getLogger() *util.SimpleLogger {
    return &util.Logger
}

type UtilsType struct{}

var Utils = UtilsType{}

func (u *UtilsType) FromStdin() string {
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
    return string(buffer.Bytes())
}

func (u *UtilsType) UnmarshalUseNumber(data []byte, target interface{}) error {
    decoder := json.NewDecoder(bytes.NewReader(data))
    decoder.UseNumber()
    return decoder.Decode(target)
}
