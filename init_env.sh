SELF_DIR=$(cd -P $(dirname ${BASH_SOURCE[0]}) >/dev/null 2>&1 && pwd)
echo $PYTHONPATH | grep -q "$SELF_DIR:" || export PYTHONPATH="$SELF_DIR:$PYTHONPATH"
# echo "Add $SELF_DIR to PYTHONPATH"

bin_dir=$SELF_DIR/bin
export PATH="$bin_dir:$PATH"
