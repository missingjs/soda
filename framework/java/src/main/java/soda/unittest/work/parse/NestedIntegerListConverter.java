package soda.unittest.work.parse;

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

    private NestedInteger parse(Object obj) {
        return NestedIntegerConverter.parse(obj);
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
        return NestedIntegerConverter.serialize(ni);
    }
}
