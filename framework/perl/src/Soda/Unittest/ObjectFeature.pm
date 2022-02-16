package Soda::Unittest::ObjectFeature;
use strict;
use warnings FATAL => 'all';

use JSON::PP;
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

our %factory_map = ();

# static
sub create {
    my ($type) = @_;
    if (exists $factory_map{$type}) {
        $factory_map{$type}->();
    } else {
        my $js = JSON::PP->new;
        $js->canonical(1);
        new(__PACKAGE__,
            sub { hash_code($_[0]) },
            sub { $js->encode($_[0]) eq $js->encode($_[1]) }
        );
    }
}

1;