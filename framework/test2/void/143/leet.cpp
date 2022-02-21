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
    void reorderList(ListNode* head) {
        auto slow = head;
        auto fast = head;
        while (fast->next && fast->next->next) {
            slow = slow->next;
            fast = fast->next->next;
        }
        if (slow == fast) {
            return;
        }

        auto r = reverse(slow->next);
        slow->next = nullptr;
        merge(head, r);
    }

private:
    ListNode* reverse(ListNode* head) {
        ListNode* q = nullptr;
        while (head) {
            auto next = head->next;
            head->next = q;
            q = head;
            head = next;
        }
        return q;
    }

    void merge(ListNode* L1, ListNode* L2) {
        ListNode h;
        auto t = &h;
        while (L1 && L2) {
            t->next = L1;
            t = L1;
            L1 = L1->next;
            t->next = L2;
            t = L2;
            L2 = L2->next;
        }
        t->next = L1 ? L1 : L2;
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
    auto work = WorkFactory::create(su, &Solution::reorderList);
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
    // auto work = WorkFactory<Class>();

    // work->setValidator(validate);
    work->setCompareSerial(true);
    cout << work->run(Utils::fromStdin());
    delete work;
    return 0;
}
