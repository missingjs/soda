package soda.unittest;

import java.util.*;
import java.util.function.Function;
import java.util.stream.*;

import soda.leetcode.*;
import soda.unittest.*;

class Solution {
    public int add(int a, int b) { return a + b; }
}

public class __Bootstrap__ implements Function<String, String> {

    @Override
    public String apply(String text) {
        // * create work by method, including method that has no return value
        var work = GenericTestWork.create(new Solution()::add);

        // * by class of data struct
        // var work = GenericTestWork.forStruct(Solution.class);

        // * for legacy code, discouraged
        // var work = TestWork.forObject(new Solution(), "METHOD");
        // var work = TestWork.forStruct(Struct.class);

        // * setup validator
        // work.setValidator((e, r) -> {...});
        work.setCompareSerial(true);
        return work.run(text);
    }

    public static void main(String[] args) throws Exception {
        System.out.println(new __Bootstrap__().apply(Utils.fromStdin()));
    }

}
