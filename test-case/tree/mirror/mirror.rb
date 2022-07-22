require 'algorithms'
require 'set'
require 'soda/leetcode/list'
require 'soda/leetcode/nest'
# step [1]: implement solution function
# @param {TreeNode} root
# @return {TreeNode}
def mirror(root)
  return nil unless root
  mirror(root.left)
  mirror(root.right)
  root.left, root.right = root.right, root.left
  root
end

if __FILE__ == $0
  require 'soda/unittest/work'
  ns = Soda::Unittest
  # step [2]: setup function/return/arguments
  work = ns::TestWork.create(method(:mirror))
  # work = ns::TestWork.for_struct(CLASS)
  # work.validator = -> (e, r) { e is equal to r }
  work.compare_serial = true
  puts work.run(ARGF.read)
end

