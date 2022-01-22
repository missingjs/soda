package soda.scala.unittest.codec

trait ICodec[Serial, Obj] {

    def encode(obj: Obj): Serial

    def decode(serial: Serial): Obj

}

