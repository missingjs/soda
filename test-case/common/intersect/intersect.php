<?php
require_once 'Soda/DS/index.php';
require_once 'Soda/Unittest/index.php';

use Soda\DS;
use Soda\Unittest\TestWork;
use Soda\Unittest\Utils;
use Soda\Unittest\Validators;

class Solution {

    /**
     * @param Integer[] $nums1
     * @param Integer[] $nums2
     * @return Integer[]
     */
    function intersection($nums1, $nums2) {
        if (count($nums1) > count($nums2)) {
            return $this->intersection($nums2, $nums1);
        }
        $mset = array();
        $res = array();
        foreach ($nums1 as $n) {
            $mset[$n] = true;
        }
        foreach ($nums2 as $b) {
            if (array_key_exists($b, $mset)) {
                $res[$b] = true;
            }
        }
        return array_keys($res);
    }
}

$work = TestWork::create(new Solution(), 'intersection', $argv[0]);
$work->validator = Validators::forArray('Integer', false);
$work->compareSerial = true;
echo $work->run(Utils::fromStdin());
