require 'set'
require 'soda/leetcode/list'
# step [1]: implement solution function
# @param {ListNode} head
# @return {ListNode}
def reverse(head)
  h = nil
  while head
    _next = head.next
    head.next = h
    h = head
    head = _next
  end
  h
end

if __FILE__ == $0
  require 'soda/unittest/work'
  ns = Soda::Unittest
  # step [2]: setup function/return/arguments
  work = ns::TestWork.create(method(:reverse))
  # work = ns::TestWork.forStruct(CLASS)
  # work.validator = -> (e, r) { e is equal to r }
  work.compare_serial = true
  puts work.run(ARGF.read)
end

