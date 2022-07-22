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
    private $original;

    /**
     * @param Integer[] $nums
     */
    function __construct($nums) {
        $this->original = $nums;
    }
  
    /**
     * @return Integer[]
     */
    function reset() {
        return $this->original;
    }
  
    /**
     * @return Integer[]
     */
    function shuffle() {
        $res = $this->reset();
        for ($s = count($res); $s > 0; --$s) {
            $i = random_int(0, $s-1);
            $j = $s - 1;
            if ($i != $j) {
                [$res[$i], $res[$j]] = [$res[$j], $res[$i]];
            }
        }
        return $res;
    }
}

// $work = TestWork::create(new Solution(), 'add', $argv[0]);
$work = TestWork::forStruct(Solution::class, $argv[0]);
$work->validator = function ($expect, $result) use ($work) {
    $args = $work->getArguments();
    $commands = $args[0];
    $arrCmp = Validators::forArray('Integer', false);
    for ($i = 1; $i < count($commands); ++$i) {
        $cmd = $commands[$i];
        if ($cmd == "shuffle") {
            $evals = $expect[$i];
            $rvals = $result[$i];
            if (!$arrCmp($expect[$i], $result[$i])) {
                return false;
            }
        }
    }
    return true;
};
$work->compareSerial = true;
echo $work->run(Utils::fromStdin());

