package soda.scala.unittest.codec

class DefaultCodec extends ICodec[Object, Object] {

    override def encode(obj: Object): Object = {
        obj
    }

    override def decode(ser: Object): Object = {
        ser
    }

}
