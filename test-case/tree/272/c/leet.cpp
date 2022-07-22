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
        vector<Node> nodes;
        collect(root, nodes, target);
        quickSelect(nodes, 0, int(nodes.size()) - 1, k);
        vector<int> res(k);
        for (int i = 0; i < k; ++i) {
            res[i] = nodes[i].value;
        }
        return res;
    }

    void quickSelect(vector<Node>& nodes, int start, int end, int index) {
        while (start < end) {
            int mid = (start + end) / 2;
            placeMedian3(nodes, start, mid, end);
            int k = partition(nodes, start, end, mid);
            if (k == index) {
                return;
            } else if (k > index) {
                end = k - 1;
            } else {
                start = k + 1;
            }
        }
    }

    int partition(vector<Node>& nodes, int start, int end, int pivot) {
        auto d = nodes[pivot].diff;
        swap(nodes[pivot], nodes[end]);
        int p = start;
        for (int i = start; i <= end; ++i) {
            if (nodes[i].diff < d) {
                if (p != i) {
                    swap(nodes[p], nodes[i]);
                }
                ++p;
            }
        }
        swap(nodes[p], nodes[end]);
        return p;
    }

    void placeMedian3(vector<Node>& nodes, int start, int mid, int end) {
        if (nodes[start].diff > nodes[mid].diff) {
            swap(nodes[start], nodes[mid]);
        }
        if (nodes[start].diff > nodes[end].diff) {
            swap(nodes[start], nodes[end]);
        }
        if (nodes[mid].diff > nodes[end].diff) {
            swap(nodes[mid], nodes[end]);
        }
    }

    void collect(TreeNode* root, vector<Node>& nodes, double target) {
        if (!root) {
            return;
        }
        nodes.push_back(Node::withTarget(root->val, target));
        collect(root->left, nodes, target);
        collect(root->right, nodes, target);
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
