#!/bin/bash

usage()
{
    local cmd=$(basename $0)
    cat>&2 << EOF
usage:
    soda java <cmd> [options]

options:
    new <testname>
        create source file with name <testname>.java

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

cmd=$1
[ -z $cmd ] && usage

testname=$2
assert_testname() {
    [ -z $testname ] && usage
}
testname=$(python3 -c "print('$testname'.capitalize())")
output_dir=./csharp

function init_dotnet_project()
{
    local classname="$1"
    local library_path="$2"
    dotnet new sln
    dotnet new console -o app
    dotnet sln add app/app.csproj
    cat >app/app.csproj << EOF
<Project Sdk="Microsoft.NET.Sdk">

  <PropertyGroup>
    <OutputType>Exe</OutputType>
    <TargetFramework>net6.0</TargetFramework>
    <ImplicitUsings>enable</ImplicitUsings>
    <Nullable>enable</Nullable>
    <RestoreSources>\$(RestoreSources);$library_path</RestoreSources>
    <StartupObject>$classname</StartupObject>
  </PropertyGroup>

</Project>
EOF
    dotnet add app package soda-csharp
}

assert_testname
case $cmd in
    new)
        # check dotnet project directory
        [ -e $output_dir ] && { echo "$output_dir already exists">&2; exit 2; }
        # prepare source file
        target_file=${testname}.cs
        template_file=$self_dir/soda/unittest/__Bootstrap__.cs
        create_source_file $template_file $target_file
        classname=$testname
        tmpfile=${classname}.tmp
        cat $target_file | grep -v '^package ' | sed "s/__Bootstrap__/$classname/g" > $tmpfile
        mv $tmpfile $target_file
        # create dotnet project
        mkdir $output_dir
        cd $output_dir
        # use debug version
        lib_path=$self_dir/soda/bin/Debug
        init_dotnet_project $classname "$lib_path"
        ;;
    make)
        srcfile=${testname}.cs
        exefile=$output_dir/app/bin/Debug/net6.0/app.dll
        if [[ ! -e $exefile ]] || [[ $srcfile -nt $exefile ]]; then
            set -e
            cp $srcfile $output_dir/app/Program.cs
            cd $output_dir/app
            dotnet build
            set +e
        fi
        ;;
    run)
        cd $output_dir/app && dotnet run
        ;;
    clean)
        [ -e $output_dir ] && rm -v -r $output_dir
        ;;
    *)
        usage
        ;;
esac

