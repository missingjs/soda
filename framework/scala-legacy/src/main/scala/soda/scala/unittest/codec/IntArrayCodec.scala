package soda.scala.unittest.codec

import soda.unittest.DataUtils

class IntArrayCodec extends ICodec[java.util.List[Integer], Array[Int]] {

    override def encode(obj: Array[Int]): java.util.List[Integer] = {
        DataUtils.toList(obj)
    }

    override def decode(ser: java.util.List[Integer]): Array[Int] = {
        DataUtils.toIntArray(ser)
    }

}

