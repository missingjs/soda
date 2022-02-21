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
class VirIndex {
public:
    VirIndex(vector<int>& nums): nums{nums} {}

    int& operator[](int i) {
        int N = (int) nums.size();
        int p = -1;
        if ((N&1) == 1 || i > ((N-1)>>1)) {
            p = (((N-i)<<1)-1) % N;
        } else {
            p = (N - 2 - (i<<1));
        }
        return nums[p];
    }

private:
    vector<int>& nums;
};

class Solution {
public:
    void wiggleSort(vector<int>& nums) {
        VirIndex vi(nums);
        int N = (int) nums.size();
        quickSelect(vi, 0, N-1, (N-1)/2);
    }

private:
    void quickSelect(VirIndex& vi, int start, int end, int k) {
        int p0 = 0, p1 = 0;
        while (start < end) {
            partition(vi, start, end, &p0, &p1);
            if (k >= p0 && k <= p1) {
                return;
            }
            if (k > p1) {
                start = p1 + 1;
            } else {
                end = p0 - 1;
            }
        }
    }

    void partition(VirIndex& vi, int start, int end, int* p0, int* p1) {
        int mid = (start + end) / 2;
        int pivot = getMedian(vi[start], vi[mid], vi[end]);
        int p = start, z = end + 1;
        for (int q = start; q < z; ) {
            if (vi[q] < pivot) {
                std::swap(vi[p], vi[q]);
                ++p;
                ++q;
            } else if (vi[q] == pivot) {
                ++q;
            } else {
                --z;
                std::swap(vi[z], vi[q]);
            }
        }
        *p0 = p;
        *p1 = z - 1;
    }

    int getMedian(int a, int b, int c) {
        if (a >= b) {
            return b >= c ? b : min(a, c);
        } else {
            return a >= c ? a : min(b, c);
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
    auto work = WorkFactory::create(su, &Solution::wiggleSort);
    //
    // [2] or, create by ordinary function
    // auto work = WorkFactory::create(function);
    //
    // [3] or, create for struct tester
    // auto tester = WorkFactory::createStructTester<Class,Args...>();
    // ADD_FUNCTION(tester, funcname)
    // auto work = WorkFactory::forStruct(tester);
    //
    // [4] or, create for struct tester (simplified)
    // auto work = WorkFactory::forStruct<Class>();

    auto valid = [](auto& e, auto& nums) {
        for (int i = 1; i < (int) nums.size(); ++i) {
            if (i % 2 != 0 && nums[i] <= nums[i-1] || i % 2 == 0 && nums[i] >= nums[i-1]) {
                return false;
            }
        }
        return true;
    };
    work->setValidator(valid);
    work->setCompareSerial(true);
    cout << work->run(Utils::fromStdin());
    delete work;
    return 0;
}
