#!/bin/bash

usage()
{
    cat << EOF
usage:
    $(basename $0) -c <case-name>
EOF
    exit 1
}

self_dir=$(cd $(dirname $0) && pwd)

while getopts ':c:' option; do
    case $option in
        c)
            case_name="$OPTARG"
            ;;
        \?)
            usage
            ;;
    esac
done

[ -z $case_name ] && usage

cat << EOF
SOURCES = ${case_name}__gen.cpp
OBJS = \$(SOURCES:.cpp=.o)
LIBRARY = -L${self_dir}/src/soda/leetcode -lleetcode -L${self_dir}/src/soda/unittest -lsodatest
CC = g++
CPPFLAGS = \${SODA_INCLUDE} \${SODA_CPP_FLAGS}
TARGET = ${case_name}.out

all: \$(TARGET)

LEETCODE_LIB:
	@cd ${self_dir}/src/soda/leetcode; \\
	make; cd -;

SODATEST_LIB:
	@cd ${self_dir}/src/soda/unittest; \\
	make; cd -;

EOF

cat <<"EOF"
%.d: %.cpp
	@set -e; rm -f $@; \
	$(CC) $(CPPFLAGS) -MM $< > $@.$$$$; \
	sed 's,\($*\)\.o[ :]*,\1.o $@ : ,g' < $@.$$$$ > $@; \
	rm -f $@.$$$$

sinclude $(SOURCES:.cpp=.d)

$(TARGET): $(OBJS) LEETCODE_LIB SODATEST_LIB
	$(CC) $(CPPFLAGS) -o $(TARGET) $(OBJS) $(LIBRARY)

clean:
	rm -f *.o $(TARGET) *.d
EOF

