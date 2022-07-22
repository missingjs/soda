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
    NestedInteger deserialize(string s) {
        p = 0;
        return parse(s);
    }

    NestedInteger parse(const string& s) {
        if (s[p] == '[') {
            ++p;
            NestedInteger root;
            while (s[p] != ']') {
                root.add(parse(s));
                if (s[p] == ',') {
                    ++p;
                }
            }
            ++p;
            return root;
        }

        bool negative = false;
        if (s[p] == '-') {
            ++p;
            negative = true;
        }

        int value = 0;
        while (p < s.size() && s[p] >= '0' && s[p] <= '9') {
            value = value * 10 + s[p] - '0';
            ++p;
        }
        if (negative) {
            value = 0 - value;
        }
        return NestedInteger{value};
    }

private:
    int p;
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
    auto work = WorkFactory::create(su, &Solution::deserialize);
    //
    // [2] or, create by ordinary function
    // auto work = WorkFactory::create(function);
    //
    // [3] or, create for struct tester
    // auto tester = WorkFactory::createStructTester<Class,Args...>();
    // ADD_FUNCTION(tester, funcname)
    // auto work = WorkFactory::forStruct(tester);

    // work->setValidator(validate);
    work->setCompareSerial(true);
    work->run();
    delete work;
    return 0;
}
