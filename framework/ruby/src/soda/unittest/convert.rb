require_relative '../leetcode/list'
require_relative '../leetcode/nest'
require_relative '../leetcode/tree'

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
  def self._register(type, parser, serializer)
    @factory_map[type.to_s] = ->() {
      ObjectConverter.new(parser, serializer)
    }
  end

  def self._init
    lc = Soda::Leetcode
    _register('ListNode', lc::ListFactory.method(:create), lc::ListFactory.method(:dump))
    _register(
      'ListNode[]',
      ->(ls) { ls.map { |e| lc::ListFactory.create(e) } },
      ->(ts) { ts.map { |e| lc::ListFactory.dump(e) } }
    )
    _register(
      'NestedInteger',
      lc::NestedIntegerFactory.method(:parse),
      lc::NestedIntegerFactory.method(:serialize)
    )
    _register(
      'NestedInteger[]',
      lc::NestedIntegerFactory.method(:parse_multi),
      lc::NestedIntegerFactory.method(:serialize_multi)
    )
    _register('TreeNode', lc::TreeFactory.method(:create), lc::TreeFactory.method(:dump))
  end

  def self.create(obj_type)
    @factory_map[obj_type].call
  end

  @factory_map = Hash.new(-> () {
    ObjectConverter.new(->(j){j}, ->(o){o})
  })
  _init
end

end  # module Soda::Unittest

