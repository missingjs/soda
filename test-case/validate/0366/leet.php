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
     * @param TreeNode $root
     * @return Integer[][]
     */
    function findLeaves($root) {
        $res = array_fill(0, 100, array());
        $r = $this->solve($root, $res);
        return array_slice($res, 0, $r);
    }

    private function solve($root, &$res) {
        if (!$root) {
            return 0;
        }
        $R = $this->solve($root->right, $res);
        $L = $this->solve($root->left, $res);
        $index = max($L, $R);
        $res[$index][] = $root->val;
        return $index + 1;
    }
}

$work = TestWork::create(new Solution(), 'findLeaves', $argv[0]);
// $work = TestWork::forStruct('STRUCT::class', $argv[0]);
$work->validator = Validators::forArray2d('Integer', true, false);
$work->compareSerial = true;
echo $work->run(Utils::fromStdin());

