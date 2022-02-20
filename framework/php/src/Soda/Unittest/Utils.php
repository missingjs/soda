<?php
namespace Soda\Unittest;

require_once __DIR__ . "/ConverterFactory.php";

class Utils
{
    static function parseArguments($argTypes, $rawArgs): array
    {
        return array_map(
            fn($i) => ConverterFactory::create($argTypes[$i])->fromJsonSerializable($rawArgs[$i]),
            range(0, count($argTypes)-1)
        );
    }

    static function nanoTime(): int
    {
        [$sec, $nano] = hrtime(false);
        $k = ($sec * 1e9 + $nano);
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

    static function functionTypeHints($filePath, $funcName): array
    {
        $lines = array();
        $fp = fopen($filePath, "r") or die("unable to open file $filePath");
        while (($line = fgets($fp))) {
            if (preg_match("/function\\s+$funcName\\s*[(]/", $line)) {
                break;
            }
            if (strlen(trim($line)) > 0) {
                $lines[] = $line;
            }
        }
        fclose($fp);
        // evict ' */'
        array_pop($lines);
        return self::parseTypeHints($lines);
    }

    private static function parseTypeHints(&$lines): array
    {
        $retType = 'void';
        $typeHints = array();
        while (count($lines) > 0) {
            $text = array_pop($lines);
            $matches = array();
            if (preg_match("/\\* @param\\s+(?<argType>[^ ]+)/", $text, $matches)) {
                $typeHints[] = $matches['argType'];
            } elseif (preg_match("/\\* @return\\s+(?<returnType>[^ ]+)/", $text, $matches)) {
                $retType = $matches['returnType'];
            } else {
                break;
            }
        }
        $typeHints[] = $retType;
        return $typeHints;
    }
}