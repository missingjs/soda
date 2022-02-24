<?php
require_once 'Soda/DS/index.php';
require_once 'Soda/Leetcode/index.php';
require_once 'Soda/Unittest/index.php';

use Soda\DS;
use Soda\Leetcode\ListNode;
use Soda\Unittest\TestWork;
use Soda\Unittest\Utils;
use Soda\Unittest\Validators;

class Solution {

    /**
     * @param ListNode[] $lists
     * @return ListNode
     */
    function mergeKLists($lists) {
        $qu = new SplPriorityQueue;
        foreach ($lists as $L) {
            if ($L) {
                $qu->insert($L, -$L->val);
            }
        }

        $head = new ListNode;
        $tail = $head;
        while (!$qu->isEmpty()) {
            $t = $qu->extract();
            $L = $t->next;
            if ($L) {
                $qu->insert($L, -$L->val);
            }
            $tail->next = $t;
            $tail = $t;
        }
        return $head->next;
    }
}

$work = TestWork::create(new Solution(), 'mergeKLists', $argv[0]);
// $work->validator = fn($e, $r) => <boolean result>
$work->compareSerial = true;
echo $work->run(Utils::fromStdin());
