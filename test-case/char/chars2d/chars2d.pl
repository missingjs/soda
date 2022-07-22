use strict;
use warnings;

# step [1]: implement solution function
# @param {string[][]} matrix
# @return {string[][]}
sub to_upper {
    my ($matrix) = @_;
    my $diff = ord('a') - ord('A');
    for my $i (0..scalar(@{$matrix})-1) {
        for my $j (0..scalar(@{$matrix->[0]})-1) {
            $matrix->[$i][$j] = chr(ord($matrix->[$i][$j]) - $diff);
        }
    }
    $matrix;
}

use Soda::Unittest::Utils qw(from_stdin);
use Soda::Unittest::TestWork qw(create);
# step [2]: setup function/return/arguments
my $work = create(\&to_upper);
# $work->validator(sub { $_[0] vs $_[1]; return boolean });
$work->compare_serial(1);
print $work->run(from_stdin());

