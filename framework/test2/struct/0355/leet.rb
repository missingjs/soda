require 'algorithms'
require 'set'
require 'soda/leetcode/list'
require 'soda/leetcode/nest'
# step [1]: implement solution function

class Tweet
  def initialize(id, ts)
    @id = id
    @ts = ts
  end
  attr_accessor :id, :ts
end

class Node
  def initialize(tweets)
    @tweets = tweets
    @index = tweets.size - 1
  end

  attr_accessor :index

  def current
    @tweets[@index]
  end

  def end?
    @index == -1
  end
end

class Twitter
  def initialize()
    @timestamp = 0
    @tweets = Hash.new { |hash,key| hash[key] = Array.new }
    @follows = Hash.new { |hash,key| hash[key] = Set.new }
    @limit = 10
  end

=begin
  :type user_id: Integer
  :type tweet_id: Integer
  :rtype: Void
=end
  def post_tweet(user_id, tweet_id)
    @tweets[user_id] << Tweet.new(tweet_id, next_timestamp)
  end

=begin
  :type user_id: Integer
  :rtype: Integer[]
=end
  def get_news_feed(user_id)
    s = @follows[user_id].to_a
    s << user_id
    pq = Containers::PriorityQueue.new
    s.each { |user|
      tq = @tweets[user]
      if tq.size > 0
        node = Node.new(tq)
        pq.push(node, node.current.ts)
      end
    }
    res = []
    i = 0
    while i < @limit && !pq.empty?
      node = pq.pop
      res << node.current.id
      node.index -= 1
      unless node.end?
        pq.push(node, node.current.ts)
      end
      i += 1
    end
    res
  end

=begin
  :type follower_id: Integer
  :type followee_id: Integer
  :rtype: Void
=end
  def follow(follower_id, followee_id)
    @follows[follower_id] << followee_id
  end

=begin
  :type follower_id: Integer
  :type followee_id: Integer
  :rtype: Void
=end
  def unfollow(follower_id, followee_id)
    @follows[follower_id].delete(followee_id)
  end

  def next_timestamp
    @timestamp += 1
    @timestamp
  end

end

if __FILE__ == $0
  require 'soda/unittest/work'
  ns = Soda::Unittest
  # step [2]: setup function/return/arguments
  # work = ns::TestWork.create(method(:my_solution))
  work = ns::TestWork.for_struct(Twitter)
  # work.validator = -> (e, r) { e is equal to r }
  work.compare_serial = true
  puts work.run(ARGF.read)
end

