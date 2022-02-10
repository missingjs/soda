require 'algorithms'
require 'set'
require 'soda/leetcode/list'
require 'soda/leetcode/nest'
# step [1]: implement solution function
class Solution

=begin
    :type nums: Integer[]
=end
    def initialize(nums)
      @original = nums.to_a
    end

=begin
    :rtype: Integer[]
=end
    def reset()
      @original.to_a
    end

=begin
    :rtype: Integer[]
=end
    def shuffle()
      res = reset
      (res.size...0).step(-1).each { |s|
        i = rand(0...s)
        j = s - 1
        if i != j
          res[i], res[j] = res[j], res[i]
        end
      }
      res
    end

end

if __FILE__ == $0
  require 'soda/unittest/work'
  ns = Soda::Unittest
  # step [2]: setup function/return/arguments
  # work = ns::TestWork.create(method(:my_solution))
  work = ns::TestWork.for_struct(Solution)
  work.validator = ->(expect, result) {
    arguments = work.arguments
    commands = arguments[0]
    (1...commands.size).each { |i|
      cmd = commands[i]
      if cmd == 'shuffle'
        evalues = expect[i]
        rvalues = result[i]
        if !ns::Validators.for_array('Integer', false).call(evalues, rvalues)
          return false
        end
      end
    }
    true
  }
  work.compare_serial = true
  puts work.run(ARGF.read)
end

