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
class Solution {
public:
    void reverseWords(vector<char>& s) {
        if (s.size() == 0) {
            return;
        }

        reverse(s, 0, s.size() - 1);

        int N = s.size();
        int i = 0, j = 0;
        while (j < N) {
            if (s[j] == ' ') {
                reverse(s, i, j-1);
                i = j + 1;
            }
            ++j;
        }
        if (i < j) {
            reverse(s, i, j-1);
        }
    }

    void reverse(vector<char>& s, int i, int j) {
        while (i < j) {
            std::swap(s[i], s[j]);
            ++i;
            --j;
        }
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
    Solution su;
    auto work = WorkFactory::create(su, &Solution::reverseWords);
    //
    // [2] or, create by ordinary function
    // auto work = WorkFactory::create(function);
    //
    // [3] or, create for struct tester
    // auto tester = NEW_STRUCT_TESTER(Class,Args...)
    // ADD_FUNCTION(tester, funcname)
    // auto worker = WorkFactory::forStruct(tester);

    // work->setValidator(validate);
    work->setCompareSerial(true);
    work->run();
    delete work;
    return 0;
}
