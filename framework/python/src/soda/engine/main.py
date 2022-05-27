import argparse
from contextlib import (
    redirect_stderr,
    redirect_stdout
)
import io
import json
import logging
import os
import sys
import time

from soda.engine import diff
from soda.engine.legacy import parse_input_legacy
from soda.engine.support import *
from soda.engine.util import ColorText, PrintBuffer

framework_dir = os.environ['SODA_FRAMEWORK_DIR']
verbose = False
logger = logging.getLogger(__name__)

def parse_input(fp):
    lineIter = LineIterator(fp)
    first_line = lineIter.next()
    if not first_line.startswith('#@format='):
        with open(fp.name, 'r') as fp2:
            yield from parse_input_legacy(fp2)
            return

    globalArgs = 0
    while lineIter.hasNext():
        line = lineIter.next().strip()
        if not line or not line.startswith('@'):
            continue
        if line.startswith('@args'):
            globalArgs = KV.intValue(line)
        elif line.startswith('@case'):
            fields = line.split(' ')
            config = TestConfig()
            numArgs = globalArgs
            for kv in fields[1:]:
                key, value = KV.split(kv)
                if key[0] == '%':
                    if key == '%args':
                        numArgs = int(value)
                else:
                    try:
                        jvalue = json.loads(value)
                        config.set(key, jvalue)
                    except:
                        config.set(key, value)
            lines = []
            arg1 = lineIter.next()
            if arg1.startswith('@include'):
                incFile = KV.value(arg1)
                print(f'Load test case from {incFile}')
                with open(incFile, 'r') as incFp:
                    _content = list(filter(lambda s: len(s) > 0, map(lambda s: s.strip(), incFp)))
                    lines.extend(_content[:numArgs+1])
            elif arg1.startswith('Input:'):
                lines.extend(parse_input_one_line(arg1))
                nextLine = lineIter.next()
                if nextLine.startswith('Output:'):
                    lines.append(nextLine.strip()[len('Output: '):])
                else:
                    lines.append(nextLine)
            else:
                lines.append(arg1)
                # include result line
                for i in range(numArgs):
                    lines.append(lineIter.next())
            yield (config, build_test_object(lines))

def parse_input_one_line(line):
    decoder = json.JSONDecoder()
    line = line.strip()[len('Input: '):]
    args = []
    while line:
        line = line.split('=', 1)[1].strip()
        _, index = decoder.raw_decode(line)
        args.append(line[:index])
        line = line[index:].strip()
        if line and line[0] == ',':
            line = line[1:].strip()
    return args

def omit_too_long(s, prefix=60, suffix=10):
    switch = os.environ.get('OMIT_TOO_LONG')
    if switch == 'no' or switch == 'false':
        return s
    size = len(s)
    if size < 100:
        return s
    else:
        return s[:prefix] + f'...({size-prefix-suffix} chars)...' + s[-suffix:]

def execute(command, testname, config, testobj):
    pbuf = PrintBuffer()
    res = execute_impl(command, testname, config, testobj, pbuf)
    print(pbuf.getvalue(), end = '')
    pbuf.close()
    return res

def execute_impl(command, testname, config, testobj, pb):
    seq_number = testobj['id']
    pb.print(f'**[{seq_number}]**')
    tag_str = f'[{config.tag}]' if config.tag else ''
    pb.print(f'* {config.inputFile} <{config.seqNum}> {tag_str}')

    if verbose:
        pb.print('config:', json.dumps(config.cfg))
        pb.print('request:', json.dumps(testobj))

    if config.showArgs:
        pb.print('input:')
        for s in map(omit_too_long, map(json.dumps, testobj['args'])):
            pb.print(s)

    if config.skip:
        pb.print(ColorText.red('SKIP') + '\n')
        return True

    try:
        begin_time = time.time()
        response = call_process(command, testobj, config.timeout)
        end_time = time.time()
        total_time = end_time - begin_time
    except Exception as ex:
        pb.print(f'Error: {ex}')
        return False

    if verbose:
        pb.print('response:', json.dumps(response))

    if not response['success']:
        logger.error(ColorText.red('TEST FAILED'))
        res = response['result']
        expected = testobj['expected']
        if expected is not None:
            if res is None:
                pb.print('Error: result is null')
                return False

            pb.print('result:', omit_too_long(json.dumps(res)))
            pb.print('expect:', omit_too_long(json.dumps(expected)))

            def not_numeric(v):
                return type(v) not in (int, float)
            if type(expected) != type(res) and not_numeric(expected) and not_numeric(res):
                pb.print('Error: result and expected is not the same type')
                return False

            # if isinstance(res, list):
            #     diff.with_list(expected, res)
            # else:
            #     info = f'Wrong answer {json.dumps(res)}, but {json.dumps(expected)} expected'
            #     print(info)
        else:
            pb.print('expected: [not present]', )
            pb.print('result:', omit_too_long(json.dumps(res)))
        return False

    pb.print(ColorText.green('SUCCESS'))
    if config.showResult:
        pb.print('output: ' + omit_too_long(json.dumps(response['result'])))
        expected = testobj['expected']
        if expected is not None:
            pb.print('expect: ' + omit_too_long(json.dumps(expected)))
        else:
            pb.print('expect: -')

    pb.print('----')
    elapse = response['elapse']
    pb.print(f'solve: {elapse:.3f} ms')
    pb.print(f'total: {total_time * 1000:.3f} ms\n')
    return True

def run_single_thread(testname, command, input_files, include_tags):
    counter = 0
    for infile in input_files:
        seq_in_file = 0
        with open(infile, 'r') as fp:
            for config, testobj in parse_input(fp):
                seq_in_file += 1
                counter += 1
                testobj['id'] = counter
                config.inputFile = infile
                config.seqNum = seq_in_file
                if include_tags:
                    if str(counter) not in include_tags and config.tag not in include_tags:
                        continue
                if not execute(command, testname, config, testobj):
                    sys.exit(3)
        if seq_in_file == 0:
            logger.error('No test case')

def main():
    parser = argparse.ArgumentParser(prog='soda')

    parser.add_argument('testname')
    parser.add_argument('--testcase')
    parser.add_argument('--delim', default=',')
    parser.add_argument('--verbose', action='store_true', default=False)
    parser.add_argument('--command', required=True)
    parser.add_argument('--tags')

    args = parser.parse_args()

    testname = args.testname
    if args.testcase is None:
        input_files = [f'{testname.lower()}.input']
    else:
        input_files = args.testcase.split(args.delim)

    global verbose
    verbose = args.verbose

    logging.basicConfig(level = logging.INFO, format = '%(levelname)s: %(message)s')

    if not input_files:
        logger.error('No test case specified')
        sys.exit(2)

    include_tags = set()
    if args.tags:
        include_tags = set(args.tags.split(','))

    run_single_thread(testname, args.command, input_files, include_tags)

if __name__ == '__main__':
    main()

