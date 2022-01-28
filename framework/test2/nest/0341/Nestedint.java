import soda.unittest.*;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.*;

import soda.leetcode.*;
import soda.unittest.Validators;
import soda.unittest.TestWork;


class Solution {
    public List<Integer> flatNested(List<NestedInteger> niList) {
        var res = new ArrayList<Integer>();
        var iter = new NestedIterator(niList);
        while (iter.hasNext()) {
            res.add(iter.next());
        }
        return res;
    }
}

class NestedIterator implements Iterator<Integer> {

    static class Node {
        public List<NestedInteger> list;
        public int index;
        public Node(List<NestedInteger> list) {
            this.list = list;
        }
        public boolean isEnd() {
            return index >= list.size();
        }
        public int value() {
            return current().getInteger();
        }
        public NestedInteger current() {
            return list.get(index);
        }
    }

    private Deque<Node> stk = new ArrayDeque<>();

    public NestedIterator(List<NestedInteger> nestedList) {
        stk.offerLast(new Node(nestedList));
        locate();
    }

    private void locate() {
        while (!stk.isEmpty()) {
            if (stk.peekLast().isEnd()) {
                stk.pollLast();
                if (!stk.isEmpty()) {
                    ++stk.peekLast().index;
                }
            } else if (stk.peekLast().current().isInteger()) {
                break;
            } else {
                stk.offerLast(new Node(stk.peekLast().current().getList()));
            }
        }
    }
    
    @Override
    public Integer next() {
        int value = stk.peekLast().value();
        ++stk.peekLast().index;
        locate();
        return value;
    }

    @Override
    public boolean hasNext() {
        return !stk.isEmpty() && !stk.peekLast().isEnd();
    }
}

public class Nestedint implements Supplier<TestWork> {

    @Override
    public TestWork get() {
        var work = new TestWork(new Solution(), "flatNested");
        // work.setValidator((e, r) -> {...});
        work.setCompareSerial(true);
        // work.setArgumentParser(index, a -> { ... });
        // work.setResultParser(r -> { ... });
        // work.setResultSerializer(r -> {...});
        return work;
    }

    public static void main(String[] args) throws Exception {
        new Nestedint().get().run();
    }

}
