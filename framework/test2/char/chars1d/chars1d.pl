use strict;
use warnings;

# step [1]: implement solution function
# @param {string[]} chars
# @return {string[]}
sub double_list {
    my ($chars) = @_;
    my @arr;
    push @arr, @{$chars};
    push @arr, @{$chars};
    \@arr;
}

use Soda::Unittest::Utils qw(from_stdin);
use Soda::Unittest::TestWork qw(create);
# step [2]: setup function/return/arguments
my $work = create(\&double_list);
# $work->validator(sub { $_[0] vs $_[1]; return boolean });
$work->compare_serial(1);
print $work->run(from_stdin());

