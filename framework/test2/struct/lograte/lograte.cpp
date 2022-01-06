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
class Logger {
    map<string,int> msgMap;
    const int limit = 10;
    int lastTimestamp = -limit;
public:
    Logger() {
    }
    
    bool shouldPrintMessage(int timestamp, string message) {
        int T = lastTimestamp;
        lastTimestamp = timestamp;
        if (timestamp - T >= 10) {
            msgMap.clear();
            msgMap[message] = timestamp;
            return true;
        }
        if (msgMap.find(message) != msgMap.end() && timestamp - msgMap[message] < limit) {
            return false;
        }
        msgMap[message] = timestamp;
        return true;
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

    auto tester = WorkFactory::createStructTester<Logger>();
    // strtester->withFunction("shouldPrintMessage", &Logger::shouldPrintMessage);
    ADD_FUNCTION(tester, shouldPrintMessage)
    auto work = WorkFactory::forStruct(tester);

    // work->setValidator(validate);
    work->setCompareSerial(true);
    work->run();
    delete work;
    return 0;
}
