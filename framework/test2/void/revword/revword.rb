# step [1]: implement solution function
# @param {String[]} s
# @return {Void}
def reverseWords(s)
  if s.size == 0
    return
  end
  reverse(s, 0, s.size - 1)
  _N = s.size
  i = j = 0
  while j < _N
    if s[j] == ' '
      reverse(s, i, j-1)
      i = j + 1
    end
    j += 1
  end
  if i < j
    reverse(s, i, j-1)
  end
end

def reverse(s, i, j)
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
  work = ns::TestWork.create(method(:reverseWords))
  # work.validator = -> (e, r) { e is equal to r }
  work.compareSerial = true
  puts work.run(ARGF.read)
end

