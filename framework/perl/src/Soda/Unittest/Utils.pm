package Soda::Unittest::Utils;
use strict;
use warnings FATAL => 'all';

use Digest::SHA qw(sha1);
use Exporter 5.57 'import';
use JSON::PP;
use Time::HiRes qw/gettimeofday/;

use Soda::Unittest::ObjectConverter;

# set the version for version checking
our $VERSION     = '1.00';

our @EXPORT = qw(
    from_stdin
    function_type_hints
    hash_code
    method_type_hints
    micro_time
    parse_arguments
    underscore
);

sub from_stdin {
    join("", <STDIN>);
}

sub micro_time {
    my ($seconds, $micros) = gettimeofday;
    return $seconds * 1e6 + $micros;
}

sub parse_arguments {
    my ($arg_types, $raw_args) = @_;
    my @args = map {
        Soda::Unittest::ObjectConverter::create($arg_types->[$_])->from_json_serializable($raw_args->[$_])
    } 0..$#{$arg_types};
    return \@args;
}

# return 32bit positive signed integer
sub hash_code {
    my ($obj) = @_;
    my $js = JSON::PP->new;
    $js->canonical(1);
    my $bytes = sha1($js->encode($obj));
    my $hash = 0;
    for my $i (0..length($bytes)-1) {
        my $n = ord(substr($bytes, $i, 1));
        $hash = $hash * 31 + $n;
        $hash &= 0x7fffffff;
    }
    $hash;
}

sub function_type_hints {
    my ($file_path, $func_name) = @_;
    my @lines;
    open my $info, $file_path or die "Could not open $file_path: $!";
    while (my $line = <$info>) {
        chomp $line;
        if ($line =~ /^sub \Q$func_name\E \{/) {
            last;
        }
        if (length($line) > 0) {
            push @lines, $line;
        }
    }
    close $info;
    parse_type_hints(\@lines);
}

sub parse_type_hints {
    my $ref_lines = shift;
    my @lines = @$ref_lines;
    my @type_hints;
    my $ret_type = 'void';
    while (@lines > 0) {
        my $text = pop @lines;
        if ($text =~ /^#\s+\@param\s+{(?<arg_type>.*)}/) {
            push @type_hints, $+{arg_type};
        } elsif ($text =~ /^#\s+\@return\s+{(?<return_type>.*)}/) {
            $ret_type = $+{return_type};
        } else {
            last;
        }
    }
    push @type_hints, $ret_type;
    \@type_hints;
}

sub method_type_hints {
    my ($file_path, $classname) = @_;

    open my $fp, $file_path or die "Could not open $file_path: $!";
    while (my $line = <$fp>) {
        chomp $line;
        if ($line =~ /^package \Q$classname\E;/) {
            last;
        }
    }

    my %hints_map;
    my @buf;
    while (my $line = <$fp>) {
        chomp $line;
        if ($line =~ /^sub (?<method_name>\w+)/) {
            $hints_map{$+{method_name}} = parse_type_hints(\@buf);
            @buf = ();
        } else {
            push @buf, $line;
        }
    }
    close $fp;

    $hints_map{new} = [] unless exists $hints_map{new};
    \%hints_map;
}

sub underscore {
    my $s = shift;
    $s =~ s/::/\//;
    $s =~ s/([A-Z]+)([A-Z][a-z])/$1_$2/;
    $s =~ s/([a-z\d])([A-Z])/$1_$2/;
    $s =~ s/-/_/;
    lc($s);
}

1;