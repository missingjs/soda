# step [1]: implement solution function
# @param {Integer} n
# @return {Integer}
def integer_break(n)
  solve(n)
end

$memo = Array.new(59, 0)

def solve(n)
  if n == 1
    return 1
  end
  if $memo[n] > 0
    return $memo[n]
  end
  res = 0
  1.step(n-1) { |i|
    res = [i*(n-i), i*solve(n-i), res].max
  }
  $memo[n] = res
  res
end

if __FILE__ == $0
  require 'soda/unittest/work'
  ns = Soda::Unittest
  # step [2]: setup function/return/arguments
  work = ns::TestWork.create(method(:integer_break))
  # work.validator = -> (e, r) { e is equal to r }
  work.compareSerial = true
  puts work.run(ARGF.read)
end

