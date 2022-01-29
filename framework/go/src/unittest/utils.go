package unittest

import (
    "bufio"
    "bytes"
    "missingjs.com/soda/util"
    "os"
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
