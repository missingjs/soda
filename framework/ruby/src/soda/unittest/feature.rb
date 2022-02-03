module Soda end

module Soda::Unittest

class ObjectFeature
  def get_hash(obj)
    obj.hash
  end

  def is_equal?(a, b)
    a == b
  end
end

end # module Soda::Unittest