use strict;
use warnings;

# @param {number} a
# @param {number} b
# @return {number}
sub add {
    my ($x, $y) = @_;
    return $x + $y;
}

use Soda::Unittest::Utils qw(from_stdin);
use Soda::Unittest::TestWork qw(create);
my $work = create(\&add, ['number', 'number', 'number']);
print $work->run(from_stdin());

