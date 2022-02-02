require 'set'
require 'soda/leetcode/list'
require 'soda/leetcode/nest'
# step [1]: implement solution function
# @param {String} s
# @return {NestedInteger}
def deserialize(s)
  $p = 0
  parse(s)
end

$p = 0
def parse(s)
  if s[$p] == '['
    $p += 1
    root = Soda::Leetcode::NestedInteger.new
    until s[$p] == ']'
      root.add(parse(s))
      if s[$p] == ','
        $p += 1
      end
    end
    $p += 1
    return root
  end

  negative = false
  if s[$p] == '-'
    $p += 1
    negative = true
  end

  value = 0
  while $p < s.size && s[$p] >= '0' && s[$p] <= '9'
    value = value * 10 + s[$p].ord - '0'.ord
    $p += 1
  end

  if negative
    value = -value
  end
  Soda::Leetcode::NestedInteger.new(value)
end

if __FILE__ == $0
  require 'soda/unittest/work'
  ns = Soda::Unittest
  # step [2]: setup function/return/arguments
  work = ns::TestWork.create(method(:deserialize))
  # work = ns::TestWork.forStruct(CLASS)
  # work.validator = -> (e, r) { e is equal to r }
  work.compare_serial = true
  puts work.run(ARGF.read)
end

