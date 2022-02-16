package Soda::Unittest::ObjectConverter;
use strict;
use warnings FATAL => 'all';

sub new {
    my $class = shift;
    my ($parser, $serializer) = @_;
    bless {
        parser => $parser,
        serializer => $serializer
    }, $class;
}

sub from_json_serializable {
    my ($self, $js) = @_;
    $self->{parser}->(($js));
}

sub to_json_serializable {
    my ($self, $obj) = @_;
    $self->{serializer}->(($obj));
}

our %factory_map = ();

# static method
sub create {
    my ($type) = @_;
    if (exists $factory_map{$type}) {
        $factory_map{$type}->();
    } else {
        new(__PACKAGE__, sub { $_[0] }, sub { $_[0] });
    }
}

1;