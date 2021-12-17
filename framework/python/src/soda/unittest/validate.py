
class ListEqual:

    def __init__(self, elementTester):
        self.elementTester = elementTester

    def __call__(self, a, b) -> bool:
        if len(a) != len(b):
            return False
        N = len(b)
        visited = [False] * N
        for e in a:
            exist = False
            for i in range(N):
                if not visited[i] and self.elementTester(e, b[i]):
                    visited[i] = True
                    exist = True
                    break
            if not exist:
                return False

        return True

class Validators:

    @classmethod
    def list1d(cls):
        return ListEqual(lambda x, y: x == y)

    @classmethod
    def list2d(cls):
        return ListEqual(cls.list1d())

