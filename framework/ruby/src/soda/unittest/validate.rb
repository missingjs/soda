require_relative 'feature-factory'

module Soda end

module Soda::Unittest

class Validators
  def self.for_array(obj_type, ordered)
    _build_for_array(ordered, FeatureFactory.create(obj_type))
  end

  def self.for_array2d(obj_type, dim1_ordered, dim2_ordered)
    elem_feat = FeatureFactory.create(obj_type)
    d = dim2_ordered ? ListFeature.new(elem_feat) : UnorderedListFeature.new(elem_feat)
    _build_for_array(dim1_ordered, d)
  end

  def self._build_for_array(ordered, elem_feat)
    ordered ? ->(a,b) { ListFeature.new(elem_feat).is_equal?(a,b) }
      : ->(a,b) { UnorderedListFeature.new(elem_feat).is_equal?(a,b) }
  end
end

end  # module Soda::Unittest
