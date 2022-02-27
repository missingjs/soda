<?php
require_once 'Soda/DS/index.php';
require_once 'Soda/Leetcode/index.php';
require_once 'Soda/Unittest/index.php';

use Soda\DS;
use Soda\Leetcode\ListNode;
use Soda\Leetcode\NestedInteger;
use Soda\Leetcode\TreeNode;
use Soda\Unittest\TestWork;
use Soda\Unittest\Utils;
use Soda\Unittest\Validators;

class NumMatrix {
    private $rows;
    private $cols;
    private $mx;
    private $bit;

    /**
     * @param Integer[][] $matrix
     */
    function __construct($matrix) {
        $this->rows = count($matrix) + 1;
        $this->cols = count($matrix[0]) + 1;
        $this->mx = array_fill(0, count($matrix), array_fill(0, count($matrix[0]), 0));
        $this->bit = array_fill(0, $this->rows, array_fill(0, $this->cols, 0));
        for ($i = 0; $i < $this->rows-1; ++$i) {
            for ($j = 0; $j < $this->cols-1; ++$j) {
                $this->update($i, $j, $matrix[$i][$j]);
            }
        }
    }

    /**
     * @param Integer row
     * @param Integer col
     * @param Integer val
     * @return NULL
     */
    function update($row, $col, $val) {
        $diff = $val - $this->mx[$row][$col];
        $this->mx[$row][$col] = $val;
        $i = $row + 1;
        while ($i < $this->rows) {
            $j = $col + 1;
            while ($j < $this->cols) {
                $this->bit[$i][$j] += $diff;
                $j += ($j & -$j);
            }
            $i += ($i & -$i);
        }
    }

    /**
     * @param Integer row1
     * @param Integer col1
     * @param Integer row2
     * @param Integer col2
     * @return Integer
     */
    function sumRegion($row1, $col1, $row2, $col2) {
        return $this->query($row1, $col1)
            - $this->query($row1, $col2+1)
            - $this->query($row2+1, $col1)
            + $this->query($row2+1, $col2+1);
    }

    private function query($r, $c) {
        $res = 0;
        $i = $r;
        while ($i > 0) {
            $j = $c;
            while ($j > 0) {
                $res += $this->bit[$i][$j];
                $j -= ($j & -$j);
            }
            $i -= ($i & -$i);
        }
        return $res;
    }
}

// $work = TestWork::create(new Solution(), 'add', $argv[0]);
$work = TestWork::forStruct(NumMatrix::class, $argv[0]);
// $work->validator = fn($e, $r) => <boolean result>
$work->compareSerial = true;
echo $work->run(Utils::fromStdin());

