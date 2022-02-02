module Soda end

module Soda::Leetcode
  
class NestedInteger
  def initialize(value = nil)
    @is_atomic = (value != nil)
    @elements = []
    @value = value
  end

  def is_integer
    """
    Return true if this NestedInteger holds a single integer, rather than a nested list.
    @return {Boolean}
    """
    @is_atomic
  end

  def get_integer
    """
    Return the single integer that this NestedInteger holds, if it holds a single integer
    Return nil if this NestedInteger holds a nested list
    @return {Integer}
    """
    @value
  end

  def set_integer(value)
    """
    Set this NestedInteger to hold a single integer equal to value.
    @return {Void}
    """
    if is_integer
      @value = value
    end
  end

  def add(elem)
    """
    Set this NestedInteger to hold a nested list and adds a nested integer elem to it.
    @return {Void}
    """
    unless is_integer
      @elements << elem
    end
  end

  def get_list
    """
    Return the nested list that this NestedInteger holds, if it holds a nested list
    Return nil if this NestedInteger holds a single integer
    @return {NestedInteger[]}
    """
    is_integer ? [] : @elements.to_a
  end

end

class NestedIntegerFactory
  def self.parse(d)
    if d.is_a?(Integer)
      return NestedInteger.new(d)
    end
    ni = NestedInteger.new
    d.each { |sub|
      ni.add(self.parse(sub))
    }
    ni
  end

  def self.serialize(ni)
    ni.is_integer ? ni.get_integer : ni.get_list.map { |e| self.serialize(e) }
  end

  def self.parse_multi(ds)
    ds.map { |e| self.parse(e) }
  end

  def self.serialize_multi(nested_list)
    nested_list.map { |e| self.serialize(e) }
  end
end

end  # module Soda::Leetcode
