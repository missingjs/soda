<?php
namespace Soda\Leetcode;

class NestedInteger {

    private bool $isAtomic;
    private array $elements = array();
    private ?int $value = 0;

    // if value is not specified, initializes an empty list.
    // Otherwise initializes a single integer equal to value.
    function __construct($value = null) {
        $this->isAtomic = $value != null;
        $this->value = $value;
    }

    // Return true if this NestedInteger holds a single integer, rather than a nested list.
    function isInteger() : bool {
        return $this->isAtomic;
    }

    // Return the single integer that this NestedInteger holds, if it holds a single integer
    // The result is undefined if this NestedInteger holds a nested list
    function getInteger() {
        return $this->value;
    }

    // Set this NestedInteger to hold a single integer.
    function setInteger($i) : void {
        $this->value = $i;
    }

    // Set this NestedInteger to hold a nested list and adds a nested integer to it.
    function add($ni) : void {
        $this->elements[] = $ni;
    }

    // Return the nested list that this NestedInteger holds, if it holds a nested list
    // The result is undefined if this NestedInteger holds a single integer
    function getList() : array {
        return $this->elements;
    }
}

class NestedIntegerFactory
{
    static function parse($d)
    {
        if (!is_array($d)) {
            return new NestedInteger($d);
        }
        $ni = new NestedInteger;
        foreach ($d as $e) {
            $ni->add(self::parse($e));
        }
        return $ni;
    }

    static function serialize($ni)
    {
        return $ni->isInteger()
            ? $ni->getInteger()
            : array_map(fn($i) => self::serialize($i), $ni->getList());
    }
}
