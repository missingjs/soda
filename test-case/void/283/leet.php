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
     * @param Integer[] $nums
     * @return NULL
     */
    function moveZeroes(&$nums) {
        $p = 0;
        for ($i = 0; $i < count($nums); ++$i) {
            if ($nums[$i] != 0) {
                if ($i != $p) {
                    [$nums[$i], $nums[$p]] = [$nums[$p], $nums[$i]];
                }
                ++$p;
            }
        }
        for (; $p < count($nums); ++$p) {
            $nums[$p] = 0;
        }
    }
}

$work = TestWork::create(new Solution(), 'moveZeroes', $argv[0]);
// $work = TestWork::forStruct('STRUCT::class', $argv[0]);
// $work->validator = fn($e, $r) => <boolean result>
$work->compareSerial = true;
echo $work->run(Utils::fromStdin());

