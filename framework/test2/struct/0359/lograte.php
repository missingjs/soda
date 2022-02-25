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

class Logger {
    private $msgMap;
    private $lastTs;
    private $limit = 10;
    /**
     */
    function __construct() {
        $this->msgMap = array();
        $this->lastTs = -$this->limit;
    }
  
    /**
     * @param Integer $timestamp
     * @param String $message
     * @return Boolean
     */
    function shouldPrintMessage($timestamp, $message) {
        $T = $this->lastTs;
        $this->lastTs = $timestamp;

        if ($timestamp - $T >= $this->limit) {
            $this->msgMap = array($message => $timestamp);
            return true;
        }

        $ts = array_key_exists($message, $this->msgMap) 
            ? $this->msgMap[$message]
            : $timestamp - $this->limit;
        if ($timestamp - $ts < $this->limit) {
            return false;
        }

        $this->msgMap[$message] = $timestamp;
        return true;
    }
}

// $work = TestWork::create(new Solution(), 'add', $argv[0]);
$work = TestWork::forStruct(Logger::class, $argv[0]);
// $work->validator = fn($e, $r) => <boolean result>
$work->compareSerial = true;
echo $work->run(Utils::fromStdin());
