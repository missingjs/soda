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
package VirIndex;

sub new {
    my ($class, $nums) = @_;
    bless {
        nums => $nums
    }, $class;
}

sub get {
    my ($self, $index) = @_;
    $self->{nums}->[$self->mapIndex($index)];
}

sub set {
    my ($self, $index, $value) = @_;
    $self->{nums}->[$self->mapIndex($index)] = $value;
}

sub mapIndex {
    my ($self, $i) = @_;
    my $N = @{$self->{nums}};
    if (($N&1) == 1 || $i > (($N-1)>>1)) {
        ((($N-$i) << 1) - 1) % $N;
    } else {
        $N - 2 - ($i << 1);
    }
}

package main;
use POSIX qw/floor/;
# @param {number[]} nums
# @return {void}
sub wiggleSort {
    my $nums = shift;
    my $vi = VirIndex->new($nums);
    my $N = @$nums;
    quick_select($vi, 0, $N-1, floor(($N-1)/2));
}

sub quick_select {
    my ($vi, $start, $end, $k) = @_;
    while ($start < $end) {
        my ($p0, $p1) = partition($vi, $start, $end);
        if ($k >= $p0 && $k <= $p1) {
            return;
        }
        if ($k > $p1) {
            $start = $p1 + 1;
        } else {
            $end = $p0 - 1;
        }
    }
}

sub partition {
    my ($vi, $start, $end) = @_;
    my $mid = floor(($start + $end) / 2);
    my $pivot = get_median($vi->get($start), $vi->get($mid), $vi->get($end));
    my $p = $start;
    my $z = $end + 1;
    for (my $q = $start; $q < $z; ) {
        if ($vi->get($q) < $pivot) {
            my $temp = $vi->get($p);
            $vi->set($p, $vi->get($q));
            $vi->set($q, $temp);
            ++$p;
            ++$q;
        } elsif ($vi->get($q) == $pivot) {
            ++$q;
        } else {
            --$z;
            my $temp = $vi->get($z);
            $vi->set($z, $vi->get($q));
            $vi->set($q, $temp);
        }
    }
    return ($p, $z-1);
}

sub get_median {
    my ($a, $b, $c) = @_;
    if ($a >= $b) {
        $b >= $c ? $b : min($a, $c);
    } else {
        $a >= $c ? $a : min($b, $c);
    }
}

package main;
use Soda::Unittest::Utils qw(from_stdin);
use Soda::Unittest::TestWork qw(create for_struct);
use Soda::Unittest::Validators qw(for_array for_array2d);
# step [2]: setup function/return/arguments
my $work = create(\&wiggleSort);
# my $work = for_struct(PACKAGE::);
my $validator = sub {
    shift;
    my $nums = shift;
    my @nums = @$nums;
    for my $i (1..$#nums) {
        if ($i % 2 != 0 && $nums[$i] <= $nums[$i-1]
                || $i % 2 == 0 && $nums[$i] >= $nums[$i-1]) {
            return 0;
        }
    }
    return 1;
};
$work->validator($validator);
$work->compare_serial(1);
print $work->run(from_stdin());

