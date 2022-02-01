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

class XEntry
  def initialize(key, value)
    @key = key
    @value = value
  end
end

class XMap
  def initialize(feature)
    @feat = feature
    @dict = Hash.new{ |hash, key| hash[key] = Array.new }
    @size = 0
  end

  def _hash(key)
    @feat.get_hash(key)
  end

  def key?(key)
    _entry(key)
  end

  def get(key, default)
    e = _entry(key)
    e ? e.value : default
  end

  def [](key)
    get(key, nil)
  end

  def []=(key, value)
    e = _entry(key)
    if e
      e.value = value
    else
      h = _hash(key)
      @dict[h] << XEntry.new(key, value)
      @size += 1
    end
  end

  def _entry(key)
    h = _hash(key)
    unless @dict.key?(h)
      return
    end
    @dict[h].each { |entry|
      if @feat.isEqual(key, entry.key)
        return entry
      end
    }
  end

  def remove(key)
    h = _hash(key)
    unless @dict.key?(h)
      return
    end
    e = nil
    @dict[h].each { |entry|
      if @feat.isEqual(key, entry.key)
        e = entry
        break
      end
    }
    if e
      @dict[h].delete(e)
      @size -= 1
    end
  end
end

class StrategyFactory
  def self.unordered_list(feat)
    ->(a, b) {
      if a.size != b.size
        return false
      end
      xmap = XMap.new(feat)
      a.each { |e|
        xmap[e] = xmap.get(e, 0) + 1
      }
      b.each { |e|
        unless xmap.contains?(e)
          return false
        end
        xmap[e] -= 1
        if xmap[e] == 0
          xmap.remove(e)
        end
      }
      true
    }
  end

  def self.list(feat)
    ->(a, b) {
      if a.size != b.size
        return false
      end
      # 0.step(a.size-1) {}
      a.each_with_index { |_, i|
        unless feat.is_equal?(a[i], b[i])
          return false
        end
      }
    }
  end
end

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
    StrategyFactory.list(@elem_feat).call(a, b)
  end
end

class UnorderedListFeature
  def initialize(elem_feat)
    @elem_feat = elem_feat
  end

  def get_hash(obj)
    res = 0
    # keep low 48 bits
    mask = 0xffffffffffff
    hash_arr = obj.map { |e| @elem_feat.get_hash(e) }
    hash_arr.sort.each { |h|
      res = res * 133 + h
      res &= mask
    }
    res
  end
  
  def is_equal?(a, b)
    StrategyFactory.unordered_list(@elem_feat).call(a, b)
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

class Validators
  def self.for_array(obj_type, ordered)
    f = StrategyFactory
    d = FeatureFactory.create(obj_type)
    ordered ? f.list(d) : f.unordered_list(d)
  end

  def self.for_array2d(obj_type, dim1_ordered, dim2_ordered)
    f = StrategyFactory
    elem_feat = FeatureFactory.create(obj_type)
    d = dim2_ordered ? ListFeature.new(elem_feat) : UnorderedListFeature.new(elem_feat)
    dim1_ordered ? f.list(d) : f.unordered_list(d)
  end
end

class FeatureFactory
  @factory_map = {}
  @factory_map.default = ->() { ObjectFeature.new }
  @factory_map['Float'] = ->() { FloatFeature.new }

  def self.create(obj_type)
    @factory_map[obj_type].call
  end
end

class ValidatorFactory
  @factory_map = {}
  @factory_map.default = ->(objType) {
    ->(x, y) {FeatureFactory.create(objType).is_equal?(x, y)}
  }
  @factory_map['Float[]'] = ->(t) {
    Validators.for_array('Float', true)
  }
  @factory_map['Float[][]'] = ->(t) {
    Validators.for_array2d('Float', true, true)
  }

  def self.create(obj_type)
    @factory_map[obj_type].call(obj_type)
  end
end

end  # module Soda::Unittest
