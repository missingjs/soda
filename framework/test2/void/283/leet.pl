use strict;
use warnings;
use List::Util qw(min max);
use Soda::DS::PriorityQueue;
use Soda::Leetcode::ListNode;
use Soda::Leetcode::NestedInteger;
use Soda::Leetcode::TreeNode;

*ListNode:: = \*Soda::Leetcode::ListNode::;
*NestedInteger:: = \*Soda::Leetcode::NestedInteger::;
*PriorityQueue:: = \*Soda::DS::PriorityQueue::;
*TreeNode:: = \*Soda::Unittest::TestWork::;

# step [1]: implement solution function
# @param {number[]} nums
# @return {void}
sub moveZeroes {
    my $nums = shift;
    my $p = 0;
    for my $i (0..$#$nums) {
        if ($nums->[$i] != 0) {
            if ($i != $p) {
                ($nums->[$i], $nums->[$p]) = ($nums->[$p], $nums->[$i]);
            }
            ++$p;
        }
    }
    while ($p < @$nums) {
        $nums->[$p] = 0;
        ++$p;
    }
}

package main;
use Soda::Unittest::Utils qw(from_stdin);
use Soda::Unittest::TestWork qw(create for_struct);
use Soda::Unittest::Validators qw(for_array for_array2d);
# step [2]: setup function/return/arguments
my $work = create(\&moveZeroes);
# my $work = for_struct(PACKAGE::);
# $work->validator(sub { $_[0] vs $_[1]; return boolean });
$work->compare_serial(1);
print $work->run(from_stdin());

