<?php
require_once 'Soda/Unittest/index.php';

use Soda\Unittest\TestWork;
use Soda\Unittest\Utils;

class Solution {

    /**
     * @param String[] $chars
     * @return String[]
     */
    function doubleList($chars) {
        $arr = array();
        array_push($arr, ...$chars);
        array_push($arr, ...$chars);
        return $arr;
    }
}

$work = TestWork::create(new Solution(), 'doubleList', $argv[0]);
// $work->validator = fn($e, $r) => <boolean result>
$work->compareSerial = true;
echo $work->run(Utils::fromStdin());
