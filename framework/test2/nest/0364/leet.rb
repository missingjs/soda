require 'set'
require 'soda/leetcode/list'
require 'soda/leetcode/nest'
# step [1]: implement solution function
# @param {NestedInteger[]} nested_list
# @return {Integer}
def depth_sum_inverse(nested_list)
  info = get_info(nested_list, 1)
  (info.max_depth + 1) * info.sum - info.product
end

class Info
  def initialize(s, p, m)
    @sum = s
    @product = p
    @max_depth = m
  end

  attr_accessor :sum, :product, :max_depth
end

def get_info(nested_list, depth)
  sum = 0
  product = 0
  max_depth = depth
  nested_list.each { |ni|
    if ni.is_integer
      val = ni.get_integer
      sum += val
      product += val * depth
      max_depth = [max_depth, depth].max
    else
      res = get_info(ni.get_list, depth + 1)
      sum += res.sum
      product += res.product
      max_depth = [max_depth, res.max_depth].max
    end
  }
  Info.new(sum, product, max_depth)
end

if __FILE__ == $0
  require 'soda/unittest/work'
  ns = Soda::Unittest
  # step [2]: setup function/return/arguments
  work = ns::TestWork.create(method(:depth_sum_inverse))
  # work = ns::TestWork.forStruct(CLASS)
  # work.validator = -> (e, r) { e is equal to r }
  work.compare_serial = true
  puts work.run(ARGF.read)
end

