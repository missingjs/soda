require 'soda/unittest/convert'
require 'soda/unittest/utils'

module Soda end

module Soda::Unittest

class StructTester
  def self.create(klass, method_hints)
    ->(operations, parameters) {
      # hints of initialize method has not return type
      ctor_args = Utils.parse_arguments(method_hints["initialize"], parameters[0])
      obj = klass.new(*ctor_args)
      res = [nil]
      1.step(operations.size-1) { |i|
        method_name = operations[i]
        unless method_hints.key?(method_name)
          ud = Utils.underscore(method_name)
          if method_hints.key?(ud)
            method_name = ud
          else
            raise Exception.new "method #{method_name} and #{ud} not found in class #{klass.name}"
          end
        end
        hints = method_hints[method_name]
        arg_types = hints[...-1]
        ret_type = hints[-1]
        args = Utils.parse_arguments(arg_types, parameters[i])
        r = obj.public_send(method_name, *args)
        if ret_type.downcase != 'void'
          res << ConverterFactory.create(ret_type).to_json_serializable(r)
        else
          res << nil
        end
      }
      res
    }
  end

  def self.method_hints
    %w[String[] Object[] Object[]]
  end
end

end  # module Soda::Unittest

