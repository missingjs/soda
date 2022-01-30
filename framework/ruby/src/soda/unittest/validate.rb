module Soda end

module Soda::Unittest

class FeatureFactory
  @@factoryMap = {}
  @@factoryMap.default = -> () {
    ObjectConverter.new(->(j){j}, ->(o){o})
  }
  def self.create(objType)
  end
end

end  # module Soda::Unittest
