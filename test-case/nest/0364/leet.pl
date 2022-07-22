use strict;
use warnings;
use List::Util qw(min max);
use Soda::Leetcode::ListNode;
use Soda::Leetcode::NestedInteger;

*ListNode:: = \*Soda::Leetcode::ListNode::;
*NestedInteger:: = \*Soda::Leetcode::NestedInteger::;

# step [1]: implement solution function
# @param {NestedInteger[]} nested_list
# @return {Integer}
sub depth_sum_inverse {
    my $nested_list = shift;
    my $info = get_info($nested_list, 1);
    ($info->{max_depth} + 1) * $info->{sum} - $info->{product};
}

sub get_info {
    my ($nested_list, $depth) = @_;
    my ($sum, $product, $max_depth) = (0, 0, $depth);
    for my $ni (@$nested_list) {
        if ($ni->is_integer) {
            my $val = $ni->get_integer;
            $sum += $val;
            $product += $val * $depth;
            $max_depth = max($max_depth, $depth);
        } else {
            my $res = get_info($ni->get_list, $depth + 1);
            $sum += $res->{sum};
            $product += $res->{product};
            $max_depth = max($max_depth, $res->{max_depth});
        }
    }
    {sum => $sum, product => $product, max_depth => $max_depth};
}

use Soda::Unittest::Utils qw(from_stdin);
use Soda::Unittest::TestWork qw(create);
use Soda::Unittest::Validators qw(for_array for_array2d);
# step [2]: setup function/return/arguments
my $work = create(\&depth_sum_inverse);
# $work->validator(sub { $_[0] vs $_[1]; return boolean });
$work->compare_serial(1);
print $work->run(from_stdin());

