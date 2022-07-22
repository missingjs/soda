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
        $this->quickSelect($nodes, 0, count($nodes)-1, $k);
        $res = array_fill(0, $k, 0);
        for ($i = 0; $i < $k; ++$i) {
            $res[$i] = $nodes[$i]->value;
        }
        return $res;
    }

    private function quickSelect(&$nodes, $start, $end, $index) {
        while ($start < $end) {
            $mid = intdiv($start + $end, 2);
            $this->placeMedian3($nodes, $start, $mid, $end);
            $k = $this->partition($nodes, $start, $end, $mid);
            if ($k == $index) {
                return;
            } elseif ($k > $index) {
                $end = $k - 1;
            } else {
                $start = $k + 1;
            }
        }
    }

    private function partition(&$nodes, $start, $end, $pivot) {
        $d = $nodes[$pivot]->diff;
        $this->swap($nodes, $pivot, $end);
        $p = $start;
        for ($i = $start; $i <= $end; ++$i) {
            if ($nodes[$i]->diff < $d) {
                if ($p != $i) {
                    $this->swap($nodes, $p, $i);
                }
                ++$p;
            }
        }
        $this->swap($nodes, $p, $end);
        return $p;
    }

    private function placeMedian3(&$nodes, $start, $mid, $end) {
        if ($nodes[$start]->diff > $nodes[$mid]->diff) {
            $this->swap($nodes, $start, $mid);
        }
        if ($nodes[$start]->diff > $nodes[$end]->diff) {
            $this->swap($nodes, $start, $end);
        }
        if ($nodes[$mid]->diff > $nodes[$end]->diff) {
            $this->swap($nodes, $mid, $end);
        }
    }

    private function collect($root, &$nodes, $target) {
        if (!$root) {
            return;
        }
        $nodes[] = Node::withTarget($root->val, $target);
        $this->collect($root->left, $nodes, $target);
        $this->collect($root->right, $nodes, $target);
    }

    private function swap(&$arr, $i, $j) {
        [$arr[$i], $arr[$j]] = [$arr[$j], $arr[$i]];
    }
}

$work = TestWork::create(new Solution(), 'closestKValues', $argv[0]);
// $work = TestWork::forStruct('STRUCT::class', $argv[0]);
$work->validator = Validators::forArray('Integer', false);
$work->compareSerial = true;
echo $work->run(Utils::fromStdin());

