require 'algorithms'
require 'set'
require 'soda/leetcode/list'
require 'soda/leetcode/nest'
require 'soda/leetcode/tree'
# step [1]: implement solution function
# @param {TreeNode} root
# @return {Integer[][]}
def find_leaves(root)
  res = Array.new(100) { Array.new }
  r = solve2(root, res)
  res.slice(0, r)
end

def solve2(root, res)
  return 0 unless root
  _R = solve2(root.right, res)
  _L = solve2(root.left, res)
  index = [_L, _R].max
  res[index] << root.val
  index + 1
end

if __FILE__ == $0
  require 'soda/unittest/work'
  ns = Soda::Unittest
  # step [2]: setup function/return/arguments
  work = ns::TestWork.create(method(:find_leaves))
  # work = ns::TestWork.for_struct(CLASS)
  work.validator = ns::Validators.for_array2d('Integer', true, false)
  work.compare_serial = true
  puts work.run(ARGF.read)
end

