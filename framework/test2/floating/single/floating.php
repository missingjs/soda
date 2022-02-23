<?php
require_once 'Soda/DS/index.php';
require_once 'Soda/Unittest/index.php';

use Soda\DS;
use Soda\Unittest\TestWork;
use Soda\Unittest\Utils;

class Solution {

    /**
     * @param Double $x
     * @param Double $y
     * @return Double
     */
    function divide($a, $b) {
        return $a / $b;
    }
}

$work = TestWork::create(new Solution(), 'divide', $argv[0]);
// $work->validator = fn($e, $r) => <boolean result>
// $work->compareSerial = true;
echo $work->run(Utils::fromStdin());
