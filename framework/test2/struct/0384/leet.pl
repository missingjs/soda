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
package Solution;
use POSIX qw/floor/;
# @param {number[]} nums
sub new {
    my ($class, $nums) = @_;
    bless {
        original => $nums
    }, $class;
}

# @return {number[]}
sub reset {
    my $self = shift;
    [@{$self->{original}}];
}

# @return {number[]}
sub shuffle {
    my $self = shift;
    my $res = $self->reset();
    for (my $s = @$res; $s > 0; --$s) {
        my $i = floor(rand() * $s);
        my $j = $s - 1;
        if ($i != $j) {
            ($res->[$i], $res->[$j]) = ($res->[$j], $res->[$i]);
        }
    }
    $res;
}

package main;
use Soda::Unittest::Utils qw(from_stdin);
use Soda::Unittest::TestWork qw(create for_struct);
use Soda::Unittest::Validators qw(for_array for_array2d);
# step [2]: setup function/return/arguments
# my $work = create(\&add);
my $work = for_struct(Solution::);
my $validator = sub {
    my ($expect, $result) = @_;
    my $args = $work->arguments;
    my $commands = $args->[0];
    for (my $i = 1; $i < @$commands; ++$i) {
        my $cmd = $commands->[$i];
        if ($cmd eq "shuffle") {
            my $evals = $expect->[$i];
            my $rvals = $result->[$i];
            return 0 unless for_array('number', 0)->($evals, $rvals);
        }
    }
    1;
};
$work->validator($validator);
$work->compare_serial(1);
print $work->run(from_stdin());

