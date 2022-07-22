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

    private $p;

    /**
     * @param String $s
     * @return NestedInteger
     */
    function deserialize($s) {
        $this->p = 0;
        return $this->parse($s);
    }

    private function parse($s) {
        if ($s[$this->p] === '[') {
            ++$this->p;
            $root = new NestedInteger;
            while ($s[$this->p] !== ']') {
                $root->add($this->parse($s));
                if ($s[$this->p] === ',') {
                    ++$this->p;
                }
            }
            ++$this->p;
            return $root;
        }

        $negative = false;
        if ($s[$this->p] === '-') {
            ++$this->p;
            $negative = true;
        }

        $value = 0;
        $zeroAscii = ord('0');
        while ($this->p < strlen($s) && $s[$this->p]>='0' && $s[$this->p]<='9') {
            $value = $value * 10 + ord($s[$this->p]) - $zeroAscii;
            ++$this->p;
        }

        if ($negative) {
            $value = -$value;
        }
        return new NestedInteger($value);
    }
}

$work = TestWork::create(new Solution(), 'deserialize', $argv[0]);
// $work->validator = fn($e, $r) => <boolean result>
$work->compareSerial = true;
echo $work->run(Utils::fromStdin());
