<?php
namespace Soda\DS;

require_once __DIR__ . '/SkipList.php';

class SortedMap
{
    private SkipList $skipList;

    function __construct($cmp = null)
    {
        if ($cmp == null) {
            $cmp = fn($a, $b) => $a - $b;
        }
        $this->skipList = new SkipList(10, 0.5, $cmp);
    }

    function has($key): bool
    {
        return $this->skipList->query($key) != null;
    }

    function get($key, $default = null)
    {
        $node = $this->skipList->query($key);
        return $node != null ? $node->value : $default;
    }

    function size(): int
    {
        return $this->skipList->size();
    }

    function set($key, $value)
    {
        $this->skipList->update($key, $value);
    }

    function del($key)
    {
        $this->skipList->remove($key);
    }

    function lowerBound($key): ?array
    {
        return $this->skipList->lowerBound($key);
    }

    function iterate(): \Generator
    {
        yield from $this->skipList->items();
    }

}
