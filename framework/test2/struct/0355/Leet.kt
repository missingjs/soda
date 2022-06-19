import java.util.ArrayDeque
import java.util.PriorityQueue
import kotlin.math.*
import soda.kotlin.leetcode.*
import soda.kotlin.unittest.*

class Twitter() {

    class Tweet(val id: Int, val ts: Int)

    class Node(private val tweetList: List<Tweet>) {
        var index = tweetList.size - 1
        fun current() = tweetList[index]
        fun isEnd() = index == -1
    }

    var timestamp = 0
    val tweets = mutableMapOf<Int, ArrayDeque<Tweet>>()
    val follows = mutableMapOf<Int, MutableSet<Int>>()
    val limit = 10

    fun postTweet(userId: Int, tweetId: Int) {
        val qu = tweets.getOrPut(userId, { ArrayDeque<Tweet>() })
        qu.offerLast(Tweet(tweetId, nextTimestamp()))
        if (qu.size > limit) {
            qu.pollFirst()
        }
    }

    fun getNewsFeed(userId: Int): List<Int> {
        val s = follows.getOrPut(userId, { mutableSetOf() }).toMutableList()
        s += userId
        val pq = PriorityQueue<Node>(compareBy {-it.current().ts})
        for (user in s) {
            tweets[user]?.let {
                if (!it.isEmpty()) {
                    pq.offer(Node(it.toList()))
                }
            }
        }
        val res = mutableListOf<Int>()
        var i = 0
        while (i < limit && !pq.isEmpty()) {
            val node = pq.poll()
            res += node.current().id
            --node.index
            if (!node.isEnd()) {
                pq.offer(node)
            }
            ++i
        }
        return res.toList()
    }

    fun follow(followerId: Int, followeeId: Int) {
        follows.getOrPut(followerId, { mutableSetOf() }) += followeeId
    }

    fun unfollow(followerId: Int, followeeId: Int) {
        follows.getOrPut(followerId, { mutableSetOf() }) -= followeeId
    }

    private fun nextTimestamp(): Int = ++timestamp

}

class Leet : (String) -> String {
    override fun invoke(text: String): String {
        // * setup with your method
        // val work = GenericTestWork.create(Solution()::add)

        // * by method that has not return value
        // val work = GenericTestWork.createVoid(Solution.METHOD_WITHOUT_RETURN)
        // * by class of data struct
        val work = GenericTestWork.forStruct(Twitter::class)

        // * setup validator
        // work.validator = (T, T) -> Boolean
        work.compareSerial = true
        return work.run(text)
    }

}

fun main() {
    print(Leet()(Utils.fromStdin()))
}

