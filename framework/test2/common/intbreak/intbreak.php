<?php
require_once 'Soda/DS/index.php';
require_once 'Soda/Unittest/index.php';

use Soda\DS;
use Soda\Unittest\TestWork;
use Soda\Unittest\Utils;

class Solution {

    private $memo;

    /**
     * @param Integer n
     * @return Integer
     */
    function integerBreak($n) {
        $this->memo = array_fill(0, 59, 0);
        return $this->solve($n);
    }

    private function solve($n) {
        if ($n == 1) {
            return 1;
        }
        if ($this->memo[$n] > 0) {
            return $this->memo[$n];
        }
        $res = 0;
        for ($i = 1; $i < $n; ++$i) {
            $res = max($i * ($n-$i), $i * $this->solve($n-$i), $res);
        }
        $this->memo[$n] = $res;
        return $res;
    }
}

$work = TestWork::create(new Solution(), 'integerBreak', $argv[0]);
// $work->validator = fn($e, $r) => <boolean result>
$work->compareSerial = true;
echo $work->run(Utils::fromStdin());
