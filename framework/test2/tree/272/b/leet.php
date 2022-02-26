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

class Node {
    public $diff;
    public $value;

    function __construct($d, $v) {
        $this->diff = $d;
        $this->value = $v;
    }

    static function withTarget($value, $target) {
        return new Node(abs($target - $value), $value);
    }
}

class Solution {

    /**
     * @param TreeNode $root
     * @param Float $target
     * @param Integer $k
     * @return Integer[]
     */
    function closestKValues($root, $target, $k) {
        $nodes = array();
        $this->collect($root, $nodes, $target);
        usort($nodes, fn($n1, $n2) => $n1->diff <=> $n2->diff);
        $res = array_fill(0, $k, 0);
        for ($i = 0; $i < $k; ++$i) {
            $res[$i] = $nodes[$i]->value;
        }
        return $res;
    }

    private function collect($root, &$nodes, $target) {
        if (!$root) {
            return;
        }
        $nodes[] = Node::withTarget($root->val, $target);
        $this->collect($root->left, $nodes, $target);
        $this->collect($root->right, $nodes, $target);
    }
}

$work = TestWork::create(new Solution(), 'closestKValues', $argv[0]);
// $work = TestWork::forStruct('STRUCT::class', $argv[0]);
$work->validator = Validators::forArray('Integer', false);
$work->compareSerial = true;
echo $work->run(Utils::fromStdin());

