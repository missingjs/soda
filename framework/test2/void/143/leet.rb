require 'algorithms'
require 'set'
require 'soda/ds/treemap'
require 'soda/leetcode/list'
require 'soda/leetcode/nest'
require 'soda/leetcode/tree'
# step [1]: implement solution function
include Soda::DS
include Soda::Leetcode

# @param {ListNode} head
# @return {Void} Do not return anything, modify head in-place instead.
def reorder_list(head)
  fast = slow = head
  while fast.next && fast.next.next
    slow = slow.next
    fast = fast.next.next
  end

  return if slow == fast

  r = reverse(slow.next)
  slow.next = nil
  merge(head, r)
end

def reverse(head)
  q = nil
  while head
    _next = head.next
    head.next = q
    q = head
    head = _next
  end
  q
end

def merge(n1, n2)
  t = ListNode.new
  while n1 && n2
    t.next = n1
    t = n1
    n1 = n1.next
    t.next = n2
    t = n2
    n2 = n2.next
  end
  t.next = n1 || n2
end

if __FILE__ == $0
  require 'soda/unittest/work'
  include Soda::Unittest
  # step [2]: setup function/return/arguments
  work = TestWork.create(method(:reorder_list))
  # work = TestWork.for_struct(CLASS)
  # work.validator = -> (e, r) { e is equal to r }
  work.compare_serial = true
  puts work.run(ARGF.read)
end

