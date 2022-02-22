<?php
// namespace Soda\DS;

class SkipNode
{
    public $level;
    public $link;
    public $key;
    public $value;

    function __construct($level, $key, $value)
    {
        $this->level = $level;
        $this->link = array_fill(0, $level, null);
        $this->key = $key;
        $this->value = $value;
    }

    static function create($maxLevel, $p, $key, $value = null)
    {
        $level = 1;
        while (mt_rand() / mt_getrandmax() < $p && $level < $maxLevel) {
            ++$level;
        }
        return new SkipNode($level, $key, $value);
    }
}

class SkipList
{
    private $maxLevel;
    private $prob;
    private $head;
    private $footbuf;

    function __construct($maxLevel, $p)
    {
        $this->maxLevel = $maxLevel;
        $this->prob = $p;
        $this->head = new SkipNode($maxLevel, null, null);
        $this->footbuf = array_fill(0, $maxLevel, null);
    }

    function queryValue($key) 
    {
        $node = $this->find($key, $this->footbuf);
        return $node && $node->key == key ? $node->value : null;
    }

    function lowerBound($key)
    {
        $node = $this->find($key, $this->footbuf);
        return $node ? $node->key : null;
    }

    function insert($key, $value)
    {
        $this->update($key, $value);
    }

    function update($key, $value)
    {
        $footprint = $this->footbuf;
        $node = $this->find($key, $footprint);
        if (!$node || $node->key != $key) {
            $node = $this->do_insert($key, $footprint);
        }
        $node->value = $value;
    }

    function remove($key)
    {
        $footprint = $this->footbuf;
        $node = $this->find($key, $footprint);
        if ($node && $node->key == $key) {
            for ($lv = 0; $lv < count($node->level); ++$lv) {
                $footprint[$lv]->link[$lv] = $node->link[$lv];
            }
        }
    }

    function items()
    {
        $p = $this->head->link[0];
        while ($p) {
            yield [$p->key, $p->value];
            $p = $p->link[0];
        }
    }

    private function do_insert($key, &$footprint)
    {
        $node = SkipNode::create($this->maxLevel, $this->prob, $key);
        for ($lv = 0; $lv < $node->level; ++$lv) {
            $next = $footprint[$lv]->link[$lv];
            $footprint[$lv]->link[$lv] = $node;
            $node->link[$lv] = $next;
        }
        return $node;
    }

    private function find($key, &$footprint)
    {
        $node = $this->head;
        $lv = $this->maxLevel - 1;
        while ($lv >= 0) {
            if ($node->link[$lv] && $node->link[$lv]->key < $key) {
                $node = $node->link[$lv];
            } else {
                $footprint[$lv] = $node;
                --$lv;
            }
        }
        return $node->link[0];
    }
}

$slist = new SkipList(10, 0.5);
$values = [3,9,7,6,12,19,17,26,21,25];
foreach ($values as $val) {
    $slist->insert($val, "value of $val");
}
foreach ($slist->items() as [$key, $value]) {
    echo "$key => $value\n";
}

