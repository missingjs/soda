<?php
namespace Soda\Unittest;

require_once __DIR__ . "/ObjectFeature.php";

class XEntry
{
    public mixed $key;
    public mixed $value;
    function __construct($key, $value)
    {
        $this->key = $key;
        $this->value = $value;
    }
}

class XMap
{
    private ObjectFeature $feat;
    private array $dict;
    private int $size;

    function __construct(ObjectFeature $feat)
    {
        $this->feat = $feat;
        $this->dict = array();
        $this->size = 0;
    }

    private function _hash($key)
    {
        return $this->feat->hash($key);
    }

    private function _entry($key): ?XEntry
    {
        $h = $this->_hash($key);
        if (!array_key_exists($h, $this->dict)) {
            return null;
        }
        foreach ($this->dict[$h] as $e) {
            if ($this->feat->isEqual($key, $e->key)) {
                return $e;
            }
        }
        return null;
    }

    function has($key): bool
    {
        return !is_null($this->_entry($key));
    }

    function get($key, $default = null)
    {
        $e = $this->_entry($key);
        return !is_null($e) ? $e->value : $default;
    }

    function set($key, $value)
    {
        $e = $this->_entry($key);
        if (is_null($e)) {
            $h = $this->_hash($key);
            if (!array_key_exists($h, $this->dict)) {
                $this->dict[$h] = [];
            }
            $this->dict[$h][] = new XEntry($key, $value);
            ++$this->size;
        } else {
            $e->value = $value;
        }
    }

    function delete($key)
    {
        $h = $this->_hash($key);
        if (!array_key_exists($h, $this->dict)) {
            return;
        }
        $index = 0;
        $elist = &$this->dict[$h];
        for (; $index < count($elist); ++$index) {
            if ($this->feat->isEqual($key, $elist[$index]->key)) {
                array_splice($elist, $index, 1);
                return;
            }
        }
    }
}
