use strict;
use warnings;

# step [1]: implement solution function
# @param {string[][]} equations
# @param {number[]} values
# @param {string[][]} queries
# @return {number[]}
sub calc_equation {
    my ($equations, $values, $queries) = @_;
    my $index_map = get_index_map($equations);
    my $N = keys %$index_map;
    my @table = map { [(-1) x $N] } (0..$N-1);

    for my $k (0..$#$values) {
        my $p = $equations->[$k];
        my $i = $index_map->{$p->[0]};
        my $j = $index_map->{$p->[1]};
        $table[$i][$j] = $values->[$k];
        $table[$j][$i] = 1 / $values->[$k];
    }

    my @res = (0) x @$queries;
    my @visited = (0) x $N;
    for my $i (0..$#res) {
        my ($a, $b) = @{$queries->[$i]};
        my $ai = $index_map->{$a};
        my $bi = $index_map->{$b};
        if (!defined($ai) || !defined($bi)) {
            $res[$i] = -1;
            next;
        }
        if ($ai == $bi) {
            $res[$i] = 1;
            next;
        }
        @visited = (0) x $N;
        $res[$i] = dfs($ai, $bi, \@table, \@visited);
    }
    \@res;
}

sub get_index_map {
    my $eqs = shift;
    my %imap;
    for my $e (@$eqs) {
        my ($a, $b) = @$e;
        $imap{$a} = keys %imap unless defined $imap{$a};
        $imap{$b} = keys %imap unless defined $imap{$b};
    }
    \%imap;
}

sub dfs {
    my ($ai, $bi, $table, $visited) = @_;
    return $table->[$ai][$bi] if $table->[$ai][$bi] >= 0;

    $visited->[$ai] = 1;
    my $res = -1;
    for my $adj (0..$#$table) {
        if ($table->[$ai][$adj] >= 0 && !$visited->[$adj]) {
            my $v = dfs($adj, $bi, $table, $visited);
            if ($v >= 0) {
                $res = $table->[$ai][$adj] * $v;
                last;
            }
        }
    }
    $table->[$ai][$bi] = $res;
    $table->[$bi][$ai] = 1 / $res;
    $res;
}

use Soda::Unittest::Utils qw(from_stdin);
use Soda::Unittest::TestWork qw(create);
use Soda::Unittest::Validators qw(for_array for_array2d);
# step [2]: setup function/return/arguments
my $work = create(\&calc_equation);
# $work->validator(sub { $_[0] vs $_[1]; return boolean });
# $work->compare_serial(1);
print $work->run(from_stdin());

