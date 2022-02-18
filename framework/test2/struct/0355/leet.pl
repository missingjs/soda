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
package Node;
sub new {
    my ($class, $tweets) = @_;
    bless {
        tweets => $tweets,
        index  => @$tweets - 1
    }, $class;
}
sub current {
    my $self = shift;
    $self->{tweets}->[$self->{index}];
}
sub is_end {
    my $self = shift;
    $self->{index} == -1;
}

package Twitter;
sub new_tweet {
    my ($id, $ts) = @_;
    { id => $id, ts => $ts };
}

my $limit = 10;

sub new {
    my $class = shift;
    bless {
        timestamp => 0,
        tweets    => {},
        follows   => {}
    }, $class;
}

# @param {number} user_id
# @param {number} tweet_id
# @return {void}
sub post_tweet {
    my ($self, $user_id, $tweet_id) = @_;
    $self->{tweets}->{$user_id} = [] unless exists $self->{tweets}->{$user_id};
    push @{$self->{tweets}->{$user_id}}, new_tweet($tweet_id, $self->next_timestamp);
}

# @param {number} user_id
# @return {number[]}
sub get_news_feed {
    my ($self, $user_id) = @_;
    my @s;
    if (defined $self->{follows}->{$user_id}) {
        @s = keys %{$self->{follows}->{$user_id}};
    }
    push @s, $user_id;

    my $pq = PriorityQueue->new(sub {
        $_[1]->current->{ts} - $_[0]->current->{ts}
    });
    for my $user (@s) {
        my $tq = $self->{tweets}->{$user} || [];
        if (@$tq) {
            $pq->push(Node->new($tq));
        }
    }

    my @res;
    my $i = 0;
    while ($i < $limit && $pq->size) {
        my $node = $pq->pop();
        push @res, $node->current->{id};
        --$node->{index};
        unless ($node->is_end) {
            $pq->push($node);
        }
        ++$i;
    }
    \@res;
}

# @param {number} follower_id
# @param {number} followee_id
# @return {void}
sub follow {
    my ($self, $follower_id, $followee_id) = @_;
    unless (defined $self->{follows}->{$follower_id}) {
        $self->{follows}->{$follower_id} = {};
    }
    $self->{follows}->{$follower_id}->{$followee_id} = 1;
}

# @param {number} follower_id
# @param {number} followee_id
# @return {void}
sub unfollow {
    my ($self, $follower_id, $followee_id) = @_;
    if (defined $self->{follows}->{$follower_id}) {
        delete($self->{follows}->{$follower_id}->{$followee_id});
    }
}

sub next_timestamp {
    my $self = shift;
    ++$self->{timestamp};
}

package main;
use Soda::Unittest::Utils qw(from_stdin);
use Soda::Unittest::TestWork qw(create for_struct);
use Soda::Unittest::Validators qw(for_array for_array2d);
# step [2]: setup function/return/arguments
# my $work = create(\&add);
my $work = for_struct(Twitter::);
# $work->validator(sub { $_[0] vs $_[1]; return boolean });
$work->compare_serial(1);
print $work->run(from_stdin());

