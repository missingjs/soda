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
# @param {TreeNode} p
# @param {TreeNode} q
# @return {TreeNode}
sub lowest_common_ancestor {
    my ($root, $p, $q) = @_;
    my @stk = ($root);
    my $last = $root;
    my $found_one = 0;
    my $index = -1;

    if (eqref($root, $p) || eqref($root, $q)) {
        $found_one = 1;
        $index = 0;
    }

    while (@stk) {
        my $node = $stk[-1];
        if ($node->left && !eqref($last, $node->left) && !eqref($last, $node->right)) {
            if (eqref($node->left, $p) || eqref($node->left, $q)) {
                if (!$found_one) {
                    $index = @stk;
                    $found_one = 1;
                } else {
                    return $stk[$index];
                }
            }
            push @stk, $node->left;
        } elsif ($node->right && !eqref($last, $node->right)) {
            if (eqref($node->right, $p) || eqref($node->right, $q)) {
                if (!$found_one) {
                    $index = @stk;
                    $found_one = 1;
                } else {
                    return $stk[$index];
                }
            }
            push @stk, $node->right;
        } else {
            $last = $node;
            if ($index == $#stk) {
                --$index;
            }
            pop @stk;
        }
    }
    undef;
}

sub eqref {
    my ($rx, $ry) = @_;
    ref($rx) && ref($ry) && $rx == $ry;
}

# @param {TreeNode} root
# @param {Integer} p
# @param {Integer} q
# @return {Integer}
sub driver {
    my ($root, $p, $q) = @_;
    my $p_node = find_node($root, $p);
    my $q_node = find_node($root, $q);
    lowest_common_ancestor($root, $p_node, $q_node)->val;
}

sub find_node {
    my ($root, $val) = @_;
    return undef unless $root;
    return $root if $root->val == $val;
    find_node($root->left, $val) || find_node($root->right, $val);
}

package main;
use Soda::Unittest::Utils qw(from_stdin);
use Soda::Unittest::TestWork qw(create for_struct);
use Soda::Unittest::Validators qw(for_array for_array2d);
# step [2]: setup function/return/arguments
my $work = create(\&driver);
# my $work = for_struct(PACKAGE::);
# $work->validator(sub { $_[0] vs $_[1]; return boolean });
$work->compare_serial(1);
print $work->run(from_stdin());

