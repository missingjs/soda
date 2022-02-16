package Soda::Unittest::Utils;
use strict;
use warnings FATAL => 'all';

use Exporter 5.57 'import';
use Time::HiRes qw/gettimeofday/;

# set the version for version checking
our $VERSION     = '1.00';

our @EXPORT = qw(from_stdin micro_time);

sub from_stdin {
    join("", <STDIN>);
}

sub micro_time {
    my ($seconds, $micros) = gettimeofday;
    return $seconds * 1e6 + $micros;
}

1;