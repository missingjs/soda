<?php
require_once 'Soda/Unittest/index.php';

use Soda\Unittest\TestWork;
use Soda\Unittest\Utils;

class Solution {

    /**
     * @param string[][] $matrix
     * @return string[][]
     */
    function toUpper($matrix) {
        $diff = ord('a') - ord('A');
        for ($i = 0; $i < count($matrix); ++$i) {
            for ($j = 0; $j < count($matrix[0]); ++$j) {
                $matrix[$i][$j] = chr(ord($matrix[$i][$j]) - $diff);
            }
        }
        return $matrix;
    }
}

$work = TestWork::create(new Solution(), 'toUpper', $argv[0]);
// $work->validator = fn($e, $r) => <boolean result>
$work->compareSerial = true;
echo $work->run(Utils::fromStdin());
