<?php
namespace Soda\Unittest;

require_once __DIR__ . "/ConverterFactory.php";
require_once __DIR__ . "/FeatureFactory.php";
require_once __DIR__ . "/Utils.php";

class TestWork
{
    private mixed $proc;
    private array $argumentTypes;
    private string $returnType;
    public  bool $compareSerial = false;
    public ?object $validator = null;
    private array $arguments = [];

    function __construct($proc, $typeHints)
    {
        $this->proc = $proc;
        $this->argumentTypes = array_slice($typeHints, 0, -1);
        $this->returnType = $typeHints[count($typeHints)-1];
    }

    function run(string $text): string
    {
        $input = json_decode($text, true);
        $this->arguments = $args = Utils::parseArguments($this->argumentTypes, $input['args']);

        $startTime = Utils::microTime();
        $result = ($this->proc)(...$args);
        $endTime = Utils::microTime();
        $elapseMillis = ($endTime - $startTime) / 1e3;

        $retType = $this->returnType;
        if ($retType == 'void' || $retType == 'Void') {
            $retType = $this->argumentTypes[0];
            $result = $args[0];
        }

        $resConv = ConverterFactory::create($retType);
        $serialResult = $resConv->toJsonSerializable($result);
        $resp = array (
            'id'     => $input['id'],
            'result' => $serialResult,
            'elapse' => $elapseMillis
        );

        $success = true;
        if ($input['expected'] != null) {
            if ($this->compareSerial && $this->validator == null) {
                $a = json_encode($input['expected']);
                $b = json_encode($serialResult);
                $success = $a == $b;
            } else {
                $expect = $resConv->fromJsonSerializable($input['expected']);
                if ($this->validator) {
                    $success = ($this->validator)($expect, $result);
                } else {
                    $success = FeatureFactory::create($retType)->isEqual($expect, $result);
                }
            }
        }
        $resp['success'] = $success;

        return json_encode($resp);
    }

    function getArguments(): array
    {
        return $this->arguments;
    }

    static function create($instance, $methodName, $mainFile): TestWork
    {
        return new TestWork([$instance, $methodName], Utils::functionTypeHints($mainFile, $methodName));
    }

    static function forStruct($className, $mainFile): TestWork
    {
        $hintsMap = Utils::methodTypeHints($mainFile, $className);
        $testFunc = function ($operations, $parameters) use ($className, $hintsMap) {
            // hints of constructor has not return type
            $ctorArgs = Utils::parseArguments($hintsMap['__construct'], $parameters[0]);
            $object = new $className(...$ctorArgs);
            $res = [null];
            for ($i = 1; $i < count($parameters); ++$i) {
                $methodName = $operations[$i];
                if (!array_key_exists($methodName, $hintsMap)) {
                    throw new \Exception("hints for method $methodName not found");
                }
                $hints = $hintsMap[$methodName];
                $argTypes = array_slice($hints, 0, count($hints)-1);
                $retType = $hints[count($hints)-1];
                $args = Utils::parseArguments($argTypes, $parameters[$i]);
                $r = $object->$methodName(...$args);
                if ($retType !== 'void' && $retType != 'Void') {
                    $res[] = ConverterFactory::create($retType)->toJsonSerializable($r);
                } else {
                    $res[] = null;
                }
            }
            return $res;
        };
        return new TestWork($testFunc, ['String[]', 'mixed[]', 'mixed[]']);
    }
}
