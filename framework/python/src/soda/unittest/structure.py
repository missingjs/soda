from typing import *

from .registry import getConverter

def structTester(cls):
    def test(operations: List[str], parameters: List) -> List:
        ctor_args = parse_args(get_type_hints(cls.__init__).values(), parameters[0])
        obj = cls(*ctor_args)
        res = [None]
        for i in range(1, len(operations)):
            m = obj.__getattribute__(operations[i])
            hints = list(get_type_hints(m).values())
            arg_types = hints[:-1]
            ret_type = hints[-1]
            r = m(*parse_args(arg_types, parameters[i]))
            res.append(getConverter(ret_type).toJsonSerializable(r))
        return res
    return test

def parse_args(types, raw_args):
    return tuple(getConverter(t).fromJsonSerializable(v) for t,v in zip(types, raw_args))
