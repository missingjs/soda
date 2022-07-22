use strict;
use warnings;
use List::Util qw(min max);
use Soda::Leetcode::ListNode;
use Soda::Leetcode::NestedInteger;

*ListNode:: = \*Soda::Leetcode::ListNode::;
*NestedInteger:: = \*Soda::Leetcode::NestedInteger::;

# step [1]: implement solution function

my $p = 0;
# @param {String} s
# @return {NestedInteger}
sub deserialize {
    my $s = shift;
    $p = 0;
    parse($s);
}

sub ch {
    my ($s, $index) = @_;
    substr($s, $index, 1);
}

sub parse {
    my $s = shift;
    if (substr($s, $p, 1) eq '[') {
        ++$p;
        my $root = NestedInteger->new;
        until (ch($s,$p) eq ']') {
            $root->add(parse($s));
            if (ch($s,$p) eq ',') {
                ++$p;
            }
        }
        ++$p;
        return $root;
    }

    my $negative = 0;
    if (ch($s,$p) eq '-') {
        ++$p;
        $negative = 1;
    }

    my $value = 0;
    while ($p < length($s) && ch($s,$p) ge '0' && ch($s,$p) le '9') {
        $value = $value * 10 + ord(ch($s,$p)) - ord('0');
        ++$p;
    }

    $value = -$value if $negative;
    NestedInteger->new($value);
}

use Soda::Unittest::Utils qw(from_stdin);
use Soda::Unittest::TestWork qw(create);
use Soda::Unittest::Validators qw(for_array for_array2d);
# step [2]: setup function/return/arguments
my $work = create(\&deserialize);
# $work->validator(sub { $_[0] vs $_[1]; return boolean });
$work->compare_serial(1);
print $work->run(from_stdin());

