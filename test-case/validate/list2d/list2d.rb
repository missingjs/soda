# step [1]: implement solution function
# @param {String[]} strs
# @return {String[][]}
def group_by_length(strs)
  strs.shuffle!
  group = Hash.new{ |hash,key| hash[key] = Array.new }
  strs.each { |s|
    group[s.size] << s
  }
  group.keys.shuffle.map{ |k| group[k] }
end

if __FILE__ == $0
  require 'soda/unittest/work'
  ns = Soda::Unittest
  # step [2]: setup function/return/arguments
  work = ns::TestWork.create(method(:group_by_length))
  # work = ns::TestWork.forStruct(CLASS)
  work.validator = -> (e, r) { ns::Validators.for_array2d('String', false, false) }
  work.compare_serial = true
  puts work.run(ARGF.read)
end

