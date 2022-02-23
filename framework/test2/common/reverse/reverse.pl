use strict;
use warnings;

# step [1]: implement solution function
# @param {string} s
# @return {string}
sub reverse_vowels {
    my $s = shift;
    my @isv = (0) x 128;
    my $vowels = "aeiouAEIOU";
    for my $i (0..length($vowels)-1) {
        my $ch = substr($vowels, $i, 1);
        $isv[ord($ch)] = 1;
    }
    my @buf = split('', $s);
    my ($i, $j) = (0, length($s)-1);
    while ($i < $j) {
        while ($i < $j && !$isv[ord($buf[$i])]) {
            ++$i;
        }
        while ($i < $j && !$isv[ord($buf[$j])]) {
            --$j;
        }
        if ($i < $j) {
            ($buf[$i], $buf[$j]) = ($buf[$j], $buf[$i]);
            ++$i;
            --$j;
        }
    }
    return join('', @buf);
}

use Soda::Unittest::Utils qw(from_stdin);
use Soda::Unittest::TestWork qw(create);
use Soda::Unittest::Validators qw(for_array for_array2d);
# step [2]: setup function/return/arguments
my $work = create(\&reverse_vowels);
# $work->validator(sub { $_[0] vs $_[1]; return boolean });
$work->compare_serial(1);
print $work->run(from_stdin());

