package main

import (
    . "missingjs.com/soda/leetcode"
    "missingjs.com/soda/unittest"
    "missingjs.com/soda/util"
)

var logger = util.Logger
var _used ListNode

type NumMatrix struct {
    Matrix [][]int
    Bit    [][]int
}


func Constructor(matrix [][]int) NumMatrix {
    rows, cols := len(matrix), len(matrix[0])

    var na NumMatrix
    na.Matrix = make([][]int, rows)
    for i := 0; i < rows; i++ {
        na.Matrix[i] = make([]int, cols)
    }

    na.Bit = make([][]int, rows+1)
    for i := 0; i <= rows; i++ {
        na.Bit[i] = make([]int, cols+1)
    }

    for i := 0; i < rows; i++ {
        for j := 0; j < cols; j++ {
            na.Update(i, j, matrix[i][j])
        }
    }

    return na
}


func (this *NumMatrix) Update(row int, col int, val int)  {
    diff := val - this.Matrix[row][col]
    this.Matrix[row][col] = val
    for i := row+1; i < len(this.Bit); i += (i & -i) {
        for j := col+1; j < len(this.Bit[0]); j += (j & -j) {
            this.Bit[i][j] += diff
        }
    }
}


func (this *NumMatrix) SumRegion(row1 int, col1 int, row2 int, col2 int) int {
    return this.getSum(row2+1, col2+1) + this.getSum(row1, col1) - this.getSum(row2+1, col1) - this.getSum(row1, col2+1)
}


func (this *NumMatrix) getSum(bRow, bCol int) int {
    res := 0
    for i := bRow; i > 0; i -= (i & -i) {
        for j := bCol; j > 0; j -= (j & -j) {
            res += this.Bit[i][j]
        }
    }
    return res
}

func main() {
    // create tester by function
    // work := unittest.CreateWork(FUNCTION)
    // OR create by struct factory
    work := unittest.CreateWorkForStruct(Constructor)
    // work.SetValidator(func(e,r)bool)
    work.CompareSerial = true
    work.Run()
}
