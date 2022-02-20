<?php
namespace Soda\Unittest;

class ObjectConverter
{
    private mixed $parser;
    private mixed $serializer;

    function __construct($p, $s)
    {
        $this->parser = $p;
        $this->serializer = $s;
    }

    function fromJsonSerializable($js)
    {
        return ($this->parser)($js);
    }

    function toJsonSerializable($obj)
    {
        return ($this->serializer)($obj);
    }
}