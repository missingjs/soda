use strict;
use warnings;
use List::Util qw(min max);
use Soda::Leetcode::ListNode;
use Soda::Leetcode::NestedInteger;

*ListNode:: = \*Soda::Leetcode::ListNode::;
*NestedInteger:: = \*Soda::Leetcode::NestedInteger::;

# step [1]: implement solution function
package SummaryRanges;
sub new {
    my $class = shift;
    bless {
        parent       => [(0) x 10003],
        ancestor_set => {}
    }, $class;
}

# @param {number} val
# @return {void}
sub add_num {
    my ($self, $val) = @_;
    ++$val;
    return if $self->{parent}->[$val];
    $self->{parent}->[$val] = -1;
    $self->{ancestor_set}->{$val} = 1;
    my $left = $val - 1;
    my $right = $val + 1;
    if ($left > 0 && $self->{parent}->[$left] != 0) {
        $self->merge($left, $val);
    }
    unless ($self->{parent}->[$right] == 0) {
        $self->merge($val, $right);
    }
}

# @return {number[][]}
sub get_intervals {
    my $self = shift;
    my @ans = sort { $a <=> $b } keys %{$self->{ancestor_set}};
    my @res = (undef) x @ans;
    for my $i (0..$#res) {
        my $start = $ans[$i];
        my $end = $start - $self->{parent}->[$start] - 1;
        $res[$i] = [$start-1, $end-1];
    }
    \@res;
}

sub merge {
    my ($self, $x, $y) = @_;
    my $ax = $self->get_ancestor($x);
    my $ay = $self->get_ancestor($y);
    if ($ax < $ay) {
        $self->merge_ancestor($ax, $ay);
    } else {
        $self->merge_ancestor($ay, $ax);
    }
}

sub merge_ancestor {
    my ($self, $ax, $ay) = @_;
    $self->{parent}->[$ax] += $self->{parent}->[$ay];
    $self->{parent}->[$ay] = $ax;
    delete $self->{ancestor_set}->{$ay};
}

sub get_ancestor {
    my ($self, $x) = @_;
    $self->{parent}->[$x] < 0
        ? $x
        : ($self->{parent}->[$x] = $self->get_ancestor($self->{parent}->[$x]));
}

package main;
use Soda::Unittest::Utils qw(from_stdin);
use Soda::Unittest::TestWork qw(create for_struct);
use Soda::Unittest::Validators qw(for_array for_array2d);
# step [2]: setup function/return/arguments
# my $work = create(\&add);
my $work = for_struct(SummaryRanges::);
# $work->validator(sub { $_[0] vs $_[1]; return boolean });
$work->compare_serial(1);
print $work->run(from_stdin());

