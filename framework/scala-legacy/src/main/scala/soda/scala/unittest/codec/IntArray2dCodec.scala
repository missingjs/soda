package soda.scala.unittest.codec

import java.util.List

import soda.unittest.DataUtils

class IntArray2dCodec extends ICodec[List[List[Integer]], Array[Array[Int]]] {

    override def encode(obj: Array[Array[Int]]): List[List[Integer]] = {
        DataUtils.toList2d(obj)
    }

    override def decode(ser: List[List[Integer]]): Array[Array[Int]] = {
        DataUtils.toIntArray2d(ser)
    }

}

