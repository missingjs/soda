<?php
require_once 'Soda/DS/index.php';
require_once 'Soda/Unittest/index.php';

use Soda\DS;
use Soda\Unittest\TestWork;
use Soda\Unittest\Utils;

class Solution {

    /**
     * @param String $s
     * @return String
     */
    function reverseVowels($s) {
        $isv = array_fill(0, 128, false);
        $vowels = "aeiouAEIOU";
        for ($i = 0; $i < strlen($vowels); ++$i) {
            $ch = $vowels[$i];
            $isv[ord($ch)] = true;
        }
        $buf = str_split($s);
        [$i, $j] = [0, strlen($s)-1];
        while ($i < $j) {
            while ($i < $j && !$isv[ord($buf[$i])]) {
                ++$i;
            }
            while ($i < $j && !$isv[ord($buf[$j])]) {
                --$j;
            }
            if ($i < $j) {
                [$buf[$i], $buf[$j]] = [$buf[$j], $buf[$i]];
                ++$i;
                --$j;
            }
        }
        return join('', $buf);
    }
}

$work = TestWork::create(new Solution(), 'reverseVowels', $argv[0]);
// $work->validator = fn($e, $r) => <boolean result>
$work->compareSerial = true;
echo $work->run(Utils::fromStdin());
