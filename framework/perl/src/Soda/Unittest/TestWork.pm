package Soda::Unittest::TestWork;
use strict;
use warnings FATAL => 'all';

use Data::Dumper;
use Exporter 5.57 'import';
use JSON::PP;
use Sub::Identify;

use Soda::Unittest::Utils qw/micro_time parse_arguments/;
use Soda::Unittest::ObjectConverter;
use Soda::Unittest::ObjectFeature;

our @EXPORT = qw(create);

sub new {
    my $class = shift;
    my ($func, $type_hints) = @_;
    my $return_type = $type_hints->[$#{$type_hints}];
    my @arg_types = $type_hints->@[0 .. ($#{$type_hints} - 1)];
    bless {
        func           => $func,
        type_hints     => $type_hints,
        argument_types => \@arg_types,
        return_type    => $return_type,
        compare_serial => 0,
        validator      => undef,
        arguments      => undef
    }, $class;
}

sub create {
    my ($func, $type_hints) = @_;
    # print STDERR "func: ", Sub::Identify::sub_name($func), "\n";
    # my $packname = __PACKAGE__;
    # print STDERR "package: $packname\n";
    new(__PACKAGE__, $func, $type_hints);
}

sub run {
    my $js = JSON::PP->new;
    $js->canonical(1);
    my ($self, $text) = @_;
    my $input = $js->decode($text);

    my $args = parse_arguments($self->{argument_types}, $input->{args});
    $self->{arguments} = $args;

    my $start_time = micro_time;
    my $result = $self->{func}->(@{$args});
    my $end_time = micro_time;
    my $elapse_millis = ($end_time - $start_time) / 1000;

    my $ret_type = $self->{return_type};
    if ($ret_type eq 'void' || $ret_type eq 'Void') {
        $ret_type = $self->{argument_types}->[0];
        $result = $args->[0];
    }

    my $res_conv = Soda::Unittest::ObjectConverter::create($ret_type);
    my $serial_result = $res_conv->to_json_serializable($result);
    my $resp = {
        id     => $input->{id},
        result => $serial_result,
        elapse => $elapse_millis
    };

    my $success = 1;
    if (defined($input->{expected})) {
        if ($self->{compare_serial} && !defined($self->{validator})) {
            my $as = $js->encode($input->{expected});
            my $bs = $js->encode($serial_result);
            $success = $as eq $bs;
        } else {
            my $expect = $res_conv->from_json_serializable($input->{expected});
            if (defined($self->{validator})) {
                $success = $self->{validator}->(($expect, $result));
            } else {
                $success = Soda::Unittest::ObjectFeature::create($ret_type)->is_equal($expect, $result);
            }
        }
    }
    $resp->{success} = $success ? \1 : \0;

    $js->encode($resp);
}

sub compare_serial {
    my ($self, $val) = @_;
    $self->{compare_serial} = $val if defined($val);
}

sub validator {
    my ($self, $vali) = @_;
    $self->{validator} = $vali if defined($vali);
}

1;