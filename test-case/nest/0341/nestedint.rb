require 'set'
require 'soda/leetcode/list'
require 'soda/leetcode/nest'
# step [1]: implement solution function
# @param {NestedInteger[]} n
# @return {Integer[]}
def flat_nested(ni_list)
  res = []
  iter = NestedIterator.new(ni_list)
  while iter.has_next
    res << iter.next
  end
  res
end

class Node
  attr_accessor :index

  def initialize(ni_list)
    @ni_list = ni_list
    @index = 0
  end

  def end?
    @index >= @ni_list.size
  end

  def value
    current.get_integer
  end

  def current
    @ni_list[@index]
  end
end

class NestedIterator
  def initialize(nested_list)
    @stk = [Node.new(nested_list)]
    locate
  end

  def locate
    until @stk.empty?
      if @stk[-1].end?
        @stk.pop
        unless @stk.empty?
          @stk[-1].index += 1
        end
      elsif @stk[-1].current.is_integer
        break
      else
        @stk << Node.new(@stk[-1].current.get_list)
      end
    end
  end

  def next
    value = @stk[-1].value
    @stk[-1].index += 1
    locate
    value
  end

  def has_next
    !@stk.empty? && !@stk[-1].end?
  end
end

if __FILE__ == $0
  require 'soda/unittest/work'
  ns = Soda::Unittest
  # step [2]: setup function/return/arguments
  work = ns::TestWork.create(method(:flat_nested))
  # work = ns::TestWork.forStruct(CLASS)
  # work.validator = -> (e, r) { e is equal to r }
  work.compare_serial = true
  puts work.run(ARGF.read)
end

