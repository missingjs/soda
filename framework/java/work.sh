#!/bin/bash

usage()
{
    local cmd=$(basename $0)
    cat>&2 << EOF
usage:
    $cmd <command> [options]

options:
    new <testname>
        create source file with name <testname>.java

    source <testname>
        show source file name

    make <testname> 
        compile test case

    run <testname> [--remote]
        run test case

    clean <testname>
        remove all class files under current directory

    remote-setup
        reset remote server, drop old work class

EOF
    exit 1
}

self_dir=$(cd $(dirname $0) && pwd)
framework_dir=$(dirname $self_dir)
source $framework_dir/common/bashlib.sh || exit

[ -e $self_dir/setup_env.sh ] || cp $self_dir/_setup_env.sh $self_dir/setup_env.sh
source $self_dir/setup_env.sh || exit

cmd=$1
[ -z $cmd ] && usage

testname=$2
assert_testname() {
    [ -z $testname ] && usage
}
testname=$(python3 -c "print('$testname'.capitalize())")
srcfile=${testname}.java
output_dir=./java
jarfile=work.jar

remote_run()
{
    # $input must be in valid json format
    local input="$(</dev/stdin)"
    local classname=$1
    local pathkey=$(cd $output_dir && pwd)

local pycode=$(cat << EOF
import json
info = {
  "key": "$pathkey",
  "bootClass": "$classname",
  "testCase" : """$input"""
}
print(json.dumps(info))
EOF
)

    local post_content="$(python3 -c "$pycode")"
    local url="http://localhost:$server_port/soda/java/work"
    curl --connect-timeout 2 -X POST -d "$post_content" -s "$url"
}

remote_setup()
{
    local echo_url="http://localhost:$server_port/soda/java/echo?a=b"
    curl --connect-timeout 2 -s "$echo_url" >/dev/null \
        || { echo "server not open" >&2; exit 2; }

    local pathkey=$(cd $output_dir && pwd)
    local boot_url="http://localhost:$server_port/soda/java/bootstrap"

    local_md5="$(md5sum $output_dir/$jarfile | awk '{print $1}')"
    remote_md5="$(curl --connect-timeout 2 -s "${boot_url}?key=$pathkey&format=text")"

    if [ "$local_md5" != "$remote_md5" ]; then  
        curl --connect-timeout 2 -s -f -X POST \
            -F "key=$pathkey" \
            -F "jar=@$output_dir/$jarfile" \
            "$boot_url" >/dev/null
    fi
}

new_project()
{
    assert_testname
    target_file=$srcfile
    template_file=$self_dir/src/main/java/soda/unittest/__Bootstrap__.java
    create_source_file $template_file $target_file
    classname=$testname
    echo "import soda.unittest.*;" > ${classname}.tmp
    cat $target_file \
        | grep -v '^package ' \
        | sed "s/__Bootstrap__/$classname/g" \
        >> ${classname}.tmp
    mv ${classname}.tmp $target_file
}

make_project()
{
    assert_testname
    [ -e $output_dir ] || mkdir $output_dir
    classfile=$output_dir/${testname}.class
    if [[ ! -e $classfile ]] || [[ $srcfile -nt $classfile ]]; then
        assert_framework
        tmpdir=$(mktemp -d)
        code='s/GenericTestWork.create(\w+)\(new (.*)\(\)::(.*)\)/GenericTestWork.create\1(\2.class, "\3", new \2()::\3)/g'
        perl -pe "$code" $srcfile > $tmpdir/$srcfile
        cp $tmpdir/$srcfile $output_dir/${testname}__gen.java

        [ -e $output_dir/$jarfile ] && rm $output_dir/$jarfile

        echo "Compiling $srcfile ..."
        classpath=$(get_classpath)
        set -x
        javac -d $output_dir -cp $classpath $SODA_JAVA_COMPILE_OPTION $tmpdir/$srcfile || exit
        set +x
        rm -rf $tmpdir
        echo "Compile $srcfile OK"
        (cd $output_dir && jar cf $jarfile *.class)
    fi
}

run_project()
{
    local run_mode=$1
    assert_testname
    classname=$testname
    if [ "$run_mode" == "--remote" ]; then
        remote_run $classname <&0
    else
        assert_framework
        java -cp $(get_classpath):$output_dir $classname
    fi
}

case $cmd in
    new)
        new_project
        ;;
    source)
        echo $srcfile
        ;;
    make)
        make_project
        ;;
    run)
        shift; shift;
        run_project "$@"
        ;;
    clean)
        assert_testname
        [ -e $output_dir ] && rm -rfv $output_dir || true
        ;;
    remote-setup)
        remote_setup
        ;;
    *)
        usage
        ;;
esac

