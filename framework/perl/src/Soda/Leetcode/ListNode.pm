package Soda::Leetcode::ListNode;
use strict;
use warnings FATAL => 'all';

sub new {
    my $class = shift;
    my $val = shift;
    bless {
        val   => $val || 0,
        next  => undef
    }, $class;
}

sub val {
    my $self = shift;
    $self->{val};
}

sub set_val {
    my ($self, $p) = @_;
    $self->{val} = $p;
}

sub next {
    my $self = shift;
    $self->{next};
}

sub set_next {
    my ($self, $p) = @_;
    $self->{next} = $p;
}

# static
sub create {
    my $data = shift;
    my $head = new(__PACKAGE__);
    my $tail = $head;
    for my $val (@$data) {
        my $node = new(__PACKAGE__, $val);
        $tail->set_next($node);
        $tail = $node;
    }
    $head->next;
}

# static
sub dump {
    my $head = shift;
    my @list;
    while (defined $head) {
        push @list, $head->val;
        $head = $head->next;
    }
    \@list;
}

1;