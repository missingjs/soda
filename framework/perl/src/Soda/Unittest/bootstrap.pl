use strict;
use warnings;

# step [1]: implement solution function
# @param {number} a
# @param {number} b
# @return {number}
sub add {
    my ($x, $y) = @_;
    return $x + $y;
}

use Soda::Unittest::Utils qw(from_stdin);
use Soda::Unittest::TestWork qw(create);
use Soda::Unittest::Validators qw(for_array for_array2d);
# step [2]: setup function/return/arguments
my $work = create(\&add);
# $work->validator(sub { $_[0] vs $_[1]; return boolean });
$work->compare_serial(1);
print $work->run(from_stdin());

