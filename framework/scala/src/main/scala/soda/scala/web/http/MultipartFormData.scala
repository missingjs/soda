package soda.scala.web.http

import soda.scala.web.exception.ParameterMissingException

class MultipartFormData(private val partData: Map[String, List[Part]]) {

  def firstValueOpt(key: String): Option[String] = {
    partData.getOrElse(key, List.empty).headOption.map(_.bodyString)
  }

  def firstValue(key: String): String = {
    firstValueOpt(key) match {
      case Some(v) => v
      case None => throw new ParameterMissingException(key)
    }
  }

  def values(key: String): List[String] = {
    partData.getOrElse(key, List.empty).map(_.bodyString)
  }

  def firstFileOpt(key: String): Option[Array[Byte]] = {
    partData.getOrElse(key, List.empty).headOption.map(_.payload)
  }

  def firstFile(key: String): Array[Byte] = {
    firstFileOpt(key) match {
      case Some(v) => v
      case None => throw new ParameterMissingException(key)
    }
  }

  def files(key: String): List[Array[Byte]] = {
    partData.getOrElse(key, List.empty).map(_.payload)
  }

}
