package Soda::Unittest::ObjectConverter;
use strict;
use warnings FATAL => 'all';

use Soda::Leetcode::ListNode;

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

my %factory_map;

# static method
sub create {
    my ($type) = @_;
    if (exists $factory_map{$type}) {
        $factory_map{$type}->();
    } else {
        new(__PACKAGE__, sub { $_[0] }, sub { $_[0] });
    }
}

sub register_factory {
    my ($type, $parser, $serializer) = @_;
    $factory_map{$type} = sub {
        new (
            __PACKAGE__,
            sub { $parser->(@_) },
            sub { $serializer->(@_) }
        );
    };
}

BEGIN {
    %factory_map = ();
    register_factory('ListNode', \&Soda::Leetcode::ListNode::create, \&Soda::Leetcode::ListNode::dump);
    register_factory(
        'ListNode[]',
        sub {
            my $ds = shift;
            [map { Soda::Leetcode::ListNode::create($_) } @$ds];
        },
        sub {
            my $nodes = shift;
            [map { Soda::Leetcode::ListNode::dump($_) } @$nodes];
        }
    );
}

1;