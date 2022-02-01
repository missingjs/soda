require 'set'
require 'soda/leetcode/list'
# step [1]: implement solution function
# @param {ListNode[]} lists
# @return {ListNode[]}
def reverse_all(lists)
  (0...lists.size).each { |i|
    lists[i] = reverse(lists[i])
  }
  i, j = 0, lists.size - 1
  while i < j
    lists[i], lists[j] = lists[j], lists[i]
    i += 1
    j -= 1
  end
  lists
end

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
  work = ns::TestWork.create(method(:reverse_all))
  # work = ns::TestWork.forStruct(CLASS)
  # work.validator = -> (e, r) { e is equal to r }
  work.compare_serial = true
  puts work.run(ARGF.read)
end

