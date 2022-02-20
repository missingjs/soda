<?php
namespace Soda\Unittest;

class ObjectFeature
{
    private mixed $hashFunc;
    private mixed $equalFunc;

    function __construct($hash, $equal)
    {
        $this->hashFunc = $hash;
        $this->equalFunc = $equal;
    }

    function hash($obj)
    {
        return ($this->hashFunc)($obj);
    }

    function isEqual($x, $y)
    {
        return ($this->equalFunc)($x, $y);
    }
}