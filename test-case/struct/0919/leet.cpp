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
class CBTInserter {
    deque<TreeNode*> qu;
    TreeNode* root;
public:
    CBTInserter(TreeNode* root): qu{}, root{root} {
        qu.push_back(root);
        while (!qu.empty()) {
            auto node = qu.front();
            if (!node->left) {
                break;
            }
            qu.push_back(node->left);
            if (!node->right) {
                break;
            }
            qu.push_back(node->right);
            qu.pop_front();
        }
    }
    
    // @member_function
    int insert(int v) {
        auto node = new TreeNode(v);
        auto head = qu.front();
        qu.push_back(node);
        if (!head->left) {
            head->left = node;
        } else {
            head->right = node;
            qu.pop_front();
        }
        return head->val;
    }
    
    // @member_function
    TreeNode* get_root() {
        return root;
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
    // auto tester = WorkFactory::createStructTester<CBTInserter,TreeNode*>();
    // ADD_FUNCTION(tester, insert)
    // ADD_FUNCTION(tester, get_root)
    // auto work = WorkFactory::forStruct(tester);
    auto work = WorkFactory::forStruct<CBTInserter>();

    // work->setValidator(validate);
    work->setCompareSerial(true);
    work->run();
    delete work;
    return 0;
}
