require 'algorithms'
require 'set'
require 'soda/ds/treemap'
require 'soda/leetcode/list'
require 'soda/leetcode/nest'
require 'soda/leetcode/tree'
# step [1]: implement solution function
include Soda::DS
include Soda::Leetcode

# @param {Integer[]} nums
# @return {Void} Do not return anything, modify nums in-place instead.
def move_zeroes(nums)
  p = 0
  (0...nums.size).each { |i|
    if nums[i] != 0
      if i != p
        nums[i], nums[p] = nums[p], nums[i]
      end
      p += 1
    end
  }
  while p < nums.size
    nums[p] = 0
    p += 1
  end
end

if __FILE__ == $0
  require 'soda/unittest/work'
  include Soda::Unittest
  # step [2]: setup function/return/arguments
  work = TestWork.create(method(:move_zeroes))
  # work = TestWork.for_struct(CLASS)
  # work.validator = -> (e, r) { e is equal to r }
  work.compare_serial = true
  puts work.run(ARGF.read)
end

