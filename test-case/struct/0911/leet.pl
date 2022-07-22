use strict;
use warnings;
use List::Util qw(min max);
use Soda::DS::PriorityQueue;
use Soda::Leetcode::ListNode;
use Soda::Leetcode::NestedInteger;

*ListNode:: = \*Soda::Leetcode::ListNode::;
*NestedInteger:: = \*Soda::Leetcode::NestedInteger::;
*PriorityQueue:: = \*Soda::DS::PriorityQueue::;

# step [1]: implement solution function
package TopVotedCandidate;
#use POSIX qw/floor/;
# @param {number[]} persons
# @param {number[]} times
sub new {
    my ($class, $persons, $times) = @_;
    my $n = @$persons;
    my $winner = [(0) x $n];
    my $self = {
        N      => $n,
        times  => $times,
        winner => $winner
    };
    bless $self, $class;
    my @counter = (0) x ($n+1);
    my $win = 0;
    for (my $i = 0; $i < $n; ++$i) {
        if (++$counter[$persons->[$i]] >= $counter[$win]) {
            $win = $persons->[$i];
        }
        $winner->[$i] = $win;
    }
    $self;
}

# @param {number} t
# @return {number}
sub q {
    my ($self, $t) = @_;
    if ($t >= $self->{times}->[-1]) {
        return $self->{winner}->[$self->{N}-1];
    }
    my ($low, $high) = (0, $self->{N}-1);
    while ($low < $high) {
        my $mid = ($low + $high) >> 1;
        if ($t <= $self->{times}->[$mid]) {
            $high = $mid;
        } else {
            $low = $mid + 1;
        }
    }
    my $idx = $t == $self->{times}->[$low] ? $low : $low - 1;
    $self->{winner}->[$idx];
}

package main;
use Soda::Unittest::Utils qw(from_stdin);
use Soda::Unittest::TestWork qw(create for_struct);
use Soda::Unittest::Validators qw(for_array for_array2d);
# step [2]: setup function/return/arguments
# my $work = create(\&add);
my $work = for_struct(TopVotedCandidate::);
# $work->validator(sub { $_[0] vs $_[1]; return boolean });
$work->compare_serial(1);
print $work->run(from_stdin());

