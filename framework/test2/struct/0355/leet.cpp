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
class Twitter {
public:
    struct Tweet {
        int id, ts;
        Tweet(int id, int ts): id{id}, ts{ts} {}
    };

    struct Node {
        const deque<Tweet>& tweets;
        int index;
        Node(const deque<Tweet>& t): tweets{t}, index{int(t.size())-1} {}
        Tweet current() const {
            return tweets[index];
        }
        bool isEnd() const {
            return index == -1;
        }
    };

    int timestamp {0};
    unordered_map<int,deque<Tweet>> tweets;
    unordered_map<int,unordered_set<int>> follows;
    static const int Limit = 10;

    Twitter() {
        
    }
    
    // @member_function
    void postTweet(int userId, int tweetId) {
        tweets[userId].push_back(Tweet(tweetId, nextTimestamp()));
        if (tweets[userId].size() > Limit) {
            tweets[userId].pop_front();
        }
    }
    
    // @member_function
    vector<int> getNewsFeed(int userId) {

        auto cmp = [](Node* n1, Node* n2) {
            return n1->current().ts < n2->current().ts;
        };
        priority_queue<Node*, vector<Node*>, decltype(cmp)> pq(cmp);

        vector<int> s(follows[userId].begin(), follows[userId].end());
        s.push_back(userId);
        for (int user : s) {
            if (tweets[user].size() > 0) {
                pq.push(new Node(tweets[user]));
            }
        }

        vector<int> res;
        for (int i = 0; i < Limit && !pq.empty(); ++i) {
            auto node = pq.top(); pq.pop();
            res.push_back(node->current().id);
            --node->index;
            if (!node->isEnd()) {
                pq.push(node);
            }
        }
        return res;
    }
    
    // @member_function
    void follow(int followerId, int followeeId) {
        follows[followerId].insert(followeeId);
    }
    
    // @member_function
    void unfollow(int followerId, int followeeId) {
        follows[followerId].erase(followeeId);
    }

    int nextTimestamp() {
        return ++timestamp;
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
    // auto tester = WorkFactory::createStructTester<Twitter>();
    // ADD_FUNCTION(tester, postTweet)
    // ADD_FUNCTION(tester, getNewsFeed)
    // ADD_FUNCTION(tester, follow)
    // ADD_FUNCTION(tester, unfollow)
    // auto work = WorkFactory::forStruct(tester);
    auto work = WorkFactory::forStruct<Twitter>();

    // work->setValidator(validate);
    work->setCompareSerial(true);
    work->run();
    delete work;
    return 0;
}
