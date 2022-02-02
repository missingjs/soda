require 'algorithms'
require 'set'
require 'soda/leetcode/list'
require 'soda/leetcode/nest'
require 'soda/leetcode/tree'
# step [1]: implement solution function

class NumMatrix
=begin
  :type matrix: Integer[][]
=end
  def initialize(matrix)
    @rows = matrix.size + 1
    @cols = matrix[0].size + 1
    @mx = Array.new(matrix.size) { Array.new(matrix[0].size, 0) }
    @bit = Array.new(@rows) { Array.new(@cols, 0) }
    (0...@rows-1).each { |i|
      (0...@cols-1).each { |j|
        update(i, j, matrix[i][j])
      }
    }
  end

=begin
  :type row: Integer
  :type col: Integer
  :type val: Integer
  :rtype: Void
=end
  def update(row, col, val)
    diff = val - @mx[row][col]
    @mx[row][col] = val
    i = row + 1
    while i < @rows
      j = col + 1
      while j < @cols
        @bit[i][j] += diff
        j += (j & -j)
      end
      i += (i & -i)
    end
  end

=begin
  :type row1: Integer
  :type col1: Integer
  :type row2: Integer
  :type col2: Integer
  :rtype: Integer
=end
  def sum_region(row1, col1, row2, col2)
    query(row1, col1) - query(row1, col2+1) - query(row2+1, col1) + query(row2+1, col2+1)
  end

  def query(r, c)
    res = 0
    i = r
    while i > 0
      j = c
      while j > 0
        res += @bit[i][j]
        j -= (j & -j)
      end
      i -= (i & -i)
    end
    res
  end
end

if __FILE__ == $0
  require 'soda/unittest/work'
  ns = Soda::Unittest
  # step [2]: setup function/return/arguments
  # work = ns::TestWork.create(method(:my_solution))
  work = ns::TestWork.for_struct(NumMatrix)
  # work.validator = -> (e, r) { e is equal to r }
  work.compare_serial = true
  puts work.run(ARGF.read)
end

