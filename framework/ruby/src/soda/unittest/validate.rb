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

class FeatureFactory
  @@factoryMap = {}
  @@factoryMap.default = ->() {ObjectFeature.new}

  def self.create(objType)
    @@factoryMap[objType].call()
  end
end

class ValidatorFactory
  @@factoryMap = {}
  @@factoryMap.default = ->(objType) {
    ->(x, y) {FeatureFactory.create(objType).isEqual?(x, y)}
  }

  def self.create(objType)
    @@factoryMap[objType].call(objType)
  end
end

end  # module Soda::Unittest
