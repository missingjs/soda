module Soda end

module Soda::Unittest

class ObjectFeature
  def getHash(obj)
    obj.hash
  end

  def isEqual?(a, b)
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
    @feat.getHash(key)
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
    if !@dict.key?(h)
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
    if !@dict.key?(h)
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
  def self.unorderedList(feat)
    ->(a, b) {
      if a.size != b.size
        return false
      end
      xmap = XMap.new(feat)
      for e in a
        xmap[e] = xmap.get(e, 0) + 1
      end
      for e in b
        if !xmap.contains?(e)
          return false
        end
        xmap[e] -= 1
        if xmap[e] == 0
          xmap.remove(e)
        end
      end
      true
    }
  end

  def self.list(feat)
    ->(a, b) {
      if a.size != b.size
        return false
      end
      for i in a.index
        if !feat.isEqual?(a[i], b[i])
          return false
        end
      end
    }
  end
end

class ListFeature < ObjectFeature
  def initialize(elemFeat)
    @elemFeat = elemFeat
  end

  def getHash(obj)
    res = 0
    # keep low 48 bits
    mask = 0xffffffffffff
    for e in obj
      h = @elemFeat.getHash(e)
      res = res * 133 + h
      res &= mask
    end
    res
  end

  def isEqual?(a, b)
    StrategyFactory.list(@elemFeat).call(a, b)
  end
end

class UnorderedListFeature
  def initialize(elemFeat)
    @elemFeat = elemFeat
  end

  def getHash(obj)
    res = 0
    # keep low 48 bits
    mask = 0xffffffffffff
    hashArr = obj.map { |e| @elemFeat.getHash(e) }
    for h in hashArr.sort
      res = res * 133 + h
      res &= mask
    end
    res
  end
  
  def isEqual?(a, b)
    StrategyFactory.unorderedList(@elemFeat).call(a, b)
  end
end

class FloatFeature < ObjectFeature
  def getHash(val)
    val.hash
  end

  def isEqual?(a, b)
    (a-b).abs < 1e-6
  end
end

class Validators
  def self.forArray(objType, ordered)
    f = StrategyFactory
    d = FeatureFactory.create(objType)
    ordered ? f.list(d) : f.unorderedList(d)
  end

  def self.forArray2d(objType, dim1Ordered, dim2Ordered)
    f = StrategyFactory
    elemFeat = FeatureFactory.create(objType)
    d = dim2Ordered ? ListFeature.new(elemFeat) : UnorderedListFeature.new(elemFeat)
    dim1Ordered ? f.list(d) : f.unorderedList(d)
  end
end

class FeatureFactory
  @@factoryMap = {}
  @@factoryMap.default = ->() { ObjectFeature.new }
  @@factoryMap['Float'] = ->() { FloatFeature.new }

  def self.create(objType)
    @@factoryMap[objType].call()
  end
end

class ValidatorFactory
  @@factoryMap = {}
  @@factoryMap.default = ->(objType) {
    ->(x, y) {FeatureFactory.create(objType).isEqual?(x, y)}
  }
  @@factoryMap['Float[]'] = ->(t) {
    Validators.forArray(t, true)
  }
  @@factoryMap['Float[][]'] = ->(t) {
    Validators.forArray2d(t, true, true)
  }

  def self.create(objType)
    @@factoryMap[objType].call(objType)
  end
end

end  # module Soda::Unittest
