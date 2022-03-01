require 'algorithms'

module Soda end

module Soda::DS

class TreeMap < Containers::RubyRBTreeMap
  def lower_bound_key(key)
    node = self.lower_bound_node(key)
    node ? node.key : nil 
  end 

  def lower_bound_node(key)
    ptr = @root
    last_ge = nil 
    while ptr 
      cmp = key <=> ptr.key
      if cmp == 0
        last_ge = ptr 
        break
      elsif cmp == -1
        last_ge = ptr
        ptr = ptr.left
      else
        ptr = ptr.right
      end
    end
    last_ge
  end
end

end  # module Soda::DS
