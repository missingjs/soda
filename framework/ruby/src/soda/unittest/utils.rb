require 'soda/unittest/convert'

module Soda end

module Soda::Unittest

class Utils
  def self.parse_arguments(arg_types, raw_args)
    arg_types.each_with_index.map { |type, idx|
      ConverterFactory.create(type).from_json_serializable(raw_args[idx])
    }
  end

  def self.function_type_hints(file_path, func_name)
    lines = []
    File.readlines(file_path).each { |line|
      if line =~ /^def #{func_name}\(/
        break
      end
      if line.strip.length > 0
        lines << line
      end
    }
    ret_type = "Void"
    arg_types = []
    while lines.length > 0
      text = lines.pop
      if /^#\s+@param\s+{(?<arg_type>.*)}/ =~ text
        arg_types << arg_type
      elsif /^#\s+@return\s+{(?<return_type>.*)}/ =~ text
        ret_type = return_type
      else
        break
      end
    end
    arg_types = arg_types.reverse()
    arg_types << ret_type
  end

  def self.method_type_hints(file_path, klass)
    class_name = klass.name
    lines = File.readlines(file_path)
    i = 0
    while i < lines.size && lines[i] !~ /^class #{class_name}/
      i += 1
    end
    if i == lines.size
      raise Exception.new "class #{class_name} not found in #{file_path}"
    end

    hints_map = {}
    hints_buf = []
    until /^end/ =~ lines[i]
      if /^=begin/ =~ lines[i]
        i += 1
        while /^=end/ !~ lines[i]
          hints_buf << lines[i]
          i += 1
        end
        i += 1
        if /^\s*def (?<method_name>\w+)/ =~ lines[i]
          hints_map[method_name] = hints_buf.map { |line, _|
            /[:].*[:]\s+(\S+)/.match(line)[1]
          }
          hints_buf.clear
        end
      end
      i += 1
    end

    klass.methods(false).map { |s| s.to_s }.each { |name|
      unless hints_map.key?(name)
        hints_map[name] = ['Void']
      end
    }
    unless hints_map.key?('initialize')
      hints_map['initialize'] = []
    end
    hints_map
  end

  def self.underscore(s)
    s.gsub(/::/, '/').
    gsub(/([A-Z]+)([A-Z][a-z])/,'\1_\2').
    gsub(/([a-z\d])([A-Z])/,'\1_\2').
    tr("-", "_").
    downcase
  end
end

end  # module Soda::Unittest
