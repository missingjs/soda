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
    int depthSumInverse(vector<NestedInteger>& nestedList) {
        return method3(nestedList);
    }

    struct Info {
        int sum{0}, product{0}, maxDepth{0};
    };

    int method3(vector<NestedInteger>& nestedList) {
        auto info = getInfo(nestedList, 1);
        return (info.maxDepth + 1) * info.sum - info.product;
    }

    int method2(vector<NestedInteger>& nestedList) {
        int A = 0, B = 0, maxDepth = 0;
        for (auto& ni : nestedList) {
            int d = solve2(ni, 1, &A, &B);
            maxDepth = std::max(maxDepth, d);
        }
        return A + B * maxDepth;
    }

    int method1(vector<NestedInteger>& nestedList) {
        int maxDepth = 0;
        for (auto& ni : nestedList) {
            maxDepth = std::max(maxDepth, getDepth(ni, 1));
        }
        int res = 0;
        for (auto& ni : nestedList) {
            res += calc(ni, 1, maxDepth);
        }
        return res;
    }

private:
    Info getInfo(const vector<NestedInteger>& nestedList, int depth) {
        int sum = 0, product = 0, maxDepth = depth;
        for (auto& ni : nestedList) {
            if (ni.isInteger()) {
                int val = ni.getInteger();
                sum += val;
                product += val * depth;
                maxDepth = std::max(maxDepth, depth);
            } else {
                Info res = getInfo(ni.getList(), depth+1);
                sum += res.sum;
                product += res.product;
                maxDepth = std::max(maxDepth, res.maxDepth);
            }
        }
        return Info{sum, product, maxDepth};
    }

    int solve2(const NestedInteger& ni, int depth, int *A, int *B) {
        if (ni.isInteger()) {
            int val = ni.getInteger();
            *A += val * (1 - depth);
            *B += val;
            return depth;
        }
        int md = depth;
        for (auto& j : ni.getList()) {
            md = std::max(md, solve2(j, depth+1, A, B));
        }
        return md;
    }

    int calc(const NestedInteger& ni, int depth, int maxDepth) {
        if (ni.isInteger()) {
            return ni.getInteger() * (maxDepth - depth + 1);
        }
        int res = 0;
        for (auto& j : ni.getList()) {
            res += calc(j, depth+1, maxDepth);
        }
        return res;
    }

    int getDepth(const NestedInteger& ni, int depth) {
        if (ni.isInteger()) {
            return depth;
        }
        int d = depth;
        for (auto& j : ni.getList()) {
            d = std::max(d, getDepth(j, depth+1));
        }
        return d;
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
    auto work = WorkFactory::create(su, &Solution::depthSumInverse);
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
