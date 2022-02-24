<?php
namespace Soda\Unittest;

require_once __DIR__ . "/ListFeatureFactory.php";
require_once __DIR__ . "/FeatureFactory.php";

class Validators
{
    static function forArray($type, $ordered)
    {
        return self::for_arr($ordered, FeatureFactory::create($type));
    }

    static function forArray2d($type, $dim1Ordered, $dim2Ordered)
    {
        $elemFeat = FeatureFactory::create($type);
        $method = $dim2Ordered ? "ordered" : "unordered";
        $d = ListFeatureFactory::$method($elemFeat);
        return self::for_arr($dim1Ordered, $d);
    }

    private static function for_arr($ordered, $elemFeat)
    {
        $method = $ordered ? "ordered" : "unordered";
        $listFeat = ListFeatureFactory::$method($elemFeat);
        return fn($x, $y) => $listFeat->isEqual($x, $y);
    }
}

