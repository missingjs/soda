package soda.unittest.conv;

import soda.leetcode.ListHelper;
import soda.leetcode.ListNode;

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
}
