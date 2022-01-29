package soda.scala.unittest.validate

import scala.collection.mutable

class XMap[K, V](feat: ObjectFeature[K]) {

  case class XEntry(key: K, var value: V)

  private val dict = mutable.Map[Long, mutable.ArrayBuffer[XEntry]]()

  private var size: Int = 0

  private def _hash(key: K): Long = feat.hash(key)

  private def _entry(key: K): Option[XEntry] = {
    dict.getOrElse(_hash(key), mutable.ArrayBuffer.empty).find(entry => feat.isEqual(key, entry.key))
  }

  def contains(key: K): Boolean = _entry(key).isDefined

  def get(key: K): Option[V] = _entry(key).map(e => e.value)

  def getOrElse(key: K, default: V): V = {
    _entry(key) match {
      case Some(entry) => entry.value
      case None => default
    }
  }

  def put(key: K, value: V): Unit = {
    val e = _entry(key) match {
      case Some(entry) => entry
      case None => {
        val h = _hash(key)
        val values = dict.getOrElseUpdate(h, mutable.ArrayBuffer[XEntry]())
        val entry = XEntry(key, value)
        values += entry
        size += 1
        entry
      }
    }
    e.value = value
  }

  def remove(key: K): Unit = {
    val h = _hash(key)
    dict.get(h) match {
      case Some(entries) => {
        entries.find(e => feat.isEqual(key, e.key)) match {
          case Some(entry) => {
            dict(h) -= entry
            size -= 1
          }
          case None =>
        }
      }
      case None =>
    }
  }

}
