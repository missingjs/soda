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

class TopVotedCandidate {
    private $N;
    private $times;
    private $winner;
    /**
     * @param Integer[] $persons
     * @param Integer[] $times
     */
    function __construct($persons, $times) {
        $n = count($persons);
        $winner = array_fill(0, $n, 0);
        $this->N = $n;
        $this->times =& $times;
        $this->winner =& $winner;
        $counter = array_fill(0, $n+1, 0);
        $win = 0;
        for ($i = 0; $i < $n; ++$i) {
            if (++$counter[$persons[$i]] >= $counter[$win]) {
                $win = $persons[$i];
            }
            $winner[$i] = $win;
        }
    }
  
    /**
     * @param Integer $t
     * @return Integer
     */
    function q($t) {
        if ($t >= end($this->times)) {
            return $this->winner[$this->N-1];
        }
        $low = 0;
        $high = $this->N - 1;
        while ($low < $high) {
            $mid = ($low + $high) >> 1;
            if ($t <= $this->times[$mid]) {
                $high = $mid;
            } else {
                $low = $mid + 1;
            }
        }
        $idx = $t == $this->times[$low] ? $low : $low - 1;
        return $this->winner[$idx];
    }
}

// $work = TestWork::create(new Solution(), 'add', $argv[0]);
$work = TestWork::forStruct(TopVotedCandidate::class, $argv[0]);
// $work->validator = fn($e, $r) => <boolean result>
$work->compareSerial = true;
echo $work->run(Utils::fromStdin());

