# step [1]: implement solution function
class Logger
  def initialize()
    @msgMap = {}
    @Limit = 10
    @lastTs = -@Limit
  end

=begin
  :type timestamp: Integer
  :type message: String
  :rtype: Boolean
=end
  def should_print_message(timestamp, message)
    _T = @lastTs
    @lastTs = timestamp
    if timestamp - _T >= @Limit
      @msgMap = {}
      @msgMap[message] = timestamp
      return true
    end
    if timestamp - @msgMap.fetch(message, timestamp - @Limit) < @Limit
      return false
    end
    @msgMap[message] = timestamp
    true
  end

end

if __FILE__ == $0
  require 'soda/unittest/work'
  ns = Soda::Unittest
  # step [2]: setup function/return/arguments
  # work = ns::TestWork.create(method(:my_solution))
  work = ns::TestWork.forStruct(Logger)
  # work.validator = -> (e, r) { e is equal to r }
  work.compareSerial = true
  puts work.run(ARGF.read)
end

