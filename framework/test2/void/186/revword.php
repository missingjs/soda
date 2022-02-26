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
     * @param String[] $s
     * @return NULL
     */
    function reverseWords(&$s) {
        if (count($s) == 0) {
            return;
        }

        $this->reverse_array($s, 0, count($s)-1);

        $N = count($s);
        $i = $j = 0;
        while ($j < $N) {
            if ($s[$j] == ' ') {
                $this->reverse_array($s, $i, $j-1);
                $i = $j + 1;
            }
            ++$j;
        }
        if ($i < $j) {
            $this->reverse_array($s, $i, $j-1);
        }
    }

    private function reverse_array(&$s, $i, $j) {
        while ($i < $j) {
            [$s[$i], $s[$j]] = [$s[$j], $s[$i]];
            ++$i;
            --$j;
        }
    }
}

$work = TestWork::create(new Solution(), 'reverseWords', $argv[0]);
// $work = TestWork::forStruct('STRUCT::class', $argv[0]);
// $work->validator = fn($e, $r) => <boolean result>
$work->compareSerial = true;
echo $work->run(Utils::fromStdin());

