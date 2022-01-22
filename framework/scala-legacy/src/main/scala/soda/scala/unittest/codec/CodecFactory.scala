package soda.scala.unittest.codec

import collection.mutable.{Map => MMap}

object CodecFactory {

    val codecMap = MMap[Class[_], Class[_ <: ICodec[_,_]]](
            classOf[Array[Int]] -> classOf[IntArrayCodec],
            classOf[Array[Array[Int]]] -> classOf[IntArray2dCodec]
    )

    def create(objClass: Class[_]): ICodec[_,_] = {
        val cls = codecMap.get(objClass)
        cls match {
            case Some(s) => s.getDeclaredConstructor().newInstance()
            case None => {
                if (objClass.isArray())
                    new ObjectArrayCodec(getElementType(objClass), getDimension(objClass))
                else
                    new DefaultCodec()
            }
        }
    }

    def getElementType(cls: Class[_]): Class[_] = {
        if (cls.isArray()) getElementType(cls.getComponentType()) else cls
    }

    def getDimension(cls: Class[_]): Int = {
        if (cls.isArray()) 1 + getDimension(cls.getComponentType()) else 0
    }

}
