require 'algorithms'
require 'set'
require 'soda/leetcode/list'
require 'soda/leetcode/nest'
require 'soda/leetcode/tree'
# step [1]: implement solution function
# @param {TreeNode} root
# @param {TreeNode} p
# @param {TreeNode} q
# @return {TreeNode}
def lowest_common_ancestor(root, p, q)
  stk = [root]
  last = root
  found_one = false
  index = -1

  if root == p || root == q
    found_one = true
    index = 0
  end

  while stk.size > 0
    node = stk[-1]
    if node.left && last != node.left && last != node.right
      if node.left == p || node.left == q
        if !found_one
          index = stk.size
          found_one = true
        else
          return stk[index]
        end
      end
      stk << node.left
    elsif node.right && last != node.right
      if node.right == p || node.right == q
        if !found_one
          index = stk.size
          found_one = true
        else
          return stk[index]
        end
      end
      stk << node.right
    else
      last = node
      if index == stk.size - 1
        index -= 1
      end
      stk.pop
    end
  end
  nil
end

# @param {TreeNode} root
# @param {Integer} p
# @param {Integer} q
# @return {Integer}
def driver(root, p, q)
  p_node = find_node(root, p)
  q_node = find_node(root, q)
  lowest_common_ancestor(root, p_node, q_node).val
end

def find_node(root, val)
  return nil unless root
  return root if root.val == val
  left = find_node(root.left, val)
  left ? left : find_node(root.right, val)
end

if __FILE__ == $0
  require 'soda/unittest/work'
  include Soda::Unittest
  # step [2]: setup function/return/arguments
  work = TestWork.create(method(:driver))
  # work = TestWork.for_struct(CLASS)
  # work.validator = -> (e, r) { e is equal to r }
  work.compare_serial = true
  puts work.run(ARGF.read)
end

