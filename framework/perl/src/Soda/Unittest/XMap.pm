package Soda::Unittest::XMap;
use strict;
use warnings FATAL => 'all';

sub new {
    my $class = shift;
    my $feature = shift;
    bless {
        feat => $feature,
        dict => {},
        size => 0
    }, $class;
}

sub _hash {
    my ($self, $key) = @_;
    $self->{feat}->hash($key);
}

sub _entry {
    my ($self, $key) = @_;
    my $h = $self->_hash($key);
    exists($self->{dict}->{$h})
        ? (grep { $self->{feat}->is_equal($key, $_->{key}) } @{$self->{dict}->{$h}})[0]
        : undef;
}

sub has {
    my ($self, $key) = @_;
    defined $self->_entry($key) ? 1 : 0;
}

sub get {
    my ($self, $key, $default_val) = @_;
    my $e = $self->_entry($key);
    $e ? $e->{value} : $default_val;
}


sub set {
    my ($self, $key, $value) = @_;
    my $e = $self->_entry($key);
    if (defined $e) {
        $e->{value} = $value;
    } else {
        my $h = $self->_hash($key);
        $self->{dict}->{$h} = [] unless exists $self->{dict}->{$h};
        push @{$self->{dict}->{$h}}, { key => $key, value => $value };
        ++$self->{size};
    }
}

sub delete {
    my ($self, $key) = @_;
    my $h = $self->_hash($key);
    if (!exists $self->{dict}->{$h}) {
        return;
    }
    my $entries = $self->{dict}->{$h};
    my ($index) = grep { $self->{feat}->is_equal($key, $entries->[$_]->{key}) } (0..$#{$entries});
    if (defined $index) {
        splice @{$self->{dict}->{$h}}, $index, 1;
    }
}

1;