using Soda.Unittest;
using Soda.Leetcode;

public class Twitter {

    class Tweet {
        public int id;
        public int ts;
        public Tweet(int id, int ts) {
            this.id = id;
            this.ts = ts;
        }
    }

    class Node {
        public IList<Tweet> tweetList;
        public int index;
        public Node(IList<Tweet> t) {
            tweetList = t;
            index = t.Count-1;
        }
        public Tweet Current() {
            return tweetList[index];
        }
        public bool IsEnd() {
            return index == -1;
        }
    }

    private int timestamp;

    private IDictionary<int, Queue<Tweet>> tweets = new Dictionary<int, Queue<Tweet>>();

    private IDictionary<int, ISet<int>> follows = new Dictionary<int, ISet<int>>();

    private static readonly int Limit = 10;

    public Twitter() {
        
    }
    
    public void PostTweet(int userId, int tweetId) {
        Queue<Tweet> qu = null;
        if (!tweets.TryGetValue(userId, out qu)) {
            tweets[userId] = qu = new Queue<Tweet>();
        }
        qu.Enqueue(new Tweet(tweetId, nextTimestamp()));
        if (qu.Count > Limit) {
            qu.Dequeue();
        }
    }
    
    public IList<int> GetNewsFeed(int userId) {
        ISet<int> s = null;
        if (!follows.TryGetValue(userId, out s)) {
            follows[userId] = s = new HashSet<int>();
        }
        s.Add(userId);
        var pq = new PriorityQueue<Node, int>();
        foreach (int user in s) {
            Queue<Tweet> tq = null;
            if (tweets.TryGetValue(user, out tq) && tq.Count > 0) {
                var node = new Node(new List<Tweet>(tq));
                pq.Enqueue(node, -node.Current().ts);
            }
        }
        var res = new List<int>();
        for (int i = 0; i < Limit && pq.Count > 0; ++i) {
            var node = pq.Dequeue();
            res.Add(node.Current().id);
            --node.index;
            if (!node.IsEnd()) {
                pq.Enqueue(node, -node.Current().ts);
            }
        }
        return res;
    }
    
    public void Follow(int followerId, int followeeId) {
        ISet<int> s = null;
        if (!follows.TryGetValue(followerId, out s)) {
            follows[followerId] = s = new HashSet<int>();
        }
        s.Add(followeeId);
    }
    
    public void Unfollow(int followerId, int followeeId) {
        ISet<int> s = null;
        if (!follows.TryGetValue(followerId, out s)) {
            follows[followerId] = s = new HashSet<int>();
        }
        s.Remove(followeeId);
    }

    private int nextTimestamp() {
        return ++timestamp;
    }
}

public class Leet
{
    public static void Main(string[] args)
    {
        // var work = WorkFactory.Create(Utils.Fn(new Solution().Add));
        var work = WorkFactory.ForStruct<Twitter>();
        // work.SetValidator((e, r) => ...);
        work.CompareSerial = true;
        Console.WriteLine(work.Run(Utils.ReadStdin()));
    }
}
