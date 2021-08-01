import json
import logging
import subprocess
from subprocess import Popen, PIPE
import sys
import time

from soda.engine.util import ColorText

logger = logging.getLogger(__name__)

class TestConfig:
    def __init__(self):
        self.cfg = {
            'showArgs': True,
            'showResult': True,
            'skip': False,
            'timeout': 10.0
        }
        self.inputFile = ''
        self.seqNum = -1

    @classmethod
    def fromString(cls, text):
        tc = cls()
        text = text.strip()
        if text:
            d = json.loads(text)
            tc.cfg.update(d)
        return tc

    def __getattr__(self, key):
        return self.cfg.get(key)

    def set(self, key, value):
        self.cfg[key] = value

def pstree(pid: int):
    pid_list = []
    def recursive(pid):
        res = subprocess.run(f'pgrep -P {pid}', shell=True, stdout=PIPE, encoding='utf-8')
        text = res.stdout.strip()
        if not text:
            return
        c_pids = []
        for cpid in text.split('\n'):
            cpid = cpid.strip()
            try:
                cpid = int(cpid)
                recursive(cpid)
                pid_list.append(cpid)
            except:
                pass
    recursive(pid)
    return pid_list

def kill_all_child_processes(ppid):
    for cpid in pstree(ppid):
        subprocess.run(f'kill -9 {cpid}', shell=True, stdout=PIPE, stderr=PIPE)
        time.sleep(0.01)

class LineIterator:
    def __init__(self, fp, strip=True):
        self.fp = fp
        self.strip = strip
        self.next_line = None
        self.forward()

    def forward(self):
        try:
            self.next_line = next(self.fp)
            if self.strip:
                self.next_line = self.next_line.strip()
        except StopIteration:
            self.next_line = None

    def hasNext(self):
        return self.next_line is not None

    def next(self):
        res = self.next_line
        self.forward()
        return res

    def nextContentLine(self):
        while self.hasNext():
            t = self.next().strip()
            if t and not t.startswith('#'):
                return t

class KV:
    @classmethod
    def split(cls, kv_str):
        kv = kv_str.split('=', 1)
        return (kv[0].strip(), kv[1].strip())

    @classmethod
    def value(cls, kv_str):
        return cls.split(kv_str)[1]

    @classmethod
    def intValue(cls, kv_str):
        return int(cls.split(kv_str)[1])

def call_process(command, testobj, timeout):
    datatext = json.dumps(testobj)
    return_code = None
    # DONOT capture stderr! Message from stderr should print to console
    with Popen(command, shell=True, stdin=PIPE, stdout=PIPE, encoding='utf-8') as proc:
        try:
            outs, _ = proc.communicate(datatext, timeout=timeout)
        except subprocess.TimeoutExpired as ex:
            kill_all_child_processes(proc.pid)
            raise Exception(f'Time Limit Exceeded, timeout={timeout}s')
        except:
            logger.exception('internal error')
            kill_all_child_processes(proc.pid)

        return_code = proc.returncode

    if return_code != 0:
        logger.error(ColorText.red('test failed'))
        logger.error(f'<stdout>: {outs}')
        raise Exception(f'Sub process exit with {return_code}')

    try:
        return json.loads(outs)
    except:
        raise Exception(f'Invalid response: {outs}')

def build_test_object(lines):
    testobj = {}
    testobj['args'] = list(map(json.loads, lines[:-1]))
    if lines[-1] != '-':
        testobj['expected'] = json.loads(lines[-1])
    else:
        testobj['expected'] = None
    return testobj

