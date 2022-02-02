module Soda end

module Soda::Leetcode
  
class TreeNode
  attr_accessor :val, :left, :right
  def initialize(val = 0, left = nil, right = nil)
    @val = val
    @left = left
    @right = right
  end
end

class TreeFactory
  def self.create(data)
    return nil if data.empty?

    root = TreeNode.new(data[0])
    qu = [root]
    index = 1
    while index < data.size
      node = qu.shift
      if data[index]
        node.left = TreeNode.new(data[index])
        qu.push(node.left)
      end
      index += 1
      if index == data.size
        break
      end
      if data[index]
        node.right = TreeNode.new(data[index])
        qu.push(node.right)
      end
      index += 1
    end
    root
  end

  def self.dump(root)
    return [] unless root
    res = []
    qu = [root]
    until qu.empty?
      node = qu.shift
      if node
        res << node.val
        qu << node.left
        qu << node.right
      else
        res << nil
      end
    end
    until res[-1]
      res.pop
    end
    res
  end
end

end
