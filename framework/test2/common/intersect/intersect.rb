require 'set'
# step [1]: implement solution function
# @param {Integer[]} nums1
# @param {Integer[]} nums2
# @return {Integer[]}
def intersection(nums1, nums2)
  if nums1.size > nums2.size
    return intersection(nums2, nums1)
  end
  mset = nums1.to_set
  res = Set.new
  nums2.each { |b|
    if mset.include?(b)
      res << b
    end
  }
  res.to_a
end

if __FILE__ == $0
  require 'soda/unittest/work'
  ns = Soda::Unittest
  # step [2]: setup function/return/arguments
  work = ns::TestWork.create(method(:intersection))
  # work = ns::TestWork.forStruct(CLASS)
  work.validator = ns::Validators.for_array('Integer', false)
  work.compare_serial = true
  puts work.run(ARGF.read)
end

