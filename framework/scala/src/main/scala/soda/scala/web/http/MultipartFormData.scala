package soda.scala.web.http

import soda.scala.web.exception.ParameterMissingException

class MultipartFormData(private val partData: Map[String, List[Part]]) {

  def firstValueOpt(key: String): Option[String] = {
    elementOpt(key, _.bodyString)
  }

  def firstValue(key: String): String = {
    firstElement(key, _.bodyString)
  }

  def values(key: String): List[String] = {
    elements(key, _.bodyString)
  }

  def firstFileOpt(key: String): Option[Array[Byte]] = {
    elementOpt(key, _.payload)
  }

  def firstFile(key: String): Array[Byte] = {
    firstElement(key, _.payload)
  }

  def files(key: String): List[Array[Byte]] = {
    elements(key, _.payload)
  }

  private def elements[T](key: String, mapper: Part => T): List[T] = {
    partData.getOrElse(key, List.empty).map(mapper)
  }

  private def elementOpt[T](key: String, mapper: Part => T): Option[T] = {
    elements(key, mapper).headOption
  }

  private def firstElement[T](key: String, mapper: Part => T): T = {
    elementOpt(key, mapper) match {
      case Some(v) => v
      case None => throw new ParameterMissingException(key)
    }
  }

}
