package soda.scala.web.http

class MultipartFormData(private val partData: Map[String, List[Part]]) {

  def firstValue(key: String): Option[String] = {
    partData.getOrElse(key, List.empty).headOption.map(_.bodyString)
  }

  def values(key: String): List[String] = {
    partData.getOrElse(key, List.empty).map(_.bodyString)
  }

  def firstFile(key: String): Option[Array[Byte]] = {
    partData.getOrElse(key, List.empty).headOption.map(_.payload)
  }

  def files(key: String): List[Array[Byte]] = {
    partData.getOrElse(key, List.empty).map(_.payload)
  }

}
