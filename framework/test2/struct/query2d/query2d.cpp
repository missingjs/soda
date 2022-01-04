#ifdef __clang__
    #include "soda/clang/stdc++.h"
#else
    #include <bits/stdc++.h>
#endif
#include "soda/leetcode/leetcode.h"
#include "soda/unittest/unittest.h"

using namespace std;
using namespace soda::leetcode;
using namespace soda::unittest;

// step [1]: implement class Solution
// class Solution {};
class NumMatrix {
    int rows, cols;
    vector<vector<int>> matrix;
    vector<vector<int>> bit;
public:
    NumMatrix(vector<vector<int>>& matrix):
        rows{int(matrix.size())+1}, cols{int(matrix[0].size())+1},
        matrix(rows-1, vector<int>(cols-1)),
        bit(rows, vector<int>(cols))
    {
        for (int i = 0; i < rows - 1; ++i) {
            for (int j = 0; j < cols - 1; ++j) {
                update(i, j, matrix[i][j]);
            }
        }
    }
    
    void update(int row, int col, int val) {
        int diff = val - matrix[row][col];
        matrix[row][col] = val;
        for (int i = row+1; i < rows; i += (i & -i)) {
            for (int j = col+1; j < cols; j += (j & -j)) {
                bit[i][j] += diff;
            }
        }
    }
    
    int sumRegion(int row1, int col1, int row2, int col2) {
        return query(row1, col1) - query(row1, col2+1) - query(row2+1, col1) + query(row2+1, col2+1);
    }

private:
    int query(int r, int c) {
        int res = 0;
        for (int i = r; i > 0; i -= (i & -i)) {
            for (int j = c; j > 0; j -= (j & -j)) {
                res += bit[i][j];
            }
        }
        return res;
    }
};

namespace {
    const auto __ = []() {
        ios_base::sync_with_stdio(false);
        cin.tie(nullptr);
        return 0;
    }();
}

int main()
{
    // [1] create by class member function
    // Solution su;
    // auto work = WorkFactory::create(su, &Solution::function);
    //
    // [2] or, create by ordinary function
    // auto work = WorkFactory::create(function);

    // auto tester = WorkFactory::createStructTester<NumMatrix,vector<vector<int>>>();
    // tester->withFunction("update", &NumMatrix::update);
    // tester->withFunction("sumRegion", &NumMatrix::sumRegion);
    auto tester = NEW_STRUCT_TESTER(NumMatrix,vector<vector<int>>)
    ADD_FUNCTION(tester, update)
    ADD_FUNCTION(tester, sumRegion)
    auto work = WorkFactory::forStruct(tester);

    // work->setValidator(validate);
    work->setCompareSerial(true);
    work->run();
    delete work;
    return 0;
}
