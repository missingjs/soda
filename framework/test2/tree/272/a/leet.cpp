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
class Node {
public:
    double diff;
    int value;

    static Node withTarget(int value, double target) {
        return Node{ std::abs(target - value), value };
    }

    string str() const {
        char buf[64];
        snprintf(buf, sizeof(buf), "%.3f|%d", diff, value);
        return string(buf);
    }
};

class Solution {
public:
    vector<int> closestKValues(TreeNode* root, double target, int k) {
        auto comp = [](const Node& n1, const Node& n2) {
            return n1.diff < n2.diff;
        };
        priority_queue<Node, vector<Node>, decltype(comp)> queue(comp);
        solve(root, target, k, queue);

        vector<int> res;
        while (!queue.empty()) {
            res.push_back(queue.top().value);
            queue.pop();
        }
        return res;
    }

    template <typename Queue>
    void solve(TreeNode* root, double target, int k, Queue& queue) {
        if (!root) {
            return;
        }

        Node node = Node::withTarget(root->val, target);
        if (queue.size() == k) {
            if (node.diff < queue.top().diff) {
                queue.pop();
                queue.push(node);
            }
        } else {
            queue.push(node);
        }

        solve(root->left, target, k, queue);
        solve(root->right, target, k, queue);
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
    auto work = WorkFactory::create(su, &Solution::closestKValues);
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

    work->setValidator(Validators::forVector<int>(false));
    work->setCompareSerial(true);
    cout << work->run(Utils::fromStdin());
    delete work;
    return 0;
}
