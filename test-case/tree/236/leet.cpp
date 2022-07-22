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
class Solution {
public:
    TreeNode* lowestCommonAncestor(TreeNode* root, TreeNode* p, TreeNode* q) {
        vector<TreeNode*> stk;
        stk.push_back(root);

        TreeNode* last = root;
        bool foundOne = false;
        int index = -1;

        if (root == p || root == q) {
            foundOne = true;
            index = 0;
        }

        while (!stk.empty()) {
            auto node = stk.back();
            if (node->left && last != node->left && last != node->right) {
                if (node->left == p || node->left == q) {
                    if (!foundOne) {
                        index = (int) stk.size();
                        foundOne = true;
                    } else {
                        return stk[index];
                    }
                }
                stk.push_back(node->left);
            } else if (node->right && last != node->right) {
                if (node->right == p || node->right == q) {
                    if (!foundOne) {
                        index = (int) stk.size();
                        foundOne = true;
                    } else {
                        return stk[index];
                    }
                }
                stk.push_back(node->right);
            } else {
                last = node;
                if (index == int(stk.size()) - 1) {
                    --index;
                }
                stk.pop_back();
            }
        }
        return nullptr;
    }
};

class Driver {
public:
    int exec(TreeNode* root, int p, int q) {
        auto pNode = findNode(root, p);
        auto qNode = findNode(root, q);
        return su.lowestCommonAncestor(root, pNode, qNode)->val;
    }

private:
    TreeNode* findNode(TreeNode* root, int val) {
        if (!root) {
            return nullptr;
        }
        if (root->val == val) {
            return root;
        }
        auto L = findNode(root->left, val);
        return L ? L : findNode(root->right, val);
    }

private:
    Solution su;
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
    Driver d;
    auto work = WorkFactory::create(d, &Driver::exec);
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

    // work->setValidator(validate);
    work->setCompareSerial(true);
    cout << work->run(Utils::fromStdin());
    delete work;
    return 0;
}
