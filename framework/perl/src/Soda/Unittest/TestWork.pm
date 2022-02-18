package Soda::Unittest::TestWork;
use strict;
use warnings FATAL => 'all';

use Data::Dumper;
use Exporter 5.57 'import';
use JSON::PP;
use Sub::Identify;

use Soda::Unittest::Utils qw/
    function_type_hints
    method_type_hints
    micro_time
    parse_arguments
    underscore
/;
use Soda::Unittest::ObjectConverter;
use Soda::Unittest::ObjectFeature;

our @EXPORT = qw(create for_struct);

sub new {
    my $class = shift;
    my ($func, $type_hints) = @_;
    my $return_type = $type_hints->[-1];
    my @arg_types = $type_hints->@[0 .. ($#$type_hints - 1)];
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
    my ($func) = @_;
    my $type_hints = function_type_hints($0, Sub::Identify::sub_name($func));
    new(__PACKAGE__, $func, $type_hints);
}

sub for_struct {
    my $classname = shift;
    my $method_hints = method_type_hints($0, $classname);
    my $tester_func = sub {
        my ($operations, $parameters) = @_;
        # hints of constructor has not return type
        my $ctor_args = parse_arguments($method_hints->{new}, $parameters->[0]);
        my $obj = $classname->new(@$ctor_args);
        my @res = (undef);
        for my $i (1..$#$parameters) {
            my $method_name = $operations->[$i];
            unless (exists $method_hints->{$method_name}) {
                my $ud = underscore($method_name);
                if (exists $method_hints->{$ud}) {
                    $method_name = $ud;
                } else {
                    die "no type hints for method $method_name and $ud";
                }
            }
            my $hints = $method_hints->{$method_name};
            my $arg_types = [$hints->@[0..$#$hints-1]];
            my $ret_type = $hints->[-1];
            my $args = parse_arguments($arg_types, $parameters->[$i]);
            my $r = $obj->$method_name(@$args);
            if ($ret_type ne 'void' && $ret_type ne 'Void') {
                push @res, Soda::Unittest::ObjectConverter::create($ret_type)->to_json_serializable($r);
            } else {
                push @res, undef;
            }
        }
        \@res;
    };
    my @func_hints = qw/string[] object[] object[]/;
    new(__PACKAGE__, $tester_func, \@func_hints);
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

sub arguments {
    shift()->{arguments};
}

1;