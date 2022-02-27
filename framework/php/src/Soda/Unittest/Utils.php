<?php
namespace Soda\Unittest;

require_once __DIR__ . "/ConverterFactory.php";

class Utils
{
    static function parseArguments($argTypes, $rawArgs): array
    {
        if (count($argTypes) == 0) {
            return array();
        }
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
            $line = trim($line);
            if (preg_match("/function\\s+$funcName\\s*[(]/", $line)) {
                break;
            }
            if (strlen($line) > 0) {
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
            if (preg_match("/\\* @param\\s+(?<argType>\\S+)/", $text, $matches)) {
                $typeHints[] = $matches['argType'];
            } elseif (preg_match("/\\* @return\\s+(?<returnType>\\S+)/", $text, $matches)) {
                $retType = $matches['returnType'];
            } else {
                break;
            }
        }
        $typeHints = array_reverse($typeHints);
        $typeHints[] = $retType;
        return $typeHints;
    }

    static function methodTypeHints($filePath, $className): array
    {
        $hintsMap = array();
        foreach (self::parseMethodDesc($filePath, $className) as $desc) {
            $matches = array();
            if (preg_match('/^\s+function\s+__construct[(]/', $desc['declare'], )) {
                $hintsMap['__construct'] = $desc['typeHints'];
            } elseif (preg_match('/function\s+(?<methodName>\w+)/', $desc['declare'], $matches)) {
                $hintsMap[$matches['methodName']] = $desc['typeHints'];
            }
        }
        if (array_key_exists('__construct', $hintsMap)) {
            // hints of constructor has not return type
            array_pop($hintsMap['__construct']);
        } else {
            $hintsMap['__construct'] = [];
        }
        return $hintsMap;
    }

    private static function parseMethodDesc($filePath, $className): \Generator
    {
        $fp = fopen($filePath, "r") or die("unable to open file $filePath");
        while ($line = fgets($fp)) {
            if (preg_match("/^class $className/", $line)) {
                break;
            }
        }
        while ($line = fgets($fp)) {
            if (preg_match('/^\s+\/\*\*/', $line)) {
                $buf = [];
                while (!preg_match('/^\s*\*\//', $line = fgets($fp))) {
                    $buf[] = $line;
                }
                $typeHints = self::parseTypeHints($buf);
                $methodDef = fgets($fp);
                yield array(
                    'typeHints' => $typeHints,
                    'declare' => $methodDef
                );
            } elseif (preg_match('/^[}]/', $line)) {
                break;
            }
        }
        fclose($fp);
    }

    static function isEmptyReturnType($type): bool
    {
        $type = strtolower($type);
        return $type == 'void' || $type == 'null';
    }
}
