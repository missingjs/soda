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
        $queue = new \SplPriorityQueue;
        $this->solve($root, $target, $k, $queue);

        $res = array();
        while (!$queue->isEmpty()) {
            $res[] = $queue->extract()->value;
        }
        return $res;
    }

    private function solve($root, $target, $k, $queue) {
        if (!$root) {
            return;
        }

        $node = Node::withTarget($root->val, $target);
        if ($queue->count() == $k) {
            if ($node->diff < $queue->top()->diff) {
                $queue->extract();
                $queue->insert($node, $node->diff);
            }
        } else {
            $queue->insert($node, $node->diff);
        }

        $this->solve($root->left, $target, $k, $queue);
        $this->solve($root->right, $target, $k, $queue);
    }
}

$work = TestWork::create(new Solution(), 'closestKValues', $argv[0]);
// $work = TestWork::forStruct('STRUCT::class', $argv[0]);
$work->validator = Validators::forArray('Integer', false);
$work->compareSerial = true;
echo $work->run(Utils::fromStdin());

