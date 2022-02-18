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
package NumMatrix;
# @param {number[][]} matrix
sub new {
    my ($class, $matrix) = @_;
    my $mr = @$matrix;
    my $mc = @{$matrix->[0]};
    my $rows = $mr + 1;
    my $cols = $mc + 1;
    my $mx = [map { [(0) x $mc] } (0) x $mr];
    my $bit = [map { [(0) x $cols] } (0) x $rows];

    my $self = {
        rows => $rows,
        cols => $cols,
        mx   => $mx,
        bit  => $bit
    };
    bless $self, $class;

    for (my $i = 0; $i < $rows-1; ++$i) {
        for (my $j = 0; $j < $cols-1; ++$j) {
            $self->update($i, $j, $matrix->[$i][$j]);
        }
    }
    $self;
}

# @param {number} row
# @param {number} col
# @param {number} val
# @return {void}
sub update {
    my ($self, $row, $col, $val) = @_;
    my $diff = $val - $self->{mx}->[$row][$col];
    my $i = $row + 1;
    while ($i < $self->{rows}) {
        my $j = $col + 1;
        while ($j < $self->{cols}) {
            $self->{bit}->[$i][$j] += $diff;
            $j += ($j & -$j);
        }
        $i += ($i & -$i);
    }
}

# @param {number} row1
# @param {number} col1
# @param {number} row2
# @param {number} col2
# @return {number}
sub sum_region {
    my ($self, $row1, $col1, $row2, $col2) = @_;
    $self->query($row1, $col1)
        - $self->query($row1, $col2+1)
        - $self->query($row2+1, $col1)
        + $self->query($row2+1, $col2+1);
}

sub query {
    my ($self, $r, $c) = @_;
    my $res = 0;
    my $i = $r;
    while ($i > 0) {
        my $j = $c;
        while ($j > 0) {
            $res += $self->{bit}->[$i][$j];
            $j -= ($j & -$j);
        }
        $i -= ($i & -$i);
    }
    $res;
}

package main;
use Soda::Unittest::Utils qw(from_stdin);
use Soda::Unittest::TestWork qw(create for_struct);
use Soda::Unittest::Validators qw(for_array for_array2d);
# step [2]: setup function/return/arguments
# my $work = create(\&add);
my $work = for_struct(NumMatrix::);
# $work->validator(sub { $_[0] vs $_[1]; return boolean });
$work->compare_serial(1);
print $work->run(from_stdin());

