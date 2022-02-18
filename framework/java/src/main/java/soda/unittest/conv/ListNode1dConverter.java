package soda.unittest.conv;

import soda.leetcode.ListHelper;
import soda.leetcode.ListNode;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ListNode1dConverter implements ObjectConverter<List<ListNode>, List<List<Integer>>> {
    @Override
    public List<ListNode> fromJsonSerializable(List<List<Integer>> lists) {
        return lists.stream().map(ListHelper::create).collect(Collectors.toList());
    }

    @Override
    public List<List<Integer>> toJsonSerializable(List<ListNode> listNodes) {
        return listNodes.stream().map(ListHelper::dump).collect(Collectors.toList());
    }

    public static ObjectConverter<ListNode[], List<List<Integer>>> forArray() {
        var conv = new ListNode1dConverter();
        return new ObjectConverter<ListNode[], List<List<Integer>>>() {

            @Override
            public ListNode[] fromJsonSerializable(List<List<Integer>> lists) {
                return conv.fromJsonSerializable(lists).toArray(ListNode[]::new);
            }

            @Override
            public List<List<Integer>> toJsonSerializable(ListNode[] listNodes) {
                return conv.toJsonSerializable(Arrays.stream(listNodes).collect(Collectors.toList()));
            }
        };
    }

}
