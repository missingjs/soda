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
package Node;
sub new {
    my ($class, $diff, $value) = @_;
    bless {
        diff  => $diff,
        value => $value
    }, $class;
}

sub with_target {
    my ($value, $target) = @_;
    new(__PACKAGE__, abs($target - $value), $value);
}

package main;
# @param {TreeNode} root
# @param {number} target
# @param {number} k
# @return {number[]}
sub closestKValues {
    my ($root, $target, $k) = @_;
    my @nodes;
    collect($root, \@nodes, $target);
    @nodes = sort { $a->{diff} <=> $b->{diff} } @nodes;
    [map { $_->{value} } @nodes[0..$k-1]]
}

sub collect {
    my ($root, $nodes, $target) = @_;

    return unless defined $root;
    push @$nodes, Node::with_target($root->val, $target);
    collect($root->left, $nodes, $target);
    collect($root->right, $nodes, $target);
}

package main;
use Soda::Unittest::Utils qw(from_stdin);
use Soda::Unittest::TestWork qw(create for_struct);
use Soda::Unittest::Validators qw(for_array for_array2d);
# step [2]: setup function/return/arguments
my $work = create(\&closestKValues);
# my $work = for_struct(PACKAGE::);
$work->validator(for_array('number', 0));
$work->compare_serial(1);
print $work->run(from_stdin());

