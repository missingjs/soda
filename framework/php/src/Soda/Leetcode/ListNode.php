<?php
namespace Soda\Leetcode;

class ListNode {
    public $val = 0;
    public $next = null;
    function __construct($val = 0, $next = null) {
        $this->val = $val;
        $this->next = $next;
    }
}

class ListFactory
{
    static function create(array $data): ?ListNode
    {
        $head = new ListNode;
        $tail = $head;
        foreach ($data as $val) {
            $node = new ListNode($val);
            $tail->next = $node;
            $tail = $node;
        }
        return $head->next;
    }

    static function dump(?ListNode $head): array
    {
        $list = array();
        while ($head != null) {
            $list[] = $head->val;
            $head = $head->next;
        }
        return $list;
    }
}
