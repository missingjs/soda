import logging

from soda.engine.support import *

logger = logging.getLogger(__name__)

def parse_input_legacy(fp):
    logger.error('Using legacy format')
    status = 0
    lines = []
    config = None
    for line in fp:
        line = line.strip()
        if status == 0:
            if line.startswith('#>>') or line.startswith('@>>'):
                config = TestConfig.fromString(line[3:])
                status = 1
        elif status == 1:
            if line.startswith('#<<') or line.startswith('@<<'):
                yield (config, build_test_object(lines))
                status = 0
                lines = []
                config = None
            elif line and line[0] != '#':
                lines.append(line)
