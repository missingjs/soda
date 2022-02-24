<?php
namespace Soda\Unittest;

require_once "Soda/Leetcode/index.php";
require_once __DIR__ . "/ObjectConverter.php";

use Soda\Leetcode\ListFactory;

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

    private static function registerFactory($type, $parser, $serializer)
    {
        self::regFact($type, fn() => new ObjectConverter($parser, $serializer));
    }

    private static function regFact($type, $factory)
    {
        self::$factoryMap[$type] = $factory;
    }

    static function __init()
    {
        self::registerFactory(
            'ListNode',
            fn($d) => ListFactory::create($d),
            fn($n) => ListFactory::dump($n)
        );

        self::registerFactory(
            'ListNode[]',
            fn($ds) => array_map(fn($d) => ListFactory::create($d), $ds),
            fn($ns) => array_map(fn($n) => ListFactory::dump($n), $ns)
        );
    }
}

ConverterFactory::__init();
