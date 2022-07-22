require 'algorithms'
require 'set'
require 'soda/leetcode/list'
require 'soda/leetcode/nest'
require 'soda/leetcode/tree'
# step [1]: implement solution function

include Soda::Leetcode

class CBTInserter

=begin
    :type root: TreeNode
=end
    def initialize(root)
      @root = root
      @qu = []
      return unless root
      @qu << root
      until @qu.empty?
        node = @qu[0]
        unless node.left
          break
        end
        @qu << node.left
        unless node.right
          break
        end
        @qu << node.right
        @qu.shift
      end
    end

=begin
    :type val: Integer
    :rtype: Integer
=end
    def insert(val)
      node = TreeNode.new(val)
      head = @qu[0]
      @qu << node
      if head.left
        head.right = node
        @qu.shift
      else
        head.left = node
      end
      head.val
    end

=begin
    :rtype: TreeNode
=end
    def get_root()
      @root
    end

end

if __FILE__ == $0
  require 'soda/unittest/work'
  ns = Soda::Unittest
  # step [2]: setup function/return/arguments
  # work = ns::TestWork.create(method(:my_solution))
  work = ns::TestWork.for_struct(CBTInserter)
  # work.validator = -> (e, r) { e is equal to r }
  work.compare_serial = true
  puts work.run(ARGF.read)
end

