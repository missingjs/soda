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
# @param {ListNode} head
# @return {void}
sub reorderList {
    my $head = shift;
    my $fast = $head;
    my $slow = $head;
    while ($fast->next && $fast->next->next) {
        $slow = $slow->next;
        $fast = $fast->next->next;
    }
    return if $slow == $fast;

    my $r = do_reverse($slow->next);
    $slow->set_next(undef);
    merge($head, $r);
}

sub do_reverse {
    my $head = shift;
    my $q;
    while ($head) {
        my $next = $head->next;
        $head->set_next($q);
        $q = $head;
        $head = $next;
    }
    $q;
}

sub merge {
    my ($L1, $L2) = @_;
    my $t = ListNode->new;
    while ($L1 && $L2) {
        $t->set_next($L1);
        $t = $L1;
        $L1 = $L1->next;
        $t->set_next($L2);
        $t = $L2;
        $L2 = $L2->next;
    }
    $t->set_next($L1 || $L2);
}

package main;
use Soda::Unittest::Utils qw(from_stdin);
use Soda::Unittest::TestWork qw(create for_struct);
use Soda::Unittest::Validators qw(for_array for_array2d);
# step [2]: setup function/return/arguments
my $work = create(\&reorderList);
# my $work = for_struct(PACKAGE::);
# $work->validator(sub { $_[0] vs $_[1]; return boolean });
$work->compare_serial(1);
print $work->run(from_stdin());

