<?php
namespace Soda\Unittest;

require_once __DIR__ . "/ObjectFeature.php";

class FeatureFactory
{
    private static array $factoryMap = array();

    static function create(string $type): ObjectFeature
    {
        $c = count(self::$factoryMap);
        if (array_key_exists($type, self::$factoryMap)) {
            return self::$factoryMap[$type]();
        }
        return new ObjectFeature(
            fn($obj) => Utils::hashCode($obj),
            fn($x, $y) => $x === $y
        );
    }

    private static function registerFactory($type, $hash, $equal)
    {
        self::regFact($type, fn() => new ObjectFeature($hash, $equal));
    }

    private static function regFact($type, $factory)
    {
        self::$factoryMap[$type] = $factory;
    }

    private static function regAlias($type, $alias)
    {
        self::regFact($alias, fn() => self::create($type));
    }

    static function __init()
    {
        self::registerFactory(
            'double',
            fn($obj) => Utils::hashCode($obj),
            fn($x, $y) => abs($x-$y) < 1e-6
        );
        self::regAlias('double', 'Double');
    }
}

FeatureFactory::__init();

