import soda.unittest.*;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.*;

import soda.leetcode.*;
import soda.unittest.Validators;
import soda.unittest.work.TestWork;

import static soda.unittest.LoggerHelper.logger;

class Solution {
    public TreeNode mirror(TreeNode root) {
        if (root == null) {
            return null;
        }
        mirror(root.left);
        mirror(root.right);
        var temp = root.left;
        root.left = root.right;
        root.right = temp;
        return root;
    }
}

public class Mirror implements Supplier<TestWork> {

    @Override
    public TestWork get() {
        var work = new TestWork(new Solution(), "mirror");
        // var work = TestWork.forStruct(Struct.class);
        // work.setValidator((e, r) -> {...});
        work.setCompareSerial(true);
        return work;
    }

    public static void main(String[] args) throws Exception {
        new Mirror().get().run();
    }

}
