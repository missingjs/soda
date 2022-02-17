package Soda::Leetcode::NestedInteger;
use strict;
use warnings FATAL => 'all';

sub new {
    my $class = shift;
    my $value = shift;
    bless {
        is_atomic => defined($value),
        elements  => [],
        value     => $value
    }, $class;
}

sub is_integer {
    my $self = shift;
    $self->{is_atomic};
}

sub get_integer {
    my $self = shift;
    $self->{value};
}

sub set_integer {
    my ($self, $value) = @_;
    $self->{value} = $value if $self->is_integer;
}

sub add {
    my ($self, $elem) = @_;
    push(@{$self->{elements}}, $elem) unless $self->is_integer;
}

sub get_list {
    my $self = shift;
    [@{$self->{elements}}];
}

# static
sub parse {
    my $d = shift;
    return new(__PACKAGE__, $d) if !ref($d);
    my $ni = new(__PACKAGE__);
    for my $e (@$d) {
        $ni->add(parse($e));
    }
    $ni;
}

# static
sub serialize {
    my $ni = shift;
    $ni->is_integer ? $ni->get_integer : [map { serialize($_) } @{$ni->get_list}];
}

1;