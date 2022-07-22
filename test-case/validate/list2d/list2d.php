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
     * @param String[] $strs
     * @return String[][]
     */
    function groupByLength($strs) {
        shuffle($strs);
        $group = array();
        foreach ($strs as $s) {
            $group[strlen($s)][] = $s;
        }
        $keys = array_keys($group);
        shuffle($keys);
        return array_map(fn($k) => $group[$k], $keys);
    }
}

$work = TestWork::create(new Solution(), 'groupByLength', $argv[0]);
// $work = TestWork::forStruct('STRUCT::class', $argv[0]);
$work->validator = Validators::forArray2d('String', false, false);
$work->compareSerial = true;
echo $work->run(Utils::fromStdin());

