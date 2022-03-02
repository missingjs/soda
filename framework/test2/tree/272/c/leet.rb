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
  nodes = []
  collect(root, nodes, target)
  quick_select(nodes, 0, nodes.size-1, k)
  nodes[0,k].map { |n| n.value }
end

def quick_select(nodes, start, _end, index)
  while start < _end
    mid = ((start + _end) / 2).to_i
    place_median3(nodes, start, mid, _end)
    k = partition(nodes, start, _end, mid)
    if k == index
      return
    elsif k > index
      _end = k - 1
    else
      start = k + 1
    end
  end
end

def partition(nodes, start, _end, pivot)
  d = nodes[pivot].diff
  swap(nodes, pivot, _end)
  p = start
  (start.._end).each { |i|
    if nodes[i].diff < d
      if p != i
        swap(nodes, p, i)
      end
      p += 1
    end
  }
  swap(nodes, p, _end)
  p
end

def place_median3(nodes, start, mid, _end)
  if nodes[start].diff > nodes[mid].diff
    swap(nodes, start, mid)
  end
  if nodes[start].diff > nodes[_end].diff
    swap(nodes, start, _end)
  end
  if nodes[mid].diff > nodes[_end].diff
    swap(nodes, mid, _end)
  end
end

def swap(arr, i, j)
  arr[i], arr[j] = arr[j], arr[i]
end

def collect(root, nodes, target)
  return unless root
  nodes << Node.with_target(root.val, target)
  collect(root.left, nodes, target)
  collect(root.right, nodes, target)
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

