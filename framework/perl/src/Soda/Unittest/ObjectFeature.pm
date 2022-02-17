package Soda::Unittest::ObjectFeature;
use strict;
use warnings FATAL => 'all';

use JSON::PP;
use Soda::Unittest::ListFeatureFactory;
use Soda::Unittest::Utils qw(hash_code);

sub new {
    my $class = shift;
    my ($hash_func, $equal_func) = @_;
    bless {
        hash_func  => $hash_func,
        equal_func => $equal_func
    }, $class;
}

sub hash {
    my ($self, $obj) = @_;
    $self->{hash_func}->(($obj));
}

sub is_equal {
    my ($self, $x, $y) = @_;
    $self->{equal_func}->(($x, $y));
}

my %factory_map;

# static
sub create {
    my $type = shift;
    if (exists $factory_map{$type}) {
        $factory_map{$type}->();
    } else {
        my $js = JSON::PP->new;
        $js->canonical(1);
        new(__PACKAGE__,
            \&hash_code,
            sub { $js->encode($_[0]) eq $js->encode($_[1]) }
        );
    }
}

sub register_factory {
    my ($type, $hash_func, $equal_func) = @_;
    $factory_map{$type} = sub {
        new (__PACKAGE__, $hash_func, $equal_func);
    };
}

sub reg_fact {
    my ($type, $factory) = @_;
    $factory_map{$type} = $factory;
}

BEGIN {
    %factory_map = ();
    register_factory('number', \&hash_code, sub { abs($_[0] - $_[1]) < 1e-6 });
    reg_fact('number[]', sub { Soda::Unittest::ListFeatureFactory::ordered(create('number')) });
    reg_fact('number[][]', sub { Soda::Unittest::ListFeatureFactory::ordered(create('number[]')) });
}

1;