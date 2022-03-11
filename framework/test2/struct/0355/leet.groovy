import soda.groovy.leetcode.*
import soda.groovy.unittest.*

class Twitter {

    static class Tweet {
        final int id
        final int ts
        Tweet(int id, int ts) {
            this.id = id
            this.ts = ts
        }
    }

    static class Node {
        final List<Tweet> tweetList
        int index
        Node(List<Tweet> t) {
            tweetList = t
            index = t.size()-1
        }
        Tweet current() {
            return tweetList[index]
        }
        boolean isEnd() {
            return index == -1
        }
    }

    private int timestamp

    private Map<Integer, Deque<Tweet>> tweets = new HashMap<>()

    private Map<Integer, Set<Integer>> follows = new HashMap<>()

    static final int Limit = 10

    void postTweet(int userId, int tweetId) {
        def qu = tweets.computeIfAbsent(userId, k -> new ArrayDeque<>())
        qu.offerLast(new Tweet(tweetId, nextTimestamp()))
        if (qu.size() > Limit) {
            qu.pollFirst()
        }
    }
    
    List<Integer> getNewsFeed(int userId) {
        def s = new ArrayList<>(follows.computeIfAbsent(userId, k -> new HashSet<>()))
        s << userId
        def pq = new PriorityQueue<Node>((n1, n2) -> n2.current().ts - n1.current().ts)
        for (int user : s) {
            def tq = tweets[user]
            if (tq != null && tq.size() > 0) {
                pq.offer(new Node(new ArrayList<>(tq)))
            }
        }
        def res = new ArrayList<Integer>()
        for (int i = 0; i < Limit && pq.size() > 0; ++i) {
            def node = pq.poll()
            res.add(node.current().id)
            --node.index
            if (!node.isEnd()) {
                pq.offer(node)
            }
        }
        res
    }
    
    public void follow(int followerId, int followeeId) {
        def s = follows.computeIfAbsent(followerId, k -> new HashSet<>())
        s << followeeId
    }
    
    public void unfollow(int followerId, int followeeId) {
        def s = follows.computeIfAbsent(followerId, k -> new HashSet<>())
        s.remove(followeeId)
    }

    private int nextTimestamp() {
        return ++timestamp
    }
}

// def work = TestWork.create(new Solution().&add)
def work = TestWork.forStruct(Twitter)
// work.validator = { i, j -> i == j }
work.compareSerial = true
println work.run(System.in.getText('UTF-8'))
