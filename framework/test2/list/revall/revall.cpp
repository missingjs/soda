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
    vector<ListNode*> reverseAll(vector<ListNode*>& lists) {
        for (int i = 0; i < int(lists.size()); ++i) {
            lists[i] = reverse(lists[i]);
        }
        int i = 0, j = int(lists.size()) - 1;
        while (i < j) {
            swap(lists[i], lists[j]);
            ++i;
            --j;
        }
        return lists;
    }

    ListNode* reverse(ListNode* head) {
        ListNode* h = nullptr;
        while (head) {
            auto next = head->next;
            head->next = h;
            h = head;
            head = next;
        }
        return h;
    }
};

namespace {
    const auto __ = []() {
        ios_base::sync_with_stdio(false);
        cin.tie(nullptr);
        return 0;
    }();
}

// [optional] use custom type with parser and serializer,
// and if you use this macro, may be you should define your own validator
// USE_CUSTOM_SERIALIZER(type)

// [optional] instantiate json access type. This will increase compile time
// #include "soda/unittest/json_access.h"
// SODA_JSON_ACCESS_TYPE(type)

int main()
{
    // [1] create by class member function
    Solution su;
    auto work = WorkFactory::create(su, &Solution::reverseAll);
    //
    // [2] or, create by ordinary function
    // auto work = WorkFactory::create(function);

    // work->setValidator(validate);
    work->setCompareSerial(true);
    // work->setArgParser<0,from_type>(parse_func);
    // work->setResultParser<from_type>(parse_func);
    // work->setResultSerializer(serial_func);
    work->run();
    delete work;
    return 0;
}
