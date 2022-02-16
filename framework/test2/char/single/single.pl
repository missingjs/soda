use strict;
use warnings;

# step [1]: implement solution function
# @param {string} ch
# @return {string}
sub next_char {
    my ($ch) = @_;
    chr(ord($ch) + 1);
}

use Soda::Unittest::Utils qw(from_stdin);
use Soda::Unittest::TestWork qw(create);
# step [2]: setup function/return/arguments
my $work = create(\&next_char);
# $work->validator(sub { $_[0] vs $_[1]; return boolean });
$work->compare_serial(1);
print $work->run(from_stdin());

