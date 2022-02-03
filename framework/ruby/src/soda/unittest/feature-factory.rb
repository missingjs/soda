require_relative 'feature'
require_relative 'unordered'

module Soda end

module Soda::Unittest

class ListFeature < ObjectFeature
  def initialize(elem_feat)
    @elem_feat = elem_feat
  end

  def get_hash(obj)
    res = 0
    # keep low 48 bits
    mask = 0xffffffffffff
    obj.each { |e|
      h = @elem_feat.get_hash(e)
      res = res * 133 + h
      res &= mask
    }
    res
  end

  def is_equal?(a, b)
    if a.size != b.size
      return false
    end
    (0...a.size).each { |i|
      unless @elem_feat.is_equal?(a[i], b[i])
        return false
      end
    }
  end
end

class FloatFeature < ObjectFeature
  def get_hash(val)
    val.hash
  end

  def is_equal?(a, b)
    (a-b).abs < 1e-6
  end
end

class FeatureFactory
  @factory_map = {}
  @factory_map.default = ->() { ObjectFeature.new }
  @factory_map['Float'] = ->() { FloatFeature.new }
  @factory_map['Float[]'] = ->() { ListFeature.new(self.create('Float')) }
  @factory_map['Float[][]'] = ->() { ListFeature.new(self.create('Float[]')) }

  def self.create(obj_type)
    @factory_map[obj_type].call
  end
end

end # module Soda::Unittest
