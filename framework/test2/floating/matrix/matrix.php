<?php
require_once 'Soda/DS/index.php';
require_once 'Soda/Unittest/index.php';

use Soda\DS;
use Soda\Unittest\TestWork;
use Soda\Unittest\Utils;

class Solution {

    /**
     * @param Double[][] $a
     * @param Double[][] $b
     * @return Double[][]
     */
    function matrixMultiply($a, $b) {
        $rows = count($a);
        $cols = count($b[0]);
        $res = array_fill(0, $rows, array_fill(0, $cols, 0));
        foreach (range(0, $rows-1) as $i) {
            foreach (range(0, $cols-1) as $j) {
                $c = 0;
                foreach (range(0, count($b)-1) as $k) {
                    $c += $a[$i][$k] * $b[$k][$j];
                }
                $res[$i][$j] = $c;
            }
        }
        return $res;
    }
}

$work = TestWork::create(new Solution(), 'matrixMultiply', $argv[0]);
// $work->validator = fn($e, $r) => <boolean result>
// $work->compareSerial = true;
echo $work->run(Utils::fromStdin());
