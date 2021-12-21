from typing import *

def structTester(cls):
    def test(operations: List[str], parameters: List) -> List:
        obj = cls(*parameters[0])
        res = [None]
        for i in range(1, len(operations)):
            m = obj.__getattribute__(operations[i])
            res.append(m(*parameters[i]))
        return res
    return test
