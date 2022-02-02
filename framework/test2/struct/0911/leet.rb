# step [1]: implement solution function

class TopVotedCandidate
=begin
  :type persons: Integer[]
  :type times: Integer[]
=end
  def initialize(persons, times)
    @_N = persons.size
    @times = times
    @winner = Array.new(@_N, 0)
    counter = Array.new(@_N+1, 0)
    win = 0
    0.step(@_N-1) { |i|
      counter[persons[i]] += 1
      if counter[persons[i]] >= counter[win]
        win = persons[i]
      end
      @winner[i] = win
    }
  end

=begin
  :type t: Integer
  :rtype: Integer
=end
  def q(t)
    if t >= @times[-1]
      return @winner[@_N-1]
    end
    low, high = 0, @_N - 1
    while low < high
      mid = (low + high) / 2
      if t <= @times[mid]
        high = mid
      else
        low = mid + 1
      end
    end
    t == @times[low] ? @winner[low] : @winner[low-1]
  end
end

if __FILE__ == $0
  require 'soda/unittest/work'
  ns = Soda::Unittest
  # step [2]: setup function/return/arguments
  # work = ns::TestWork.create(method(:my_solution))
  work = ns::TestWork.for_struct(TopVotedCandidate)
  # work.validator = -> (e, r) { e is equal to r }
  work.compare_serial = true
  puts work.run(ARGF.read)
end

