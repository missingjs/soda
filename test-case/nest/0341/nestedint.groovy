import soda.groovy.leetcode.*
import soda.groovy.unittest.*

class Solution {
    List<Integer> flatNested(List<NestedInteger> niList) {
        def res = []
        def iter = new NestedIterator(niList)
        while (iter.hasNext()) {
            res << iter.next()
        }
        res
    }
}

class NestedIterator implements Iterator<Integer> {

    static class Node {
        List<NestedInteger> list
        int index
        Node(List<NestedInteger> list) {
            this.list = list
        }
        boolean isEnd() {
            index >= list.size()
        }
        int value() {
            current().getInteger()
        }
        NestedInteger current() {
            list[index]
        }
    }

    private Deque<Node> stk = new ArrayDeque<>()

    NestedIterator(List<NestedInteger> nestedList) {
        stk.offerLast(new Node(nestedList))
        locate()
    }

    private void locate() {
        while (!stk.isEmpty()) {
            if (stk.peekLast().isEnd()) {
                stk.pollLast()
                if (!stk.isEmpty()) {
                    ++stk.peekLast().index
                }
            } else if (stk.peekLast().current().isInteger()) {
                break
            } else {
                stk.offerLast(new Node(stk.peekLast().current().getList()))
            }
        }
    }
    
    @Override
    public Integer next() {
        int value = stk.peekLast().value()
        ++stk.peekLast().index
        locate()
        value
    }

    @Override
    boolean hasNext() {
        !stk.isEmpty() && !stk.peekLast().isEnd()
    }
}

class NestedintWork {
    String call(String input) {
        def work = TestWork.create(new Solution().&flatNested)
        // def work = TestWork.forStruct(STRUCT)
        // work.validator = { i, j -> i == j }
        work.compareSerial = true
        work.run(input)
    }
}

println new NestedintWork()(System.in.getText('UTF-8'))
