use strict;
use warnings;
use List::Util qw(min max);
use Tree::RB;
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
# @param {number} k
# @param {number} t
# @return {boolean}
sub containsNearbyAlmostDuplicate {
    my ($nums, $k, $t) = @_;
    my $imap = Tree::RB->new(sub { $_[0] <=> $_[1] });
    my ($i, $j) = (0, 0);
    while ($j < @$nums) {
        if ($j - $i <= $k) {
            my $val = $nums->[$j];
            ++$j;
            my $lower = $val - $t;
            my $upper = $val + $t;
            my ($value, $node) = $imap->lookup($lower, Tree::RB::LUGTEQ);
            if (defined($node) && $node->key <= $upper) {
                return 1;
            }
            my $c = $imap->get($val);
            if (defined $c) {
                $imap->put($val, $c+1);
            } else {
                $imap->put($val, 1);
            }
        } else {
            my $val = $nums->[$i++];
            my $c = $imap->get($val) - 1;
            if ($c == 0) {
                $imap->delete($val);
            } else {
                $imap->put($val, $c);
            }
        }
    }
    return 0;
}

package main;
use Soda::Unittest::Utils qw(from_stdin);
use Soda::Unittest::TestWork qw(create for_struct);
use Soda::Unittest::Validators qw(for_array for_array2d);
# step [2]: setup function/return/arguments
my $work = create(\&containsNearbyAlmostDuplicate);
# my $work = for_struct(PACKAGE::);
# $work->validator(sub { $_[0] vs $_[1]; return boolean });
$work->compare_serial(1);
print $work->run(from_stdin());

