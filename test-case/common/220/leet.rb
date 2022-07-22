require 'algorithms'
require 'set'
require 'soda/ds/treemap'
require 'soda/leetcode/list'
require 'soda/leetcode/nest'
require 'soda/leetcode/tree'
# step [1]: implement solution function

# @param {Integer[]} nums
# @param {Integer} k
# @param {Integer} t
# @return {Boolean}
def contains_nearby_almost_duplicate(nums, k, t)
  tmap = Soda::DS::TreeMap.new
  i = j = 0
  while j < nums.size
    if j - i <= k
      val = nums[j]
      j += 1
      lower = val - t
      upper = val + t
      lb_key = tmap.lower_bound_key(lower)
      if lb_key != nil && lb_key <= upper
        return true
      end
      tmap[val] = 0 unless tmap.has_key?(val)
      tmap[val] += 1
    else
      val = nums[i]
      i += 1
      c = tmap[val] - 1
      if c == 0
        tmap.delete(val)
      else
        tmap[val] = c
      end
    end
  end
  false
end

if __FILE__ == $0
  require 'soda/unittest/work'
  include Soda::Unittest
  # step [2]: setup function/return/arguments
  work = TestWork.create(method(:contains_nearby_almost_duplicate))
  # work = TestWork.for_struct(CLASS)
  # work.validator = -> (e, r) { e is equal to r }
  work.compare_serial = true
  puts work.run(ARGF.read)
end

