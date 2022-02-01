module Soda end

module Soda::Unittest

class ObjectConverter
  def initialize(parser, serializer)
    @parser = parser
    @serializer = serializer
  end

  def from_json_serializable(j)
    @parser.call(j)
  end

  def to_json_serializable(obj)
    @serializer.call(obj)
  end
end

class ConverterFactory
  @factory_map = {}
  @factory_map.default = -> () {
    ObjectConverter.new(->(j){j}, ->(o){o})
  }

  def self.create(obj_type)
    @factory_map[obj_type].call
  end
end

end  # module Soda::Unittest

