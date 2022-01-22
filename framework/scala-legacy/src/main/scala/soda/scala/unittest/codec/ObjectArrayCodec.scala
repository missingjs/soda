package soda.scala.unittest.codec

import soda.unittest.DataUtils

class ObjectArrayCodec(elementType: Class[_], dimensions: Int) extends ICodec[Object, Object] {

    override def encode(obj: Object): Object = {
        DataUtils.toList(obj)
    }

    override def decode(ser: Object): Object = {
        DataUtils.toArray(ser, elementType, dimensions)
    }

}
