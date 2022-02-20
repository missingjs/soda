<?php
namespace Soda\Unittest;

require_once __DIR__ . "/ObjectFeature.php";

class FeatureFactory
{
    private static array $factoryMap = array();

    static function create(string $type): ObjectFeature
    {
        if (array_key_exists($type, self::$factoryMap)) {
            return self::$factoryMap[$type]();
        }
        return new ObjectFeature(
            fn($obj) => Utils::hashCode($obj),
            fn($x, $y) => $x == $y
        );
    }
}