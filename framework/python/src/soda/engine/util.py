import io
import sys

class ColorText:

    @classmethod
    def wrap(cls, color, text):
        return f"\033[0;{color}m{text}\033[0m"

    @classmethod
    def red(cls, text):
        return cls.wrap(31, text)

    @classmethod
    def green(cls, text):
        return cls.wrap(32, text)


class PrintBuffer:
    def __init__(self, fileobj = None):
        if fileobj is None:
            self.use_std = True
            self.fileobj = sys.stdout
        else:
            self.use_std = False
            self.fileobj = fileobj

    def print(self, *args, **kwargs):
        if 'file' in kwargs:
            kwargs.pop('file')
        print(*args, file=self.fileobj, **kwargs)

    def getvalue(self):
        return self.fileobj.getvalue() if not self.use_std else ''

    def close(self):
        if not self.use_std:
            self.fileobj.close()

