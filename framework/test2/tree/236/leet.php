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
     * @param TreeNode $p
     * @param TreeNode $q
     * @return TreeNode
     */
    function lowestCommonAncestor($root, $p, $q) {
        $stk = [$root];
        $last = $root;
        $foundOne = false;
        $index = -1;

        if ($root == $p || $root == $q) {
            $foundOne = true;
            $index = 0;
        }

        while (count($stk) > 0) {
            $node = end($stk);
            if ($node->left && $last != $node->left && $last != $node->right) {
                if ($node->left == $p || $node->left == $q) {
                    if (!$foundOne) {
                        $index = count($stk);
                        $foundOne = true;
                    } else {
                        return $stk[$index];
                    }
                }
                $stk[] = $node->left;
            } elseif ($node->right && $last != $node->right) {
                if ($node->right == $p || $node->right == $q) {
                    if (!$foundOne) {
                        $index = count($stk);
                        $foundOne = true;
                    } else {
                        return $stk[$index];
                    }
                }
                $stk[] = $node->right;
            } else {
                $last = $node;
                if ($index == count($stk) - 1) {
                    --$index;
                }
                array_pop($stk);
            }
        }
        return null;
    }
}

class Driver {
    /**
     * @param TreeNode $root
     * @param Integer $p
     * @param Integer $q
     * @return Integer
     */
    function exec($root, $p, $q) {
        $pNode = $this->findNode($root, $p);
        $qNode = $this->findNode($root, $q);
        return (new Solution())->lowestCommonAncestor($root, $pNode, $qNode)->val;
    }

    private function findNode($root, $val) {
        if (!$root) {
            return null;
        }
        if ($root->val === $val) {
            return $root;
        }
        $L = $this->findNode($root->left, $val);
        return $L ? $L : $this->findNode($root->right, $val);
    }
}

$work = TestWork::create(new Driver(), 'exec', $argv[0]);
// $work->validator = fn($e, $r) => <boolean result>
$work->compareSerial = true;
echo $work->run(Utils::fromStdin());
