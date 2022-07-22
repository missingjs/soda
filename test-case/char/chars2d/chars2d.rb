# step [1]: implement solution function
# @param {String[][]} matrix
# @return {String[][]}
def to_upper(matrix)
  diff = 'a'.ord - 'A'.ord
  (0...matrix.size).each { |i|
    (0...matrix[i].size).each { |j|
      matrix[i][j] = (matrix[i][j].ord - diff).chr
    }
  }
  matrix
end

if __FILE__ == $0
  require 'soda/unittest/work'
  ns = Soda::Unittest
  # step [2]: setup function/return/arguments
  work = ns::TestWork.create(method(:to_upper))
  # work = ns::TestWork.forStruct(CLASS)
  # work.validator = -> (e, r) { e is equal to r }
  work.compare_serial = true
  puts work.run(ARGF.read)
end

