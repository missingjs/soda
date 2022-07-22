<?php
require_once 'Soda/DS/index.php';
require_once 'Soda/Leetcode/index.php';
require_once 'Soda/Unittest/index.php';

use Soda\DS;
use Soda\Leetcode\ListNode;
use Soda\Leetcode\NestedInteger;
use Soda\Unittest\TestWork;
use Soda\Unittest\Utils;
use Soda\Unittest\Validators;

class Solution {
    /**
     * @param NestedInteger[] $niList
     * @return Integer[]
     */
    function flatNested($niList) {
        $iter = new NestedIterator($niList);
        $res = array();
        while ($iter->hasNext()) {
            $res[] = $iter->next();
        }
        return $res;
    }
}

class Node {
    public $niList;
    public $index;
    function __construct($niList) {
        $this->niList = $niList;
        $this->index = 0;
    }
    function isEnd() {
        return $this->index >= count($this->niList);
    }
    function value() {
        return $this->current()->getInteger();
    }
    function current() {
        return $this->niList[$this->index];
    }
}

class NestedIterator {
    private $stk;

    /**
     * @param NestedInteger[] $nestedList
     */
    function __construct($nestedList) {
        $this->stk = [new Node($nestedList)];
        $this->locate();
    }
    
    /**
     * @return Integer
     */
    function next() {
        $stk = &$this->stk;
        $value = $stk[count($stk)-1]->value();
        ++$stk[count($stk)-1]->index;
        $this->locate();
        return $value;
    }
    
    /**
     * @return Boolean
     */
    function hasNext() {
        $stk = &$this->stk;
        return count($stk) > 0 && !$stk[count($stk)-1]->isEnd();
    }

    private function locate() {
        $stk = &$this->stk;
        while (count($stk) > 0) {
            if ($stk[count($stk)-1]->isEnd()) {
                array_pop($stk);
                if (count($stk) > 0) {
                    ++$stk[count($stk)-1]->index;
                }
            } elseif ($stk[count($stk)-1]->current()->isInteger()) {
                break;
            } else {
                $stk[] = new Node($stk[count($stk)-1]->current()->getList());
            }
        }
    }
}

$work = TestWork::create(new Solution(), 'flatNested', $argv[0]);
// $work->validator = fn($e, $r) => <boolean result>
$work->compareSerial = true;
echo $work->run(Utils::fromStdin());
