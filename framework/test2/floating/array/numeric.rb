# step [1]: implement solution function
# @param {Float[]} a
# @param {Float[]} b
# @return {Float[]}
def multiply(a, b)
  a.zip(b).map { |pair|
    pair[0] * pair[1]
  }
end

if __FILE__ == $0
  require 'soda/unittest/work'
  ns = Soda::Unittest
  # step [2]: setup function/return/arguments
  work = ns::TestWork.create(method(:multiply))
  # work = ns::TestWork.forStruct(CLASS)
  # work.validator = -> (e, r) { e is equal to r }
  # work.compareSerial = true
  puts work.run(ARGF.read)
end

