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

our @EXPORT = qw(from_stdin function_type_hints micro_time parse_arguments hash_code);

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

    my $ret_type = 'void';
    my @arg_types;
    while (@lines > 0) {
        my $text = pop @lines;
        if ($text =~ /^#\s+\@param\s+{(?<arg_type>.*)}/) {
            push @arg_types, $+{arg_type};
        } elsif ($text =~ /^#\s+\@return\s+{(?<return_type>.*)}/) {
            $ret_type = $+{return_type};
        } else {
            last;
        }
    }
    push @arg_types, $ret_type;
    \@arg_types;
}

1;