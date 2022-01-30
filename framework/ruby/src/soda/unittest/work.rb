require 'json'
require 'time'

require 'soda/unittest/convert'
require 'soda/unittest/validate'

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
    TestWork.new(function, parseTypeHints(function.name))
  end

  def self.parseTypeHints(funcName)
    lines = []
    File.readlines($0).each { |line|
      if line =~ /^def #{funcName}/
        break
      end
      if line.strip.length > 0
        lines.append(line)
      end
    }
    retType = nil
    argTypes = []
    while lines.length > 0
      text = lines.pop
      if /^#\s+@param\s+\{(?<argType>.*)\}/ =~ text
        argTypes.append(argType)
      elsif /^#\s+@return\s+\{(?<returnType>.*)\}/ =~ text
        retType = returnType
      else
        break
      end
    end
    argTypes.append(retType)
  end

  def self.forStruct
    # TODO
  end

  def initialize(function, typeHints)
    @function = function
    @typeHints = typeHints
    @argumentTypes = typeHints[...-1]
    @returnType = typeHints[-1]
    @compareSerial = false
    @validator = nil
    @arguments = nil
  end

  attr_accessor :compareSerial, :validator

  def run(text)
    input = WorkInput.new(JSON.parse(text))
    args = @argumentTypes.each_with_index.map { |type,idx|
      ConverterFactory.create(type).fromJsonSerializable(input.args[idx])
    }
    STDERR.puts "arguments: #{args}"

    startTime = Time.now
    result = @function.call(*args)
    endTime = Time.now
    elapseMs = (endTime - startTime) * 1000.0

    retType = @returnType
    if retType == 'Void' || retType == 'void'
      retType = @argumentTypes[0]
      result = args[0]
    end

    resConv = ConverterFactory.create(retType)
    serialResult = resConv.toJsonSerializable(result)
    resp = {
      'id' => input.id,
      'result' => serialResult,
      'elapse' => elapseMs
    }

    success = true
    if input.expected
      if @compareSerial && !@validator
        success = (input.expected == serialResult)
      else
        expect = resConv.fromJsonSerializable(input.expected)
        if @validator
          success = @validator.call(expect, result)
        else
          success = ValidatorFactory.create(retType).call(expect, result)
        end
      end
    end
    resp['success'] = success

    resp.to_json
  end
end

end  # module Soda::Unittest
