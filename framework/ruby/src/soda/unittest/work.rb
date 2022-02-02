require 'json'
require 'time'

require 'soda/unittest/convert'
require 'soda/unittest/struct'
require 'soda/unittest/validate'
require 'soda/unittest/utils'

module Soda end

module Soda::Unittest

class WorkInput
    def initialize(_js)
        @json = _js
    end

    def expected
        @json['expected']
    end

    def id
        @json['id']
    end

    def args
        @json['args']
    end
end

class TestWork
  def self.create(function)
    TestWork.new(function, Utils.function_type_hints($0, function.name))
  end

  def self.for_struct(klass)
    hints_map = Utils.method_type_hints($0, klass)
    TestWork.new(StructTester.create(klass, hints_map), StructTester.method_hints)
  end

  def initialize(function, type_hints)
    @function = function
    @type_hints = type_hints
    @argument_types = type_hints[...-1]
    @return_type = type_hints[-1]
    @compare_serial = false
    @validator = nil
    @arguments = nil
  end

  attr_accessor :arguments, :compare_serial, :validator

  def run(text)
    input = WorkInput.new(JSON.parse(text))
    @arguments = args = Utils.parse_arguments(@argument_types, input.args)

    start_time = Time.now
    result = @function.call(*args)
    end_time = Time.now
    elapse_ms = (end_time - start_time) * 1000.0

    ret_type = @return_type
    if ret_type == 'Void' || ret_type == 'void'
      ret_type = @argument_types[0]
      result = args[0]
    end

    res_conv = ConverterFactory.create(ret_type)
    serial_result = res_conv.to_json_serializable(result)
    resp = {
      'id' => input.id,
      'result' => serial_result,
      'elapse' => elapse_ms
    }

    success = true
    if input.expected
      if @compare_serial && !@validator
        success = (input.expected == serial_result)
      else
        expect = res_conv.from_json_serializable(input.expected)
        if @validator != nil
          success = @validator.call(expect, result)
        else
          success = FeatureFactory.create(ret_type).is_equal?(expect, result)
        end
      end
    end
    resp['success'] = success

    resp.to_json
  end
end

end  # module Soda::Unittest
