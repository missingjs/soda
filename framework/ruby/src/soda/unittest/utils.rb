require 'soda/unittest/convert'

module Soda end

module Soda::Unittest

class Utils
  def self.parseArguments(argTypes, rawArgs)
    argTypes.each_with_index.map { |type, idx|
      ConverterFactory.create(type).fromJsonSerializable(rawArgs[idx])
    }
  end

  def self.functionTypeHints(filePath, funcName)
    lines = []
    File.readlines(filePath).each { |line|
      if line =~ /^def #{funcName}\(/
        break
      end
      if line.strip.length > 0
        lines << line
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
    argTypes << retType
  end

  def self.methodTypeHints(filePath, klass)
    className = klass.name
    lines = File.readlines(filePath)
    i = 0
    while i < lines.size && lines[i] !~ /^class #{className}/
      i += 1
    end
    if i == lines.size
      raise Exception.new "class #{className} not found in #{filePath}"
    end

    hintsMap = {}
    hintsBuf = []
    until /^end/ =~ lines[i]
      if /^=begin/ =~ lines[i]
        i += 1
        while /^=end/ !~ lines[i]
          hintsBuf << lines[i]
          i += 1
        end
        i += 1
        if /^\s*def (?<methodName>\w+)/ =~ lines[i]
          hintsMap[methodName] = hintsBuf.map { |line, _|
            /[:].*[:]\s+(\w+)/.match(line)[1]
          }
          hintsBuf.clear
        end
      end
      i += 1
    end

    for name in klass.methods(false).map { |s| s.to_s }
      if !hintsMap.key?(name)
        hintsMap[name] = ['Void']
      end
    end
    if !hintsMap.key?('initialize')
      hintsMap['initialize'] = []
    end
    hintsMap
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
