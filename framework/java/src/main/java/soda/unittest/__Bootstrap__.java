package soda.unittest;

import java.util.*;
import java.util.function.Function;
import java.util.stream.*;

import soda.leetcode.*;
import soda.unittest.*;

class Solution {
    public int convert(int a) { return a + 10; }
}

public class __Bootstrap__ implements Function<String, String> {

    @Override
    public String apply(String text) {
        var work = GenericTestWork.create1(new Solution()::convert);
        // var work = GenericTestWork.forStruct(Solution.class);
        // var work = TestWork.forObject(new Solution(), "METHOD");
        // var work = TestWork.forStruct(Struct.class);
        // work.setValidator((e, r) -> {...});
        work.setCompareSerial(true);
        return work.run(text);
    }

    public static void main(String[] args) throws Exception {
        System.out.println(new __Bootstrap__().apply(Utils.fromStdin()));
    }

}
