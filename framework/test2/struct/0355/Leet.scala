import soda.scala.unittest._

import scala.reflect.runtime.universe.typeOf

import soda.scala.leetcode._

import collection.mutable
import collection.mutable.{ArrayBuffer, ArrayDeque, PriorityQueue}
import util.control.Breaks.{break, breakable}

class Twitter() {

  case class Tweet(id: Int, ts: Int)

  class Node(tweetList: List[Tweet]) {
    var index = tweetList.size - 1
    def current = tweetList(index)
    def isEnd = index == -1
  }

  var timestamp = 0
  val tweets = mutable.Map[Int, ArrayDeque[Tweet]]()
  val follows = mutable.Map[Int, mutable.Set[Integer]]()
  val Limit = 10

  def postTweet(userId: Int, tweetId: Int): Unit = {
    val qu = tweets.getOrElseUpdate(userId, ArrayDeque[Tweet]())
    qu.append(Tweet(tweetId, nextTimestamp))
    if (qu.size > Limit) {
      qu.removeHead()
    }
  }

  def getNewsFeed(userId: Int): List[Int] = {
    val s = ArrayBuffer(follows.getOrElse(userId, mutable.Set.empty).toList: _*)
    s += userId
    val pq = PriorityQueue[Node]()(Ordering.by(n => n.current.ts))
    for (user <- s) {
      tweets.get(user) match {
        case Some(tq) => {
          if (tq.nonEmpty) {
            pq.enqueue(new Node(tq.toList))
          }
        }
        case None =>
      }
    }
    val res = ArrayBuffer[Int]()
    var i = 0
    while (i < Limit && pq.nonEmpty) {
      val node = pq.dequeue()
      res += node.current.id
      node.index -= 1
      if (!node.isEnd) {
        pq.enqueue(node)
      }
      i += 1
    }
    res.toList
  }

  def follow(followerId: Int, followeeId: Int): Unit = {
    follows.getOrElseUpdate(followerId, mutable.Set()) += followeeId
  }

  def unfollow(followerId: Int, followeeId: Int): Unit = {
    follows.getOrElseUpdate(followerId, mutable.Set()) -= followeeId
  }

  private def nextTimestamp: Int = { timestamp += 1; timestamp }

}

class Leet {
  def get(): () => Unit = {
    () => {
      // val work = GenericTestWork.create1(Solution.eq)
      val work = GenericTestWork.forStruct(typeOf[Twitter])
      // val work = TestWork.forObject(typeOf[Solution], "METHOD")
      // val work = TestWork.forInstance(typeOf[Solution], "METHOD")
      // val work = TestWork.createN((...)=>R)
      // val work = TestWork.forStruct(typeOf[STRUCT])
      // work.setValidator((R, R) => Boolean)
      work.compareSerial = true
      work.run()
    }
  }
}

object Leet extends App {
  new Leet().get().apply()
}
