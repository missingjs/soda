this_dir=$(cd -P $(dirname ${BASH_SOURCE[0]}) >/dev/null 2>&1 && pwd)

build_conf=Debug
dotnet_version="net6.0"

package_id=$(grep -oP '(?<=[<]PackageId>).*(?=</PackageId>)' $this_dir/soda/soda.csproj)
package_version=$(grep -oP '(?<=[<]Version>).*(?=</Version>)' $this_dir/soda/soda.csproj)

check_package()
{
    [ -z $package_id -o -z $package_version ] && { echo "package id and version not found" >&2; exit 1; }
}

