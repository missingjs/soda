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
# @param {string[]} s
# @return {void}
sub reverse_words {
    my $s = shift;
    return unless @$s;

    reverse_array($s, 0, @$s-1);

    my $N = @$s;
    my $i = 0;
    my $j = 0;
    while ($j < $N) {
        if ($s->[$j] eq ' ') {
            reverse_array($s, $i, $j-1);
            $i = $j + 1;
        }
        ++$j;
    }
    if ($i < $j) {
        reverse_array($s, $i, $j-1);
    }
}

sub reverse_array {
    my ($s, $i, $j) = @_;
    while ($i < $j) {
        ($s->[$i], $s->[$j]) = ($s->[$j], $s->[$i]);
        ++$i;
        --$j;
    }
}

package main;
use Soda::Unittest::Utils qw(from_stdin);
use Soda::Unittest::TestWork qw(create for_struct);
use Soda::Unittest::Validators qw(for_array for_array2d);
# step [2]: setup function/return/arguments
my $work = create(\&reverse_words);
# my $work = for_struct(PACKAGE::);
# $work->validator(sub { $_[0] vs $_[1]; return boolean });
$work->compare_serial(1);
print $work->run(from_stdin());

