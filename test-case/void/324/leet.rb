require 'algorithms'
require 'set'
require 'soda/ds/treemap'
require 'soda/leetcode/list'
require 'soda/leetcode/nest'
require 'soda/leetcode/tree'
# step [1]: implement solution function
include Soda::DS
include Soda::Leetcode

class VirIndex
  def initialize(nums)
    @nums = nums
  end

  def [](index)
    @nums[self.map_index(index)]
  end

  def []=(index, value)
    @nums[self.map_index(index)] = value
  end

  def map_index(i)
    n = @nums.size
    if (n&1) == 1 || i > ((n-1) >> 1)
      (((n-i) << 1) - 1) % n
    else
      n - 2 - (i << 1)
    end
  end
end

# @param {Integer[]} nums
# @return {Void} Do not return anything, modify nums in-place instead.
def wiggle_sort(nums)
  vi = VirIndex.new(nums)
  quick_select(vi, 0, nums.size-1, (nums.size-1)>>1)
end

def quick_select(vi, start, _end, k)
  while start < _end
    p0, p1 = partition(vi, start, _end)
    return if k >= p0 && k <= p1
    if k > p1
      start = p1 + 1
    else
      _end = p0 - 1
    end
  end
end

def partition(vi, start, _end)
  mid = (start + _end) >> 1
  pivot = get_median(vi[start], vi[mid], vi[_end])
  p, z, q = start, _end + 1, start
  while q < z
    if vi[q] < pivot
      vi[p], vi[q] = vi[q], vi[p]
      p += 1
      q += 1
    elsif vi[q] == pivot
      q += 1
    else
      z -= 1
      vi[z], vi[q] = vi[q], vi[z]
    end
  end
  [p, z-1]
end

def get_median(a, b, c)
  if a >= b
    b >= c ? b : [a, c].min
  else
    a >= c ? a : [b, c].min
  end
end

if __FILE__ == $0
  require 'soda/unittest/work'
  include Soda::Unittest
  # step [2]: setup function/return/arguments
  work = TestWork.create(method(:wiggle_sort))
  # work = TestWork.for_struct(CLASS)
  work.validator = -> (e, nums) {
    (1...nums.size).each { |i|
      if i % 2 != 0 && nums[i] <= nums[i-1] || i % 2 == 0 && nums[i] >= nums[i-1]
        return false
      end
    }
    true
  }
  work.compare_serial = true
  puts work.run(ARGF.read)
end

