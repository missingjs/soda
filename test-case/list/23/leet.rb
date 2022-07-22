require 'algorithms'
require 'set'
require 'soda/leetcode/list'
require 'soda/leetcode/nest'
require 'soda/leetcode/tree'
# step [1]: implement solution function
# @param {ListNode[]} lists
# @return {ListNode}
def merge_k_lists(lists)
  qu = Containers::PriorityQueue.new
  lists.each { |ls|
    if ls
      qu.push(ls, -ls.val)
    end
  }
  head = Soda::Leetcode::ListNode.new
  tail = head
  until qu.empty?
    curr = qu.pop
    next_ = curr.next
    if next_
      qu.push(next_, -next_.val)
    end
    tail.next = curr
    tail = curr
  end
  head.next
end

if __FILE__ == $0
  require 'soda/unittest/work'
  include Soda::Unittest
  # step [2]: setup function/return/arguments
  work = TestWork.create(method(:merge_k_lists))
  # work = TestWork.for_struct(CLASS)
  # work.validator = -> (e, r) { e is equal to r }
  work.compare_serial = true
  puts work.run(ARGF.read)
end

