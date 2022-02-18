package Soda::Leetcode::TreeNode;
use strict;
use warnings FATAL => 'all';

sub new {
    my ($class, $val, $left, $right) = @_;
    bless {
        val   => $val,
        left  => $left,
        right => $right
    }, $class;
}

sub val {
    shift()->{val};
}

sub set_val {
    my ($self, $val) = @_;
    $self->{val} = $val;
}

sub left {
    shift()->{left};
}

sub set_left {
    my ($self, $left) = @_;
    $self->{left} = $left;
}

sub right {
    shift()->{right};
}

sub set_right {
    my ($self, $right) = @_;
    $self->{right} = $right;
}

sub create {
    my $data = shift;
    return undef unless @$data;

    my $root = new(__PACKAGE__, $data->[0]);
    my @qu = ($root);
    for (my $index = 1; $index < @$data; ) {
        my $node = shift @qu;
        if (defined $data->[$index]) {
            $node->set_left(new(__PACKAGE__, $data->[$index]));
            push @qu, $node->left;
        }
        if (++$index == @$data) {
            last;
        }
        if (defined $data->[$index]) {
            $node->set_right(new(__PACKAGE__, $data->[$index]));
            push @qu, $node->right;
        }
        ++$index;
    }
    $root;
}

sub dump {
    my $root = shift;
    return [] unless defined $root;

    my @res;
    my @qu = ($root);
    while (@qu) {
        my $node = shift @qu;
        if (defined $node) {
            push @res, $node->val;
            push @qu, $node->left;
            push @qu, $node->right;
        } else {
            push @res, undef;
        }
    }
    until (defined $res[-1]) {
        pop @res;
    }
    \@res;
}

1;