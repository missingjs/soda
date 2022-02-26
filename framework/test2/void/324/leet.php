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

class VirIndex {
    private $nums;

    function __construct(&$nums) {
        $this->nums =& $nums;
    }

    function get($index) {
        return $this->nums[$this->mapIndex($index)];
    }

    function set($index, $value) {
        $this->nums[$this->mapIndex($index)] = $value;
    }

    private function mapIndex($i) {
        $N = count($this->nums);
        if (($N&1) == 1 || $i > (($N-1)>>1)) {
            return ((($N-$i) << 1) - 1) % $N;
        } else {
            return ($N - 2 - ($i << 1));
        }
    }
}

class Solution {

    /**
     * @param Integer[] $nums
     * @return NULL
     */
    function wiggleSort(&$nums) {
        $vi = new VirIndex($nums);
        $N = count($nums);
        $this->quickSelect($vi, 0, $N-1, intdiv($N-1,2));
    }

    private function quickSelect($vi, $start, $end, $k) {
        while ($start < $end) {
            $p = $this->partition($vi, $start, $end);
            if ($k >= $p[0] && $k <= $p[1]) {
                return;
            }
            if ($k > $p[1]) {
                $start = $p[1] + 1;
            } else {
                $end = $p[0] - 1;
            }
        }
    }

    private function partition($vi, $start, $end) {
        $mid = intdiv($start + $end, 2);
        $pivot = $this->getMedian($vi->get($start), $vi->get($mid), $vi->get($end));
        $p = $start;
        $z = $end + 1;
        for ($q = $start; $q < $z; ) {
            if ($vi->get($q) < $pivot) {
                $temp = $vi->get($p);
                $vi->set($p, $vi->get($q));
                $vi->set($q, $temp);
                ++$p;
                ++$q;
            } elseif ($vi->get($q) == $pivot) {
                ++$q;
            } else {
                --$z;
                $temp = $vi->get($z);
                $vi->set($z, $vi->get($q));
                $vi->set($q, $temp);
            }
        }
        return array($p, $z-1);
    }

    private function getMedian($a, $b, $c) {
        if ($a >= $b) {
            return $b >= $c ? $b : min($a, $c);
        } else {
            return $a >= $c ? $a : min($b, $c);
        }
    }
}

$work = TestWork::create(new Solution(), 'wiggleSort', $argv[0]);
// $work = TestWork::forStruct('STRUCT::class', $argv[0]);
$work->validator = function($e, $nums) {
    for ($i = 1; $i < count($nums); ++$i) {
        if ($i % 2 != 0 && $nums[$i] <= $nums[$i-1]
                || $i % 2 == 0 && $nums[$i] >= $nums[$i-1]) {
            return false;
        }
    }
    return true;
};
$work->compareSerial = true;
echo $work->run(Utils::fromStdin());

