module Soda end

module Soda::Leetcode

class ListNode
  attr_accessor :val, :next
  def initialize(val = 0, _next = nil)
    @val = val
    @next = _next
  end
end

class ListFactory
  def self.create(data)
    head = ListNode.new
    tail = head
    data.each { |val|
      node = ListNode.new(val)
      tail.next = node
      tail = node
    }
    head.next
  end

  def self.dump(head)
    list = []
    while head
      list << head.val
      head = head.next
    end
    list
  end
end

end  # module Soda::Leetcode
