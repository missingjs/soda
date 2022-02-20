<?php
namespace Soda\Unittest;

require_once __DIR__ . "/ObjectConverter.php";

class ConverterFactory
{
    private static array $factoryMap = array();

    static function create(string $type): ObjectConverter
    {
        if (array_key_exists($type, self::$factoryMap)) {
            return self::$factoryMap[$type]();
        }
        return new ObjectConverter(fn($j) => $j, fn($s) => $s);
    }
}