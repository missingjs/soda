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

# @param {ListNode[]} lists
# @return {ListNode}
sub mergeKLists {
    my $lists = shift;
    my $qu = PriorityQueue->new(sub {
        $_[0]->{val} - $_[1]->{val}
    });
    for my $L (@$lists) {
        $qu->push($L) if defined $L;
    }

    my $head = ListNode->new;
    my $tail = $head;
    until ($qu->empty) {
        my $t = $qu->pop;
        my $L = $t->next;
        $qu->push($L) if defined $L;
        $tail->set_next($t);
        $tail = $t;
    }
    $head->next;
}

package main;
use Soda::Unittest::Utils qw(from_stdin);
use Soda::Unittest::TestWork qw(create for_struct);
use Soda::Unittest::Validators qw(for_array for_array2d);
# step [2]: setup function/return/arguments
my $work = create(\&mergeKLists);
# my $work = for_struct(PACKAGE::);
# $work->validator(sub { $_[0] vs $_[1]; return boolean });
$work->compare_serial(1);
print $work->run(from_stdin());

