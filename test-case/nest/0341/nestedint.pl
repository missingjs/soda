use strict;
use warnings;
use Soda::Leetcode::ListNode;
use Soda::Leetcode::NestedInteger;

*ListNode:: = \*Soda::Leetcode::ListNode::;
*NestedInteger:: = \*Soda::Leetcode::NestedInteger::;

# step [1]: implement solution function
# @param {NestedInteger[]} n
# @return {Integer[]}
sub flat_nested {
    my $ni_list = shift;
    my $iter = NestedIterator->new($ni_list);
    my @res;
    while ($iter->has_next) {
        push @res, $iter->next;
    }
    \@res;
}

package Node;
sub new {
    my ($class, $ni_list) = @_;
    bless {
        ni_list => $ni_list,
        index   => 0
    }, $class;
}
sub is_end {
    my $self = shift;
    $self->{index} >= @{$self->{ni_list}};
}
sub value {
    my $self = shift;
    $self->current->get_integer;
}
sub current {
    my $self = shift;
    $self->{ni_list}->[$self->{index}];
}

package NestedIterator;
sub new {
    my ($class, $nested_list) = @_;
    my $self = {
        stk => [Node->new($nested_list)]
    };
    bless $self, $class;
    $self->locate();
    $self;
}
sub locate {
    my $self = shift;
    my $stk = $self->{stk};
    while (@$stk) {
        if ($stk->[-1]->is_end) {
            pop @$stk;
            ++$stk->[-1]->{index} if @$stk;
        } elsif ($stk->[-1]->current->is_integer) {
            last;
        } else {
            push @$stk, Node->new($stk->[-1]->current->get_list);
        }
    }
}
sub next {
    my $self = shift;
    my $stk = $self->{stk};
    my $value = $stk->[-1]->value;
    ++$stk->[-1]->{index};
    $self->locate();
    $value;
}
sub has_next {
    my $self = shift;
    my $stk = $self->{stk};
    @$stk && !$stk->[-1]->is_end;
}

package main;

use Soda::Unittest::Utils qw(from_stdin);
use Soda::Unittest::TestWork qw(create);
use Soda::Unittest::Validators qw(for_array for_array2d);
# step [2]: setup function/return/arguments
my $work = create(\&flat_nested);
# $work->validator(sub { $_[0] vs $_[1]; return boolean });
$work->compare_serial(1);
print $work->run(from_stdin());

