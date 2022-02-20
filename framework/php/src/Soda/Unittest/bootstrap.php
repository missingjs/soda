<?php
require_once 'Soda/Unittest/index.php';

use Soda\Unittest\TestWork;
use Soda\Unittest\Utils;

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

$work = TestWork::create(new Solution(), 'add', ['Integer', 'Integer', 'Integer']);
echo $work->run(Utils::fromStdin());
