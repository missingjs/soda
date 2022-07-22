require 'algorithms'
require 'set'
require 'soda/ds/treemap'
require 'soda/leetcode/list'
require 'soda/leetcode/nest'
require 'soda/leetcode/tree'
# step [1]: implement solution function

class Node
  def initialize(d, v)
    @diff = d
    @value = v
  end

  attr_accessor :diff, :value

  def self.with_target(value, target)
    Node.new((target - value).abs, value)
  end
end

# @param {TreeNode} root
# @param {Float} target
# @param {Integer} k
# @return {Integer[]}
def closest_k_values(root, target, k)
  queue = Containers::PriorityQueue.new
  solve(root, target, k, queue)
  res = []
  until queue.empty?
    res << queue.pop().value
  end
  res
end

def solve(root, target, k, queue)
  if !root
    return
  end

  node = Node.with_target(root.val, target)
  if queue.size == k
    if node.diff < queue.next.diff
      queue.pop()
      queue.push(node, node.diff)
    end
  else
    queue.push(node, node.diff)
  end

  solve(root.left, target, k, queue)
  solve(root.right, target, k, queue)
end

if __FILE__ == $0
  require 'soda/unittest/work'
  include Soda::Unittest
  # step [2]: setup function/return/arguments
  work = TestWork.create(method(:closest_k_values))
  # work = TestWork.for_struct(CLASS)
  work.validator = Validators.for_array('Integer', false)
  work.compare_serial = true
  puts work.run(ARGF.read)
end

