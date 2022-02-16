use strict;
use warnings;

# step [1]: implement solution function
# @param {number[]} nums1
# @param {number[]} nums2
# @return {number[]}
sub intersection {
    my ($nums1, $nums2) = @_;
    if (@{$nums1} > @{$nums2}) {
        return intersection($nums2, $nums1);
    }
    my %mset;
    my %res;
    for my $n (@{$nums1}) {
        $mset{$n} = 1;
    }
    for my $b (@{$nums2}) {
        $res{$b} = 1 if exists $mset{$b};
    }
    [map { int $_ } keys %res];
}

use Soda::Unittest::Utils qw(from_stdin);
use Soda::Unittest::TestWork qw(create);
use Soda::Unittest::Validators qw(for_array for_array2d);
# step [2]: setup function/return/arguments
my $work = create(\&intersection);
$work->validator(for_array('number', 0));
$work->compare_serial(1);
print $work->run(from_stdin());

