package unittest

import (
    "fmt"
)

func DecodeByteSlice(strArr []string) []byte {
    res := make([]byte, len(strArr))
    for idx, s := range strArr {
        res[idx] = s[0]
    }
    return res
}

func EncodeByteSlice(bArr []byte) []string {
    res := make([]string, len(bArr))
    for i, b := range bArr {
        res[i] = fmt.Sprintf("%c", b)
    }
    return res
}

func DecodeByteSlice2D(strArr2d [][]string) [][]byte {
    b2d := make([][]byte, len(strArr2d))
    for i, sa := range strArr2d {
        b2d[i] = DecodeByteSlice(sa)
    }
    return b2d
}

func EncodeByteSlice2D(bArr2d [][]byte) [][]string {
    s2d := make([][]string, len(bArr2d))
    for i, ba := range bArr2d {
        s2d[i] = EncodeByteSlice(ba)
    }
    return s2d
}

