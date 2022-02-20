<?php
namespace Soda\Unittest;

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

//        $resConv = ObjectConverter
//        $serialResult = $resConv->toJsonSerializer($result);
        $serialResult = $result;
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
//                $expect = $resConv->fromJsonSerializable($input['expected']);
                $expect = $input['expected'];
                if ($this->validator) {
                    $success = ($this->validator)($expect, $result);
                } else {
//                    $success = ObjectFeature::create($retType)->isEqual($expect, $result);
                    $success = $expect == $result;
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

    static function create($instance, $methodName, $typeHints): TestWork
    {
        return new TestWork([$instance, $methodName], $typeHints);
    }
}
