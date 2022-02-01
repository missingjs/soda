require 'set'
# step [1]: implement solution function
# @param {String} s 
# @return {String}
def reverse_vowels(s)
  isv = Array.new(128, false)
  'aeiouAEIOU'.each_char { |ch|
    isv[ch.ord] = true
  }
  buf = s.chars
  i, j = 0, s.size - 1
  while i < j
    while i < j && !isv[buf[i].ord]
      i += 1
    end
    while i < j && !isv[buf[j].ord]
      j -= 1
    end
    if i < j
      buf[i], buf[j] = buf[j], buf[i]
      i += 1
      j -= 1
    end
  end
  buf.join
end

if __FILE__ == $0
  require 'soda/unittest/work'
  ns = Soda::Unittest
  # step [2]: setup function/return/arguments
  work = ns::TestWork.create(method(:reverse_vowels))
  # work = ns::TestWork.forStruct(CLASS)
  # work.validator = -> (e, r) { e is equal to r }
  work.compare_serial = true
  puts work.run(ARGF.read)
end

