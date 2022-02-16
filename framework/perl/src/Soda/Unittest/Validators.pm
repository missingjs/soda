package Soda::Unittest::Validators;
use strict;
use warnings FATAL => 'all';

use Exporter 5.57 'import';
use Soda::Unittest::ListFeatureFactory;
use Soda::Unittest::ObjectFeature;

our @EXPORT = qw(for_array for_array2d);

# static
sub for_array {
    my ($obj_type, $ordered) = @_;
    for_arr($ordered, Soda::Unittest::ObjectFeature::create($obj_type));
}

# static
sub for_array2d {
    my ($obj_type, $dim1_ordered, $dim2_ordered) = @_;

    my $elem_feat = Soda::Unittest::ObjectFeature::create($obj_type);
    my $d = $dim2_ordered
        ? Soda::Unittest::ListFeatureFactory::ordered($elem_feat)
        : Soda::Unittest::ListFeatureFactory::unordered($elem_feat);
    for_arr($dim1_ordered, $d);
}

# static
sub for_arr {
    my ($ordered, $elem_feat) = @_;
    my $ls_feat = $ordered
        ? Soda::Unittest::ListFeatureFactory::ordered($elem_feat)
        : Soda::Unittest::ListFeatureFactory::unordered($elem_feat);
    sub { my ($x, $y) = @_; $ls_feat->is_equal($x, $y) }
}

1;