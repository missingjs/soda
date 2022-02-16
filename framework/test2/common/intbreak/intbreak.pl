use strict;
use warnings;
use List::Util qw(min max);

# step [1]: implement solution function
# @param {number} n
# @return {number}
sub integer_break {
    my ($n) = @_;
    solve($n);
}

my @memo = (0) x 59;

sub solve {
    my ($n) = @_;
    if ($n == 1) {
        return 1;
    }
    if ($memo[$n] > 0) {
        return $memo[$n];
    }
    my $res = 0;
    for my $i (1..$n-1) {
        $res = max($i * ($n-$i), $i * solve($n-$i), $res);
    }
    $memo[$n] = $res;
}

use Soda::Unittest::Utils qw(from_stdin);
use Soda::Unittest::TestWork qw(create);
# step [2]: setup function/return/arguments
my $work = create(\&integer_break);
# $work->validator(sub { $_[0] vs $_[1]; return boolean });
$work->compare_serial(1);
print $work->run(from_stdin());

