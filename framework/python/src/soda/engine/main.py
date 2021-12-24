import argparse
import json
import logging
import os
from subprocess import Popen, PIPE
import sys
import time

from soda.engine import diff
from soda.engine.legacy import parse_input_legacy
from soda.engine.support import *
from soda.engine.util import ColorText

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
                    config.set(key, json.loads(value))
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
    size = len(s)
    if size < 100:
        return s
    else:
        return s[:prefix] + f'...({size-prefix-suffix} chars)...' + s[-suffix:]

def execute(command, testname, config, testobj):
    seq_number = testobj['id']
    print(f'**[{seq_number}]**')
    print(f'* {config.inputFile} <{config.seqNum}>')

    if verbose:
        print('config:', json.dumps(config.cfg))
        print('request:', json.dumps(testobj))

    if config.showArgs:
        print('input:')
        for s in map(omit_too_long, map(json.dumps, testobj['args'])):
            print(s)

    if config.skip:
        print(ColorText.red('SKIP') + '\n')
        return True

    try:
        begin_time = time.time()
        response = call_process(command, testobj, config.timeout)
        end_time = time.time()
        total_time = end_time - begin_time
    except Exception as ex:
        print(f'Error: {ex}')
        return False

    if verbose:
        print('response:', json.dumps(response))

    if not response['success']:
        logger.error(ColorText.red('TEST FAILED'))
        res = response['result']
        expected = testobj['expected']
        if expected is not None:
            if res is None:
                print('Error: result is null')
                return False

            print('expected:', omit_too_long(json.dumps(expected)))
            print('result:', omit_too_long(json.dumps(res)))

            def not_numeric(v):
                return type(v) not in (int, float)
            if type(expected) != type(res) and not_numeric(expected) and not_numeric(res):
                print('Error: result and expected is not the same type')
                return False

            # if isinstance(res, list):
            #     diff.with_list(expected, res)
            # else:
            #     info = f'Wrong answer {json.dumps(res)}, but {json.dumps(expected)} expected'
            #     print(info)
        else:
            print('expected: [not present]', )
            print('result:', omit_too_long(json.dumps(res)))
        return False

    print(ColorText.green('SUCCESS'))
    if config.showResult:
        print('output: ' + omit_too_long(json.dumps(response['result'])))
        expected = testobj['expected']
        if expected is not None:
            print('expect: ' + omit_too_long(json.dumps(expected)))
        else:
            print('expect: -')

    print('----')
    elapse = response['elapse']
    print(f'solve: {elapse:.3f} ms')
    print(f'total: {total_time * 1000:.3f} ms\n')
    return True

def main():
    parser = argparse.ArgumentParser(prog='soda')

    parser.add_argument('testname')
    parser.add_argument('--testcase')
    parser.add_argument('--delim', default=',')
    parser.add_argument('--verbose', action='store_true', default=False)
    parser.add_argument('--command', required=True)

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
                if not execute(args.command, testname, config, testobj):
                    sys.exit(3)

if __name__ == '__main__':
    main()

