use strict;
use warnings;
use List::Util qw(min max);
use Soda::DS::PriorityQueue;
use Soda::Leetcode::ListNode;
use Soda::Leetcode::NestedInteger;
use Soda::Leetcode::TreeNode;

*ListNode:: = \*Soda::Leetcode::ListNode::;
*NestedInteger:: = \*Soda::Leetcode::NestedInteger::;
*PriorityQueue:: = \*Soda::DS::PriorityQueue::;
*TreeNode:: = \*Soda::Leetcode::TreeNode::;

# step [1]: implement solution function
package CBTInserter;
# @param {TreeNode} root
sub new {
    my ($class, $root) = @_;
    my @qu;
    if (defined $root) {
        push @qu, $root;
        while (@qu) {
            my $node = $qu[0];
            last unless $node->left;
            push @qu, $node->left;
            last unless $node->right;
            push @qu, $node->right;
            shift @qu;
        }
    }
    bless {
        qu => \@qu,
        root => $root
    }, $class;
}

# @param {number} val
# @return {number}
sub insert {
    my ($self, $val) = @_;
    my $node = TreeNode->new($val);
    my $head = $self->{qu}->[0];
    push @{$self->{qu}}, $node;
    if (defined $head->left) {
        $head->set_right($node);
        shift @{$self->{qu}};
    } else {
        $head->set_left($node);
    }
    $head->val;
}

# @return {TreeNode}
sub get_root {
    shift->{root};
}

package main;
use Soda::Unittest::Utils qw(from_stdin);
use Soda::Unittest::TestWork qw(create for_struct);
use Soda::Unittest::Validators qw(for_array for_array2d);
# step [2]: setup function/return/arguments
# my $work = create(\&add);
my $work = for_struct(CBTInserter::);
# $work->validator(sub { $_[0] vs $_[1]; return boolean });
$work->compare_serial(1);
print $work->run(from_stdin());

