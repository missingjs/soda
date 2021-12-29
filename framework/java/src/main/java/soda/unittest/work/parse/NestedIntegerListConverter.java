package soda.unittest.work.parse;

import soda.leetcode.DefaultNestedInteger;
import soda.leetcode.NestedInteger;

import java.util.ArrayList;
import java.util.List;

public class NestedIntegerListConverter implements ObjectConverter<List<NestedInteger>, List<Object>> {
    @Override
    public List<NestedInteger> fromJsonSerializable(List<Object> objects) {
        var res = new ArrayList<NestedInteger>();
        for (var obj : objects) {
            res.add(parse(obj));
        }
        return res;
    }

    @SuppressWarnings("unchecked")
    private NestedInteger parse(Object obj) {
        if (obj instanceof Integer) {
            return new DefaultNestedInteger((Integer) obj);
        }
        var ni = new DefaultNestedInteger();
        for (var a : (List<Object>) obj) {
            ni.add(parse(a));
        }
        return ni;
    }

    @Override
    public List<Object> toJsonSerializable(List<NestedInteger> nestedIntegers) {
        var res = new ArrayList<Object>();
        for (var ni : nestedIntegers) {
            res.add(unparse(ni));
        }
        return res;
    }

    private Object unparse(NestedInteger ni) {
        if (ni.isInteger()) {
            return ni.getInteger();
        }
        var list = new ArrayList<Object>();
        for (var e : ni.getList()) {
            list.add(unparse(e));
        }
        return list;
    }
}
