package Soda::Unittest::ListFeatureFactory;
use strict;
use warnings FATAL => 'all';

use Soda::Unittest::ObjectFeature;
use Soda::Unittest::XMap;

# static
sub ordered {
    my $elem_feat = shift;
    my $hash_func = sub {
        my $obj = shift;
        my $res = 0;
        for my $e (@{$obj}) {
            my $h = $elem_feat->hash($e);
            $res = $res * 133 + $h;
            $res &= 0x7fffffff;
        }
        return $res;
    };
    my $equal_func = sub {
        my ($x, $y) = @_;
        if (@{$x} != @{$y}) {
            return 0;
        }
        my ($index) = grep { !$elem_feat->is_equal($x->[$_], $y->[$_]); } (0..$#{$x});
        return !defined $index;
    };
    return Soda::Unittest::ObjectFeature->new($hash_func, $equal_func);
}

# static
sub unordered {
    my $elem_feat = shift;
    my $hash = sub {
        my $obj = shift;
        my $res = 0;
        my $hash_arr = map { $elem_feat->hash($_) } $obj;
        for my $h (sort @{$hash_arr}) {
            $res = $res * 133 + $h;
            $res &= 0x7fffffff;
        }
        $res;
    };
    my $is_equal = sub {
        my ($x, $y) = @_;
        if (@{$x} != @{$y}) {
            return 0;
        }
        my $xmap = Soda::Unittest::XMap->new($elem_feat);
        for my $e (@{$x}) {
            $xmap->set($e, $xmap->get($e, 0) + 1);
        }
        for my $e (@{$y}) {
            return 0 unless $xmap->has($e);
            my $c = $xmap->get($e) - 1;
            if ($c == 0) {
                $xmap->delete($e);
            } else {
                $xmap->set($e, $c);
            }
        }
        return 1;
    };
    return Soda::Unittest::ObjectFeature->new($hash, $is_equal);
}

1;