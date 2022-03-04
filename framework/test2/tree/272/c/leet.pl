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
use POSIX qw/floor/;
# @param {TreeNode} root
# @param {number} target
# @param {number} k
# @return {number[]}
sub closestKValues {
    my ($root, $target, $k) = @_;
    my @nodes;
    collect($root, \@nodes, $target);
    quick_select(\@nodes, 0, @nodes-1, $k);
    @nodes = sort { $a->{diff} <=> $b->{diff} } @nodes;
    [map { $_->{value} } @nodes[0..$k-1]]
}

sub quick_select {
    my ($nodes, $start, $end, $index) = @_;
    while ($start < $end) {
        my $mid = floor(($start + $end) / 2);
        place_median3($nodes, $start, $mid, $end);
        my $k = partition($nodes, $start, $end, $mid);
        if ($k == $index) {
            return;
        } elsif ($k > $index) {
            $end = $k - 1;
        } else {
            $start = $k + 1;
        }
    }
}

sub partition {
    my ($nodes, $start, $end, $pivot) = @_;
    my $d = $nodes->[$pivot]->{diff};
    swap($nodes, $pivot, $end);
    my $p = $start;
    for my $i ($start..$end) {
        if ($nodes->[$i]->{diff} < $d) {
            if ($p != $i) {
                swap($nodes, $p, $i);
            }
            ++$p;
        }
    }
    swap($nodes, $p, $end);
    $p;
}

sub place_median3 {
    my ($nodes, $start, $mid, $end) = @_;
    if ($nodes->[$start]->{diff} > $nodes->[$mid]->{diff}) {
        swap($nodes, $start, $mid);
    }
    if ($nodes->[$start]->{diff} > $nodes->[$end]->{diff}) {
        swap($nodes, $start, $end);
    }
    if ($nodes->[$mid]->{diff} > $nodes->[$end]->{diff}) {
        swap($nodes, $mid, $end);
    }
}

sub swap {
    my ($arr, $i, $j) = @_;
    ($arr->[$i], $arr->[$j]) = ($arr->[$j], $arr->[$i]);
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

