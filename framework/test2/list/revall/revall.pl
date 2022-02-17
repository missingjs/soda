use strict;
use warnings;
use Soda::Leetcode::ListNode;

*ListNode:: = \*Soda::Leetcode::ListNode::;

# step [1]: implement solution function
# @param {ListNode[]} lists
# @return {ListNode[]}
sub reverse_all {
    my $lists = shift;
    for my $i (0..$#$lists) {
        $lists->[$i] = reverse_list($lists->[$i]);
    }
    my ($i, $j) = (0, $#$lists);
    while ($i < $j) {
        ($lists->[$i], $lists->[$j]) = ($lists->[$j], $lists->[$i]);
        ++$i;
        --$j;
    }
    $lists;
}

sub reverse_list {
    my $head = shift;
    my $h;
    while (defined $head) {
        my $next = $head->next;
        $head->set_next($h);
        $h = $head;
        $head = $next;
    }
    $h;
}

use Soda::Unittest::Utils qw(from_stdin);
use Soda::Unittest::TestWork qw(create);
use Soda::Unittest::Validators qw(for_array for_array2d);
# step [2]: setup function/return/arguments
my $work = create(\&reverse_all);
# $work->validator(sub { $_[0] vs $_[1]; return boolean });
$work->compare_serial(1);
print $work->run(from_stdin());

