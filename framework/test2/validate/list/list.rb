# step [1]: implement solution function
# @param {String[]} chars
# @param {Integer} n
# @return {String[]}
def permutation(chars, n)
  res = []
  buf = Array.new(n, '')
  solve(chars, 0, buf, 0, res)
  res
end

def solve(chars, i, buf, j, res)
  if j == buf.size
    res << buf.join
    return
  end
  (i...chars.size).each { |k|
    chars[i], chars[k] = chars[k], chars[i]
    buf[j] = chars[i]
    solve(chars, i+1, buf, j+1, res)
    chars[i], chars[k] = chars[k], chars[i]
  }
end

if __FILE__ == $0
  require 'soda/unittest/work'
  include Soda::Unittest
  # step [2]: setup function/return/arguments
  work = TestWork.create(method(:permutation))
  # work = ns::TestWork.forStruct(CLASS)
  work.validator = ->(e, r) { Validators.for_array('String', false) }
  work.compare_serial = true
  puts work.run(ARGF.read)
end

