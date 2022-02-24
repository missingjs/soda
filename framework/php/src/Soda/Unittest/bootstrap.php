<?php
require_once 'Soda/DS/index.php';
require_once 'Soda/Unittest/index.php';

use Soda\DS;
use Soda\Unittest\TestWork;
use Soda\Unittest\Utils;
use Soda\Unittest\Validators;

class Solution {

    /**
     * @param Integer $a
     * @param Integer $b
     * @return Integer
     */
    function add($a, $b) {
        return $a + $b;
    }
}

$work = TestWork::create(new Solution(), 'add', $argv[0]);
// $work->validator = fn($e, $r) => <boolean result>
$work->compareSerial = true;
echo $work->run(Utils::fromStdin());
