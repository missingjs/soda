<?php
require_once 'Soda/DS/index.php';
require_once 'Soda/Unittest/index.php';

use Soda\DS;
use Soda\Unittest\TestWork;
use Soda\Unittest\Utils;

class Solution {

    /**
     * @param Integer[] $nums
     * @param Integer $k
     * @param Integer $t
     * @return Boolean
     */
    function containsNearbyAlmostDuplicate($nums, $k, $t) {
        $imap = new DS\SortedMap;
        [$i, $j] = [0, 0];
        while ($j < count($nums)) {
            if ($j - $i <= $k) {
                $val = $nums[$j++];
                $lower = $val - $t;
                $upper = $val + $t;
                $_pair = $imap->lowerBound($lower);
                if ($_pair != null && $_pair[0] <= $upper) {
                    return true;
                }
                $imap->set($val, $imap->get($val, 0) + 1);
            } else {
                $val = $nums[$i++];
                $c = $imap->get($val) - 1;
                if ($c == 0) {
                    $imap->del($val);
                } else {
                    $imap->set($val, $c);
                }
            }
        }
        return false;
    }
}

$work = TestWork::create(new Solution(), 'containsNearbyAlmostDuplicate', $argv[0]);
// $work->validator = fn($e, $r) => <boolean result>
$work->compareSerial = true;
echo $work->run(Utils::fromStdin());
