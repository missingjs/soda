use strict;
use warnings;
use List::Util qw(min max);
use Soda::DS::PriorityQueue;
use Soda::Leetcode::ListNode;
use Soda::Leetcode::NestedInteger;

*ListNode:: = \*Soda::Leetcode::ListNode::;
*NestedInteger:: = \*Soda::Leetcode::NestedInteger::;
*PriorityQueue:: = \*Soda::DS::PriorityQueue::;

# step [1]: implement solution function
package Logger;
my $limit = 10;

sub new {
    my $class = shift;
    bless {
        msg_map => {},
        last_ts => -$limit
    }, $class;
}

# @param {number} timestamp
# @param {string} message
# @return {boolean}
sub should_print_message {
    my ($self, $timestamp, $message) = @_;
    my $T = $self->{last_ts};
    $self->{last_ts} = $timestamp;

    if ($timestamp - $T >= $limit) {
        $self->{msg_map} = {$message => $timestamp};
        return 1;
    }

    my $ts = $self->{msg_map}->{$message};
    $ts = $timestamp - $limit unless defined $ts;
    if ($timestamp - $ts < $limit) {
        return 0;
    }

    $self->{msg_map}->{$message} = $timestamp;
    return 1;
}

package main;
use Soda::Unittest::Utils qw(from_stdin);
use Soda::Unittest::TestWork qw(create for_struct);
use Soda::Unittest::Validators qw(for_array for_array2d);
# step [2]: setup function/return/arguments
# my $work = create(\&add);
my $work = for_struct(Logger::);
# $work->validator(sub { $_[0] vs $_[1]; return boolean });
$work->compare_serial(1);
print $work->run(from_stdin());

