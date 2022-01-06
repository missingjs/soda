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
class TopVotedCandidate {
    int N;
    vector<int> times;
    vector<int> winner;
public:
    TopVotedCandidate(vector<int>& persons, vector<int>& times):
        N(persons.size()), times(times), winner(persons.size())
    {
        vector<int> counter(N+1, 0);
        int win = 0;
        for (int i = 0; i < N; ++i) {
            if (++counter[persons[i]] >= counter[win]) {
                win = persons[i];
            }
            winner[i] = win;
        }
    }
    
    int q(int t) {
        if (t >= times.back()) {
            return winner[N-1];
        }
        int low = 0, high = N-1;
        while (low < high) {
            int mid = (low + high) / 2;
            if (t <= times[mid]) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }
        return t == times[low] ? winner[low] : winner[low-1];
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
    //
    // [3] or, create for struct tester
    auto tester = WorkFactory::createStructTester<TopVotedCandidate,vector<int>,vector<int>>();
    ADD_FUNCTION(tester, q)
    auto work = WorkFactory::forStruct(tester);

    // work->setValidator(validate);
    work->setCompareSerial(true);
    work->run();
    delete work;
    return 0;
}
