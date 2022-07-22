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

class Tweet {
    public $id;
    public $ts;
    function __construct($id, $ts) {
        $this->id = $id;
        $this->ts = $ts;
    }
}

class Node {
    private $tweets;
    public $index;
    function __construct(&$t) {
        $this->tweets =& $t;
        $this->index = count($t) - 1;
    }

    function current() {
        return $this->tweets[$this->index];
    }

    function isEnd() {
        return $this->index == -1;
    }
}

class Twitter {
    private $timestamp = 0;
    private $tweets = array();    // key: id, value: tweet array
    private $follows = array();   // key: id, value: user id set
    private $Limit = 10;

    /**
     */
    function __construct() {
        
    }
  
    /**
     * @param Integer $userId
     * @param Integer $tweetId
     * @return NULL
     */
    function postTweet($userId, $tweetId) {
        $this->tweets[$userId][] = new Tweet($tweetId, $this->nextTimestamp());
        if (count($this->tweets[$userId]) > $this->Limit) {
            array_shift($this->tweets[$userId]);
        }
    }
  
    /**
     * @param Integer $userId
     * @return Integer[]
     */
    function getNewsFeed($userId) {
        $pq = new \SplPriorityQueue;
        $s = array_keys(($this->follows[$userId]) ?? []);
        $s[] = $userId;
        foreach ($s as $user) {
            if (count($this->tweets[$user] ?? []) > 0) {
                $node = new Node($this->tweets[$user]);
                $pq->insert($node, $node->current()->ts);
            }
        }

        $res = array();
        for ($i = 0; $i < $this->Limit && !$pq->isEmpty(); ++$i) {
            $node = $pq->extract();
            $res[] = $node->current()->id;
            --$node->index;
            if (!$node->isEnd()) {
                $pq->insert($node, $node->current()->ts);
            }
        }
        return $res;
    }
  
    /**
     * @param Integer $followerId
     * @param Integer $followeeId
     * @return NULL
     */
    function follow($followerId, $followeeId) {
        $this->follows[$followerId][$followeeId] = true;
    }
  
    /**
     * @param Integer $followerId
     * @param Integer $followeeId
     * @return NULL
     */
    function unfollow($followerId, $followeeId) {
        unset($this->follows[$followerId][$followeeId]);
    }

    private function nextTimestamp() {
        return ++$this->timestamp;
    }
}

// $work = TestWork::create(new Solution(), 'add', $argv[0]);
$work = TestWork::forStruct(Twitter::class, $argv[0]);
// $work->validator = fn($e, $r) => <boolean result>
$work->compareSerial = true;
echo $work->run(Utils::fromStdin());

