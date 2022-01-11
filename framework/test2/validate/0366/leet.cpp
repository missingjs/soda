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
    vector<vector<int>> findLeaves(TreeNode* root) {
        return method2(root);
    }

    vector<vector<int>> method2(TreeNode* root) {
        vector<vector<int>> res(100);
        int r = solve2(root, res);
        res.resize(r);
        return res;
    }

    int solve2(TreeNode* root, vector<vector<int>>& res) {
        if (!root) {
            return 0;
        }
        int R = solve2(root->right, res);
        int L = solve2(root->left, res);
        int index = max(L,R);
        res[index].push_back(root->val);
        return index + 1;
    }

    vector<vector<int>> method1(TreeNode* root) {
        vector<vector<int>> res;
        vector<TreeNode*> curr, next;
        unordered_map<TreeNode*,TreeNode*> parentMap;
        unordered_map<TreeNode*,int> childCounts;
        collect(root, curr, parentMap, childCounts);
        while (curr.size()) {
            next.clear();
            vector<int> team;
            for (auto node : curr) {
                team.push_back(node->val);
                if (node != root) {
                    auto p = parentMap[node];
                    if (--childCounts[p] == 0) {
                        next.push_back(p);
                    }
                }
            }
            res.push_back(std::move(team));
            std::swap(curr, next);
        }
        return res;
    }

    void collect(TreeNode* root, vector<TreeNode*>& curr,
            unordered_map<TreeNode*,TreeNode*>& parentMap,
            unordered_map<TreeNode*,int>& childCounts) {
        if (!root) {
            return;
        }

        int c = 0;
        if (root->left) {
            ++c;
            parentMap[root->left] = root;
            collect(root->left, curr, parentMap, childCounts);
        }

        if (root->right) {
            ++c;
            parentMap[root->right] = root;
            collect(root->right, curr, parentMap, childCounts);
        }

        if (c == 0) {
            curr.push_back(root);
        }
        childCounts[root] = c;
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
    auto work = WorkFactory::create(su, &Solution::findLeaves);
    //
    // [2] or, create by ordinary function
    // auto work = WorkFactory::create(function);
    //
    // [3] or, create for struct tester
    // auto tester = WorkFactory::createStructTester<Class,Args...>();
    // ADD_FUNCTION(tester, funcname)
    // auto work = WorkFactory::forStruct(tester);

    work->setValidator(Validators::forVector2d<int>(true,false));
    work->setCompareSerial(true);
    work->run();
    delete work;
    return 0;
}
