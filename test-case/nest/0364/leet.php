<?php
require_once 'Soda/DS/index.php';
require_once 'Soda/Leetcode/index.php';
require_once 'Soda/Unittest/index.php';

use Soda\DS;
use Soda\Leetcode\ListNode;
use Soda\Leetcode\NestedInteger;
use Soda\Unittest\TestWork;
use Soda\Unittest\Utils;
use Soda\Unittest\Validators;

class Info {
    public $sum;
    public $product;
    public $maxDepth;
    function __construct($s, $p, $m) {
        $this->sum = $s;
        $this->product = $p;
        $this->maxDepth = $m;
    }
}

class Solution {

    /**
     * @param NestedInteger[] $nestedList
     * @return Integer
     */
    function depthSumInverse($nestedList) {
        $info = $this->getInfo($nestedList, 1);
        return ($info->maxDepth + 1) * $info->sum - $info->product;
    }

    private function getInfo($nestedList, $depth) {
        $sum = 0;
        $product = 0;
        $maxDepth = $depth;
        foreach ($nestedList as $ni) {
            if ($ni->isInteger()) {
                $val = $ni->getInteger();
                $sum += $val;
                $product += $val * $depth;
                $maxDepth = max($maxDepth, $depth);
            } else {
                $res = $this->getInfo($ni->getList(), $depth + 1);
                $sum += $res->sum;
                $product += $res->product;
                $maxDepth = max($maxDepth, $res->maxDepth);
            }
        }
        return new Info($sum, $product, $maxDepth);
    }
}

$work = TestWork::create(new Solution(), 'depthSumInverse', $argv[0]);
// $work->validator = fn($e, $r) => <boolean result>
$work->compareSerial = true;
echo $work->run(Utils::fromStdin());
