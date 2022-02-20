<?php
namespace Soda\Unittest;

class Utils
{
    static function parseArguments($argTypes, $rawArgs)
    {
        // TODO
        return $rawArgs;
    }

    static function nanoTime(): int
    {
        [$sec, $nano] = hrtime(false);
        $k = ($sec * 1e9 + $nano);
        fwrite(STDERR, "time nano: $k\n");
        return (int) ($sec * 1e9 + $nano);
    }

    static function microTime(): int
    {
        [$sec, $nano] = hrtime(false);
        return (int) ($sec * 1e6 + $nano / 1e3);
    }

    static function fromStdin(): string
    {
        $content = "";
        while (false !== ($line = fgets(STDIN))) {
            $content .= $line;
        }
        return $content;
    }

    static function hashCode($obj): int
    {
        $jsonStr = json_encode($obj);
        $hashStr = hash('sha1', $jsonStr);
        $hash = 0;
        for ($i = 0, $len = strlen($hashStr); $i < $len; ++$i) {
            $hash = $hash * 31 + ord($hashStr[$i]);
            $hash &= 0x7fffffff;
        }
        return $hash;
    }
}