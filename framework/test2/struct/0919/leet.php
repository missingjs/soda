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

use Soda\Leetcode\TreeFactory;

class CBTInserter {
    private $qu;
    private $root;

    /**
     * @param TreeNode $root
     */
    function __construct($root) {
        $qu = new \SplQueue;
        if ($root) {
            $qu->enqueue($root);
            while (!$qu->isEmpty()) {
                $node = $qu->bottom();
                if (!$node->left) {
                    break;
                }
                $qu->enqueue($node->left);
                if (!$node->right) {
                    break;
                }
                $qu->enqueue($node->right);

                $qu->dequeue();
            }
        }
        $this->qu = $qu;
        $this->root = $root;
    }
  
    /**
     * @param Integer $val
     * @return Integer
     */
    function insert($val) {
        $node = new TreeNode($val);
        $head = $this->qu->bottom();
        $this->qu->enqueue($node);
        if ($head->left) {
            $head->right = $node;
            $this->qu->dequeue();
        } else {
            $head->left = $node;
        }
        return $head->val;
    }
  
    /**
     * @return TreeNode
     */
    function get_root() {
        return $this->root;
    }
}

// $work = TestWork::create(new Solution(), 'add', $argv[0]);
$work = TestWork::forStruct(CBTInserter::class, $argv[0]);
// $work->validator = fn($e, $r) => <boolean result>
$work->compareSerial = true;
echo $work->run(Utils::fromStdin());

