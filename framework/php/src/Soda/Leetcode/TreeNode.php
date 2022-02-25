<?php
namespace Soda\Leetcode;

use \SplQueue;

class TreeNode {
    public $val = null;
    public $left = null;
    public $right = null;
    function __construct($value) { $this->val = $value; }
}

class TreeFactory
{
    static function create(array $data): ?TreeNode
    {
        if (count($data) == 0) {
            return null;
        }

        $root = new TreeNode($data[0]);
        $qu = new SplQueue;
        $qu->enqueue($root);
        for ($index = 1; $index < count($data); ) {
            $node = $qu->dequeue();
            if (!is_null($data[$index])) {
                $node->left = new TreeNode($data[$index]);
                $qu->enqueue($node->left);
            }
            if (++$index == count($data)) {
                break;
            }
            if (!is_null($data[$index])) {
                $node->right = new TreeNode($data[$index]);
                $qu->enqueue($node->right);
            }
            ++$index;
        }
        return $root;
    }

    static function dump(?TreeNode $root): array
    {
        if (is_null($root)) {
            return array();
        }

        $res = [];
        $qu = new SplQueue;
        $qu->enqueue($root);
        while (!$qu->isEmpty()) {
            $node = $qu->dequeue();
            if ($node) {
                $res[] = $node->val;
                $qu->enqueue($node->left);
                $qu->enqueue($node->right);
            } else {
                $res[] = null;
            }
        }

        while (is_null($res[count($res)-1])) {
            array_pop($res);
        }
        return $res;
    }
}
