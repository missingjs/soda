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

EOF
    exit 1
}

self_dir=$(cd $(dirname $0) && pwd)
framework_dir=$(dirname $self_dir)
source $framework_dir/common/bashlib.sh || exit

[ -e $self_dir/setup_env.sh ] || cp $self_dir/_setup_env.sh $self_dir/setup_env.sh
source $self_dir/setup_env.sh || exit
source $self_dir/common.sh || exit

cmd=$1
[ -z $cmd ] && usage

testname=$2
testname=$(python3 -c "print('$testname'.capitalize())")
output_dir=./csharp
appname=app
app_dir=$output_dir/$appname

function init_dotnet_project()
{
    local classname="$1"
    local library_path="$2"
    dotnet new sln
    dotnet new console -o $appname
    dotnet sln add $appname/$appname.csproj
    cat >$appname/$appname.csproj << EOF
<Project Sdk="Microsoft.NET.Sdk">

  <PropertyGroup>
    <OutputType>Exe</OutputType>
    <TargetFramework>$dotnet_version</TargetFramework>
    <ImplicitUsings>enable</ImplicitUsings>
    <Nullable>disable</Nullable>
    <RestoreSources>\$(RestoreSources);$library_path;https://api.nuget.org/v3/index.json</RestoreSources>
    <StartupObject>$classname</StartupObject>
  </PropertyGroup>

</Project>
EOF
    dotnet add $appname package $package_id
}

tell_to_build()
{
    echo "Please run $this_dir/deploy.sh to build soda C# library" >&2
    exit 3
}

function check_framework()
{
    local target_dir="$self_dir/soda/bin/$build_conf"
    local pkg_file="$target_dir/$package_id.$package_version.nupkg"
    local lib_file="$target_dir/$dotnet_version/soda.dll"
    [[ ! -f $pkg_file || ! -f $lib_file ]] && tell_to_build
}

function create_work()
{
    # check dotnet project directory
    [ -e $output_dir ] && { echo "$output_dir already exists">&2; exit 2; }
    # prepare source file
    target_file=${testname}.cs
    template_file=$self_dir/soda/unittest/__Bootstrap__.cs
    create_source_file $template_file $target_file
    classname=$testname
    tmpfile=${classname}.tmp
    echo "using Soda.Unittest;" > $tmpfile
    cat $target_file | grep -v '^namespace ' \
        | sed "s/__Bootstrap__/$classname/g" \
        | sed "s/__Solution__/Solution/g" >> $tmpfile
    mv $tmpfile $target_file
    # create dotnet project
    mkdir $output_dir
    cd $output_dir || exit
    # use debug version
    lib_path=$self_dir/soda/bin/$build_conf
    init_dotnet_project $classname "$lib_path"
}

function build_work()
{
    srcfile=${testname}.cs
    exefile=$app_dir/bin/$build_conf/$dotnet_version/$appname
    if [[ ! -e $exefile ]] || [[ $srcfile -nt $exefile ]]; then
        set -e
        cp $srcfile $app_dir/Program.cs
        cd $app_dir
        dotnet build --configuration $build_conf
        set +e
    fi
}

function clean_work()
{
    set -e
    cd $app_dir
    [ -e bin ] && rm -rvf bin
    [ -e obj ] && rm -rvf obj
}

[ -z $testname ] && usage
check_framework
case $cmd in
    new)
        create_work
        ;;
    source)
        echo ${testname}.cs
        ;;
    make)
        build_work
        ;;
    run)
        cd $app_dir && ./bin/$build_conf/$dotnet_version/$appname
        ;;
    clean)
        clean_work
        ;;
    *)
        usage
        ;;
esac

