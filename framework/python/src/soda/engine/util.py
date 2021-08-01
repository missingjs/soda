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

