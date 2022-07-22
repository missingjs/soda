<?php
require_once 'Soda/DS/index.php';
require_once 'Soda/Leetcode/index.php';
require_once 'Soda/Unittest/index.php';

use Soda\DS;
use Soda\Leetcode\ListNode;
use Soda\Leetcode\NestedInteger;
use Soda\Leetcode\TreeNode;
use Soda\Unittest\TestWork;
use Soda\Unittest\Utils;
use Soda\Unittest\Validators;

class Solution {

    /**
     * @param ListNode $head
     * @return NULL
     */
    function reorderList($head) {
        $fast = $slow = $head;
        while ($fast->next && $fast->next->next) {
            $slow = $slow->next;
            $fast = $fast->next->next;
        }
        if ($slow == $fast) {
            return;
        }

        $r = $this->reverse($slow->next);
        $slow->next = null;
        $this->merge($head, $r);
    }

    private function reverse($head) {
        $q = null;
        while ($head) {
            $next = $head->next;
            $head->next = $q;
            $q = $head;
            $head = $next;
        }
        return $q;
    }

    private function merge($L1, $L2) {
        $t = $h = new ListNode;
        while ($L1 && $L2) {
            $t->next = $L1;
            $t = $L1;
            $L1 = $L1->next;
            $t->next = $L2;
            $t = $L2;
            $L2 = $L2->next;
        }
        $t->next = $L1 ? $L1 : $L2;
    }
}

$work = TestWork::create(new Solution(), 'reorderList', $argv[0]);
// $work = TestWork::forStruct('STRUCT::class', $argv[0]);
// $work->validator = fn($e, $r) => <boolean result>
$work->compareSerial = true;
echo $work->run(Utils::fromStdin());

