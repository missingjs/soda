<?php
require_once 'Soda/DS/index.php';
require_once 'Soda/Unittest/index.php';

use Soda\DS;
use Soda\Unittest\TestWork;
use Soda\Unittest\Utils;
use Soda\Unittest\Validators;

class Solution {

    /**
     * @param String[][] $equations
     * @param Float[] $values
     * @param String[][] $queries
     * @return Float[]
     */
    function calcEquation($equations, $values, $queries) {
        $indexMap = $this->getIndexMap($equations);
        $N = count($indexMap);
        $table = array_fill(0, $N, array_fill(0, $N, -1));

        for ($k = 0; $k < count($values); ++$k) {
            $p = $equations[$k];
            $i = $indexMap[$p[0]];
            $j = $indexMap[$p[1]];
            $table[$i][$j] = $values[$k];
            $table[$j][$i] = 1 / $values[$k];
        }

        $res = array_fill(0, count($queries), 0);
        for ($i = 0; $i < count($res); ++$i) {
            [$a, $b] = $queries[$i];
            if (!array_key_exists($a, $indexMap) || !array_key_exists($b, $indexMap)) {
                $res[$i] = -1;
                continue;
            }
            $ai = $indexMap[$a];
            $bi = $indexMap[$b];
            if ($ai === $bi) {
                $res[$i] = 1;
                continue;
            }
            $visited = array_fill(0, $N, 0);
            $res[$i] = $this->dfs($ai, $bi, $table, $visited);
        }
        return $res;
    }

    private function getIndexMap($eqs) {
        $imap = array();
        foreach ($eqs as $e) {
            [$a, $b] = $e;
            if (!array_key_exists($a, $imap)) {
                $imap[$a] = count($imap);
            }
            if (!array_key_exists($b, $imap)) {
                $imap[$b] = count($imap);
            }
        }
        return $imap;
    }

    private function dfs($ai, $bi, &$table, &$visited) {
        if ($table[$ai][$bi] >= 0) {
            return $table[$ai][$bi];
        }
        $visited[$ai] = 1;
        $res = -1;
        for ($adj = 0; $adj < count($table); ++$adj) {
            if ($table[$ai][$adj] >= 0 && !$visited[$adj]) {
                $v = $this->dfs($adj, $bi, $table, $visited);
                if ($v >= 0) {
                    $res = $table[$ai][$adj] * $v;
                    break;
                }
            }
        }
        $table[$ai][$bi] = $res;
        $table[$bi][$ai] = 1 / $res;
        return $res;
    }
}

$work = TestWork::create(new Solution(), 'calcEquation', $argv[0]);
// $work->validator = fn($e, $r) => <boolean result>
// $work->compareSerial = true;
echo $work->run(Utils::fromStdin());
