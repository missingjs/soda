require 'set'
require 'soda/leetcode/list'
require 'soda/leetcode/nest'
# step [1]: implement solution function:w

class SummaryRanges
  def initialize()
    @parent = Array.new(10003, 0)
    @ancestor_set = Set.new
  end

=begin
  :type val: Integer
  :rtype: Void
=end
  def add_num(val)
    val += 1
    unless @parent[val] == 0
      return
    end
    @parent[val] = -1
    @ancestor_set << val
    left = val - 1
    right = val + 1
    if left > 0 && @parent[left] != 0
      merge(left, val)
    end
    unless @parent[right] == 0
      merge(val, right)
    end
    nil
  end

=begin
  :rtype: Integer[][]
=end
  def get_intervals()
    ans = @ancestor_set.to_a.sort
    res = Array.new(ans.size, nil)
    (0...res.size).each { |i|
      start = ans[i]
      _end = start - @parent[start] - 1
      res[i] = [start-1, _end-1]
    }
    res
  end

  def merge(x, y)
    ax = get_ancestor(x)
    ay = get_ancestor(y)
    if ax < ay
      merge_ancestor(ax, ay)
    else
      merge_ancestor(ay, ax)
    end
  end

  def merge_ancestor(ax, ay)
    @parent[ax] += @parent[ay]
    @parent[ay] = ax
    @ancestor_set.delete(ay)
  end

  def get_ancestor(x)
    @parent[x] < 0 ? x : @parent[x] = get_ancestor(@parent[x])
  end

end

if __FILE__ == $0
  require 'soda/unittest/work'
  ns = Soda::Unittest
  # step [2]: setup function/return/arguments
  # work = ns::TestWork.create(method(:my_solution))
  work = ns::TestWork.for_struct(SummaryRanges)
  # work.validator = -> (e, r) { e is equal to r }
  work.compare_serial = true
  puts work.run(ARGF.read)
end

