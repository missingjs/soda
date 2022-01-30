require 'soda/unittest/convert'
require 'soda/unittest/utils'

module Soda end

module Soda::Unittest

class StructTester
  def self.create(klass, methodHints)
    ->(operations, parameters) {
      # hints of initialize method has not return type
      ctorArgs = Utils.parseArguments(methodHints["initialize"], parameters[0])
      obj = klass.new(*ctorArgs)
      res = [nil]
      1.step(operations.size-1) { |i|
        methodName = operations[i]
        if !methodHints.key?(methodName)
          ud = Utils.underscore(methodName)
          if methodHints.key?(ud)
            methodName = ud
          else
            raise Exception.new "method #{methodName} and #{ud} not found in class #{klass.name}"
          end
        end
        hints = methodHints[methodName]
        argTypes = hints[...-1]
        retType = hints[-1]
        args = Utils.parseArguments(argTypes, parameters[i])
        r = obj.public_send(methodName, *args)
        res << ConverterFactory.create(retType).toJsonSerializable(r)
      }
      res
    }
  end

  def self.methodHints
    ['String[]', 'Object[]', 'Object[]']
  end
end

end  # module Soda::Unittest

