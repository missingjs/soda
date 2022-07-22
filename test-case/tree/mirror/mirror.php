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
     * @return TreeNode 
     */
    function mirror($root) {
        if (!$root) {
            return $root;
        }
        $this->mirror($root->left);
        $this->mirror($root->right);
        [$root->left, $root->right] = [$root->right, $root->left];
        return $root;
    }
}

$work = TestWork::create(new Solution(), 'mirror', $argv[0]);
// $work->validator = fn($e, $r) => <boolean result>
$work->compareSerial = true;
echo $work->run(Utils::fromStdin());
