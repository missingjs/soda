package Soda::DS::PriorityQueue;
use strict;
use warnings FATAL => 'all';

use Array::Heap;

sub new {
    my ($class, $cmp) = @_;
    $cmp = sub { $_[0] <=> $_[1] } unless defined $cmp;
    bless {
        comp => $cmp,
        heap => []
    }, $class;
}

sub size {
    my $self = shift;
    @{$self->{heap}};
}

sub empty {
    my $self = shift;
    $self->size == 0;
}

sub push {
    my ($self, $elem) = @_;
    push_heap_cmp { $self->{comp}->($a, $b) } @{$self->{heap}}, $elem;
}

sub pop {
    my $self = shift;
    pop_heap_cmp { $self->{comp}->($a, $b) } @{$self->{heap}};
}

sub top {
    my $self = shift;
    $self->{heap}->[0];
}

1;