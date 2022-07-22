use strict;
use warnings;

use Data::Dumper;

# step [1]: implement solution function
# @param {number[][]} a
# @param {number[][]} b
# @return {number[][]}
sub matrix_multiply {
    my ($a, $b) = @_;
    my $rows = @{$a};
    my $cols = @{$b->[0]};
    my @res = map { [(0) x $cols] } (0..$rows-1);
    for my $i (0..$rows-1) {
        for my $j (0..$cols-1) {
            my $c = 0;
            for my $k (0..$#{$b}) {
                $c += $a->[$i][$k] * $b->[$k][$j];
            }
            $res[$i][$j] = $c;
        }
    }
    \@res;
}

use Soda::Unittest::Utils qw(from_stdin);
use Soda::Unittest::TestWork qw(create);
use Soda::Unittest::Validators qw(for_array for_array2d);
# step [2]: setup function/return/arguments
my $work = create(\&matrix_multiply);
# $work->validator(sub { $_[0] vs $_[1]; return boolean });
# $work->compare_serial(1);
print $work->run(from_stdin());

