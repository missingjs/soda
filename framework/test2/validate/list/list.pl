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
# @param {character[]} chars
# @param {number} n
# @return {character[]}
sub permutation {
    my ($chars, $n) = @_;
    my @res;
    my @buf = (undef) x $n;
    solve($chars, 0, \@buf, 0, \@res);
    \@res;
}

sub solve {
    my ($chars, $i, $buf, $j, $res) = @_;
    if ($j == @$buf) {
        push @$res, join('', @$buf);
        return;
    }
    for (my $k = $i; $k < @$chars; ++$k) {
        ($chars->[$i], $chars->[$k]) = ($chars->[$k], $chars->[$i]);
        $buf->[$j] = $chars->[$i];
        solve($chars, $i+1, $buf, $j+1, $res);
        ($chars->[$i], $chars->[$k]) = ($chars->[$k], $chars->[$i]);
    }
}

package main;
use Soda::Unittest::Utils qw(from_stdin);
use Soda::Unittest::TestWork qw(create for_struct);
use Soda::Unittest::Validators qw(for_array for_array2d);
# step [2]: setup function/return/arguments
my $work = create(\&permutation);
# my $work = for_struct(PACKAGE::);
$work->validator(for_array('string', 0));
$work->compare_serial(1);
print $work->run(from_stdin());

