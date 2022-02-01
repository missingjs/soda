# step [1]: implement solution function
# @param {Float[][]} a
# @param {Float[][]} b
# @return {Float[][]}
def matrix_multiply(a, b)
  rows, cols = a.size, b[0].size
  res = Array.new(rows){Array.new(cols, 0.0)}
  (0...rows).each { |i|
    (0...cols).each { |j|
      c = 0.0
      (0...b.size).each { |k|
        c += a[i][k] * b[k][j]
      }
      res[i][j] = c
    }
  }
  res
end

if __FILE__ == $0
  require 'soda/unittest/work'
  ns = Soda::Unittest
  # step [2]: setup function/return/arguments
  work = ns::TestWork.create(method(:matrix_multiply))
  # work = ns::TestWork.forStruct(CLASS)
  # work.validator = -> (e, r) { e is equal to r }
  # work.compare_serial = true
  puts work.run(ARGF.read)
end

