using Soda.Unittest;
using Soda.Leetcode;


public class Solution {
    public IList<int> FlatNested(IList<NestedInteger> niList)
    {
        var res = new List<int>();
        var iter = new NestedIterator(niList);
        while (iter.HasNext()) {
            res.Add(iter.Next());
        }
        return res;
    }
}

public class NestedIterator {

    class Node {
        public IList<NestedInteger> list;
        public int index;
        public Node(IList<NestedInteger> list) {
            this.list = list;
        }
        public bool IsEnd() { return index >= list.Count; }
        public int Value() { return Current().GetInteger(); }
        public NestedInteger Current() { return list[index]; }
    }

    private readonly Stack<Node> stk = new Stack<Node>();

    public NestedIterator(IList<NestedInteger> nestedList) {
        stk.Push(new Node(nestedList));
        locate();
    }

    private void locate() {
        while (stk.Count > 0) {
            if (stk.Peek().IsEnd()) {
                stk.Pop();
                if (stk.Count > 0) {
                    ++stk.Peek().index;
                }
            } else if (stk.Peek().Current().IsInteger()) {
                break;
            } else {
                stk.Push(new Node(stk.Peek().Current().GetList()));
            }
        }
    }

    public bool HasNext() {
        return stk.Count > 0 && !stk.Peek().IsEnd();
    }

    public int Next() {
        int value = stk.Peek().Value();
        ++stk.Peek().index;
        locate();
        return value;
    }
}

public class Nestedint
{
    public static void Main(string[] args)
    {
        var work = WorkFactory.Create(Utils.Fn(new Solution().FlatNested));
        // work.SetValidator((e, r) => ...);
        work.CompareSerial = true;
        Console.WriteLine(work.Run(Utils.readStdin()));
    }
}
