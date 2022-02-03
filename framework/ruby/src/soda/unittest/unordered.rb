module Soda end

module Soda::Unittest

class XEntry
  def initialize(key, value)
    @key = key
    @value = value
  end

  attr_accessor :key, :value
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
      if @feat.is_equal?(key, entry.key)
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
      if @feat.is_equal?(key, entry.key)
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
    if a.size != b.size
      return false
    end
    xmap = XMap.new(@elem_feat)
    a.each { |e|
      xmap[e] = xmap.get(e, 0) + 1
    }
    b.each { |e|
      unless xmap.key?(e)
        return false
      end
      xmap[e] -= 1
      if xmap[e] == 0
        xmap.remove(e)
      end
    }
    true
  end
end

end # module Soda::Unittest
