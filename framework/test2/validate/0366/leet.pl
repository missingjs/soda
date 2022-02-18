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
# @param {TreeNode} root
# @return {number[][]}
sub find_leaves {
    my $root = shift;
    my @res = map { [] } (0)x100;
    my $r = solve($root, \@res);
    return [@res[0..$r-1]];
}

sub solve {
    my ($root, $res) = @_;
    return 0 unless defined $root;
    my $R = solve($root->right, $res);
    my $L = solve($root->left, $res);
    my $index = max($L, $R);
    push @{$res->[$index]}, $root->val;
    $index + 1;
}

package main;
use Soda::Unittest::Utils qw(from_stdin);
use Soda::Unittest::TestWork qw(create for_struct);
use Soda::Unittest::Validators qw(for_array for_array2d);
# step [2]: setup function/return/arguments
my $work = create(\&find_leaves);
# my $work = for_struct(PACKAGE::);
$work->validator(for_array2d('number', 1, 0));
$work->compare_serial(1);
print $work->run(from_stdin());

