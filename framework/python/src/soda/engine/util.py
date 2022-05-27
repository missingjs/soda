import io

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
    def __init__(self):
        self.fileobj = io.StringIO()

    def print(self, *args, **kwargs):
        if 'file' in kwargs:
            kwargs.pop('file')
        print(*args, file=self.fileobj, **kwargs)

    def getvalue(self):
        return self.fileobj.getvalue()

    def close(self):
        self.fileobj.close()

