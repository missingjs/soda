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
class NestedIterator {
    struct Node {
        const vector<NestedInteger>& List;
        int index;
        Node(const vector<NestedInteger>& List)
            : List{List}, index{0}
        {}
        bool isEnd() const { return index >= int(List.size()); }
        int value() const { return current().getInteger(); }
        const NestedInteger& current() const {
            return List[index];
        }
        void advance() {
            ++index;
        }
    };
public:
    NestedIterator(vector<NestedInteger> &nestedList): stk{} {
        stk.push(new Node(nestedList));
        locate();
    }
    
    int next() {
        int value = stk.top()->value();
        stk.top()->advance();
        locate();
        return value;
    }
    
    bool hasNext() {
        return !stk.empty() && !stk.top()->isEnd();
    }
private:
    stack<Node*> stk;

    void locate() {
        while (!stk.empty()) {
            if (stk.top()->isEnd()) {
                delete stk.top();
                stk.pop();
                if (!stk.empty()) {
                    stk.top()->advance();
                }
            } else if (stk.top()->current().isInteger()) {
                break;
            } else {
                stk.push(new Node(stk.top()->current().getList()));
            }
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

class Solution {
public:
    vector<int> flatNested(vector<NestedInteger>& niList) {
        vector<int> res;
        NestedIterator iter{niList};
        while (iter.hasNext()) {
            int val = iter.next();
            res.push_back(val);
        }
        return res;
    }

    void dump(const NestedInteger& i) {
        if (i.isInteger()) {
            cerr << i.getInteger() << ' ';
        } else {
            for (auto& j : i.getList()) {
                dump(j);
            }
        }
    }
};

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
    auto work = WorkFactory::create(su, &Solution::flatNested);
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
