<?php
require_once 'Soda/Unittest/index.php';

use Soda\Unittest\TestWork;
use Soda\Unittest\Utils;

class Solution {

    /**
     * @param Character $ch
     * @return Character
     */
    function nextChar($ch) {
        return chr(ord($ch) + 1);
    }
}

$work = TestWork::create(new Solution(), 'nextChar', $argv[0]);
// $work->validator = fn($e, $r) => <boolean result>
$work->compareSerial = true;
echo $work->run(Utils::fromStdin());
