<?php
namespace Soda\Unittest;

require_once __DIR__ . "/ObjectFeature.php";
require_once __DIR__ . "/XMap.php";

class ListFeatureFactory
{
    static function ordered(ObjectFeature $elemFeat): ObjectFeature
    {
        $hash = function (array $obj) use ($elemFeat): int {
            $res = 0;
            foreach ($obj as $e) {
                $h = $elemFeat->hash($e);
                $res = $res * 133 + $h;
                $res &= 0x7fffffff;
            }
            return $res;
        };
        $equal = function (array $x, array $y) use ($elemFeat): bool {
            if (count($x) !== count($y)) {
                return false;
            }
            foreach (range(0, count($x)-1) as $i) {
                if (!$elemFeat->isEqual($x[$i], $y[$i])) {
                    return false;
                }
            }
            return true;
        };
        return new ObjectFeature($hash, $equal);
    }

    static function unordered(ObjectFeature $elemFeat): ObjectFeature
    {
        $hash = function (array $obj) use ($elemFeat): int {
            $hashArr = array_map(fn($e) => $elemFeat->hash($e), $obj);
            asort($hashArr, SORT_NUMERIC);
            $res = 0;
            foreach ($hashArr as $h) {
                $res = $res * 133 + $h;
                $res &= 0x7fffffff;
            }
            return $res;
        };
        $equal = function(array $x, array $y) use ($elemFeat): bool {
            if (count($x) !== count($y)) {
                return false;
            }
            $xmap = new XMap($elemFeat);
            foreach ($x as $e) {
                $xmap->set($e, $xmap->get($e, 0) + 1);
            }
            foreach ($y as $e) {
                if (!$xmap->has($e)) {
                    return false;
                }
                $c = $xmap->get($e) - 1;
                if ($c > 0) {
                    $xmap->set($e, $c);
                } else {
                    $xmap->delete($e);
                }
            }
            return true;
        };
        return new ObjectFeature($hash, $equal);
    }
}
