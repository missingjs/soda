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
use POSIX qw/floor/;
use Data::Dumper;
# @param {string[]} strs
# @return {string[][]}
sub group_by_length {
    my $strs = shift;
    shuffle($strs);
    my %group;
    for my $s (@$strs) {
        $group{length($s)} = [] unless exists $group{length($s)};
        push @{$group{length($s)}}, $s;
    }
    my $keys = [keys %group];
    shuffle($keys);
    [map { $group{$_} } @$keys];
}

sub shuffle {
    my $arr = shift;
    for (my $i = @$arr; $i > 0; --$i) {
        my $index = floor(rand() * $i);
        if ($index != $i-1) {
            ($arr->[$index], $arr->[$i-1]) = ($arr->[$i-1], $arr->[$index]);
        }
    }
}

package main;
use Soda::Unittest::Utils qw(from_stdin);
use Soda::Unittest::TestWork qw(create for_struct);
use Soda::Unittest::Validators qw(for_array for_array2d);
# step [2]: setup function/return/arguments
my $work = create(\&group_by_length);
# my $work = for_struct(PACKAGE::);
$work->validator(for_array2d('string', 0, 0));
$work->compare_serial(1);
print $work->run(from_stdin());

