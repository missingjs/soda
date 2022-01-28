import soda.unittest.*;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.*;

import soda.leetcode.*;
import soda.unittest.Validators;
import soda.unittest.work.TestWork;


class Twitter {

    public static class Tweet {
        public final int id;
        public final int ts;
        public Tweet(int id, int ts) {
            this.id = id;
            this.ts = ts;
        }
    }

    public static class Node {
        public final List<Tweet> tweetList;
        public int index;
        public Node(List<Tweet> t) {
            tweetList = t;
            index = t.size()-1;
        }
        public Tweet current() {
            return tweetList.get(index);
        }
        public boolean isEnd() {
            return index == -1;
        }
    }

    private int timestamp;

    private Map<Integer, Deque<Tweet>> tweets = new HashMap<>();

    private Map<Integer, Set<Integer>> follows = new HashMap<>();

    public static final int Limit = 10;

    public Twitter() {
        
    }
    
    public void postTweet(int userId, int tweetId) {
        var qu = tweets.computeIfAbsent(userId, k -> new ArrayDeque<>());
        qu.offerLast(new Tweet(tweetId, nextTimestamp()));
        if (qu.size() > Limit) {
            qu.pollFirst();
        }
    }
    
    public List<Integer> getNewsFeed(int userId) {
        var s = new ArrayList<>(follows.computeIfAbsent(userId, k -> new HashSet<>()));
        s.add(userId);
        var pq = new PriorityQueue<Node>((n1, n2) -> -Integer.compare(n1.current().ts, n2.current().ts));
        for (int user : s) {
            var tq = tweets.get(user);
            if (tq != null && tq.size() > 0) {
                pq.offer(new Node(new ArrayList<>(tq)));
            }
        }
        var res = new ArrayList<Integer>();
        for (int i = 0; i < Limit && pq.size() > 0; ++i) {
            var node = pq.poll();
            res.add(node.current().id);
            --node.index;
            if (!node.isEnd()) {
                pq.offer(node);
            }
        }
        return res;
    }
    
    public void follow(int followerId, int followeeId) {
        var s = follows.computeIfAbsent(followerId, k -> new HashSet<>());
        s.add(followeeId);
    }
    
    public void unfollow(int followerId, int followeeId) {
        var s = follows.computeIfAbsent(followerId, k -> new HashSet<>());
        s.remove(followeeId);
    }

    private int nextTimestamp() {
        return ++timestamp;
    }
}

public class Leet implements Supplier<TestWork> {

    @Override
    public TestWork get() {
        // var work = new TestWork(new Solution(), "METHOD");
        var work = TestWork.forStruct(Twitter.class);
        // work.setValidator((e, r) -> {...});
        work.setCompareSerial(true);
        return work;
    }

    public static void main(String[] args) throws Exception {
        new Leet().get().run();
    }

}
