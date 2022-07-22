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
     * @return ListNode[]
     */
    function reverseAll($lists) {
        for ($i = 0; $i < count($lists); ++$i) {
            $lists[$i] = $this->reverse($lists[$i]);
        }
        [$i, $j] = [0, count($lists)-1];
        while ($i < $j) {
            [$lists[$i], $lists[$j]] = [$lists[$j], $lists[$i]];
            ++$i;
            --$j;
        }
        return $lists;
    }

    private function reverse($head) {
        $h = null;
        while ($head) {
            $next = $head->next;
            $head->next = $h;
            $h = $head;
            $head = $next;
        }
        return $h;
    }
}

$work = TestWork::create(new Solution(), 'reverseAll', $argv[0]);
// $work->validator = fn($e, $r) => <boolean result>
$work->compareSerial = true;
echo $work->run(Utils::fromStdin());
