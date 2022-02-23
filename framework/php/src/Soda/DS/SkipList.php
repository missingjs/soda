<?php
namespace Soda\DS;

class SkipNode
{
    public int $level;
    public array $link;
    public mixed $key;
    public mixed $value;

    function __construct($level, $key, $value)
    {
        $this->level = $level;
        $this->link = array_fill(0, $level, null);
        $this->key = $key;
        $this->value = $value;
    }

    static function create($maxLevel, $p, $key, $value = null): SkipNode
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
    private int $maxLevel;
    private float $prob;
    private SkipNode $head;
    private array $footbuf;
    private mixed $cmpFunc;
    private int $size;

    function __construct($maxLevel, $p, $cmp = null)
    {
        $this->maxLevel = $maxLevel;
        $this->prob = $p;
        $this->cmpFunc = $cmp ? $cmp : fn($a, $b) => $a - $b;
        $this->head = new SkipNode($maxLevel, null, null);
        $this->footbuf = array_fill(0, $maxLevel, null);
        $this->size = 0;
    }

    function size(): int
    {
        return $this->size;
    }

    function query($key)
    {
        return $this->find($key, $this->footbuf);
    }

    function queryValue($key) 
    {
        $node = $this->find($key, $this->footbuf);
        return $node && $this->cmp($node->key, $key) == 0 ? $node->value : null;
    }

    function lowerBound($key): ?array
    {
        $node = $this->find($key, $this->footbuf);
        return $node ? [$node->key, $node->value] : null;
    }

    function insert($key, $value)
    {
        $this->update($key, $value);
    }

    function update($key, $value)
    {
        $footprint = $this->footbuf;
        $node = $this->find($key, $footprint);
        if (!$node || $this->cmp($node->key, $key) != 0) {
            $node = $this->do_insert($key, $footprint);
        }
        $node->value = $value;
    }

    function remove($key)
    {
        $footprint = $this->footbuf;
        $node = $this->find($key, $footprint);
        if ($node && $this->cmp($node->key, $key) == 0) {
            for ($lv = 0; $lv < $node->level; ++$lv) {
                $footprint[$lv]->link[$lv] = $node->link[$lv];
            }
            --$this->size;
        }
    }

    function items(): \Generator
    {
        $p = $this->head->link[0];
        while ($p) {
            yield [$p->key, $p->value];
            $p = $p->link[0];
        }
    }

    private function do_insert($key, &$footprint): SkipNode
    {
        $node = SkipNode::create($this->maxLevel, $this->prob, $key);
        for ($lv = 0; $lv < $node->level; ++$lv) {
            $next = $footprint[$lv]->link[$lv];
            $footprint[$lv]->link[$lv] = $node;
            $node->link[$lv] = $next;
        }
        ++$this->size;
        return $node;
    }

    private function find($key, &$footprint)
    {
        $node = $this->head;
        $lv = $this->maxLevel - 1;
        while ($lv >= 0) {
            if ($node->link[$lv] && $this->cmp($node->link[$lv]->key, $key) < 0) {
                $node = $node->link[$lv];
            } else {
                $footprint[$lv] = $node;
                --$lv;
            }
        }
        return $node->link[0];
    }

    private function cmp($a, $b)
    {
        return ($this->cmpFunc)($a, $b);
    }
}
