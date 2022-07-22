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
     * @param String[] $chars
     * @param Integer $n
     * @return String[]
     */
    function permutation($chars, $n) {
        $res = array();
        $buf = array_fill(0, $n, null);
        $this->solve($chars, 0, $buf, 0, $res);
        return $res;
    }

    function solve(&$chars, $i, &$buf, $j, &$res) {
        if ($j == count($buf)) {
            $res[] = join('', $buf);
            return;
        }
        for ($k = $i; $k < count($chars); ++$k) {
            [$chars[$i], $chars[$k]] = [$chars[$k], $chars[$i]];
            $buf[$j] = $chars[$i];
            $this->solve($chars, $i+1, $buf, $j+1, $res);
            [$chars[$i], $chars[$k]] = [$chars[$k], $chars[$i]];
        }
    }
}

$work = TestWork::create(new Solution(), 'permutation', $argv[0]);
// $work = TestWork::forStruct('STRUCT::class', $argv[0]);
$work->validator = Validators::forArray('String', false);
$work->compareSerial = true;
echo $work->run(Utils::fromStdin());

