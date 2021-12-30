class ObjectConverter:

    def __init__(self, funcFrom, funcTo):
        self.funcFrom = funcFrom
        self.funcTo = funcTo

    def fromJsonSerializable(self, j):
        return self.funcFrom(j)

    def toJsonSerializable(self, obj):
        return self.funcTo(obj)

