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

class SummaryRanges {
    private $parent;
    private $ancestorSet;
    /**
     */
    function __construct() {
        $this->parent = array_fill(0, 10003, 0);
        $this->ancestorSet = array();
    }
  
    /**
     * @param Integer $val
     * @return NULL
     */
    function addNum($val) {
        ++$val;
        if ($this->parent[$val] != 0) {
            return;
        }
        $this->parent[$val] = -1;
        $this->ancestorSet[$val] = true;
        $left = $val - 1;
        $right = $val + 1;
        if ($left > 0 && $this->parent[$left] != 0) {
            $this->merge($left, $val);
        }
        if ($this->parent[$right] != 0) {
            $this->merge($val, $right);
        }
    }
  
    /**
     * @return Integer[][]
     */
    function getIntervals() {
        $ans = array_keys($this->ancestorSet);
        sort($ans);
        $res = array_fill(0, count($ans), null);
        for ($i = 0; $i < count($res); ++$i) {
            $start = $ans[$i];
            $end = $start - $this->parent[$start] - 1;
            $res[$i] = [$start-1, $end-1];
        }
        return $res;
    }

    private function merge($x, $y) {
        $ax = $this->getAncestor($x);
        $ay = $this->getAncestor($y);
        if ($ax < $ay) {
            $this->mergeAncestor($ax, $ay);
        } else {
            $this->mergeAncestor($ay, $ax);
        }
    }

    private function mergeAncestor($ax, $ay) {
        $this->parent[$ax] += $this->parent[$ay];
        $this->parent[$ay] = $ax;
        unset($this->ancestorSet[$ay]);
    }

    private function getAncestor($x) {
        return $this->parent[$x] < 0
            ? $x
            : ($this->parent[$x] = $this->getAncestor($this->parent[$x]));
    }
}

// $work = TestWork::create(new Solution(), 'add', $argv[0]);
$work = TestWork::forStruct(SummaryRanges::class, $argv[0]);
// $work->validator = fn($e, $r) => <boolean result>
$work->compareSerial = true;
echo $work->run(Utils::fromStdin());

