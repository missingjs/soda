# step [1]: implement solution function
# @param {String[]} n
# @return {Void}
def reverseString(s)
  i, j = 0, s.size - 1
  while i < j
    s[i], s[j] = s[j], s[i]
    i += 1
    j -= 1
  end
end

if __FILE__ == $0
  require 'soda/unittest/work'
  ns = Soda::Unittest
  # step [2]: setup function/return/arguments
  work = ns::TestWork.create(method(:reverseString))
  # work.validator = -> (e, r) { e is equal to r }
  work.compareSerial = true
  puts work.run(ARGF.read)
end

