module Soda end

module Soda::Unittest

class ObjectConverter
  def initialize(parser, serializer)
    @parser = parser
    @serializer = serializer
  end

  def fromJsonSerializable(j)
    @parser.call(j)
  end

  def toJsonSerializable(obj)
    @serializer.call(obj)
  end
end

class ConverterFactory
  @@factoryMap = {}
  @@factoryMap.default = -> () {
    ObjectConverter.new(->(j){j}, ->(o){o})
  }

  def self.create(obj_type)
      @@factoryMap[obj_type].call()
  end
end

end  # module Soda::Unittest

