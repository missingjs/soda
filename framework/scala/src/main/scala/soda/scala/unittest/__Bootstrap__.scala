package soda.scala.unittest

class __Bootstrap__ {
  def get(): TestWork = {
    new TestWork()
  }
}

object __Bootstrap__ extends App {
  new __Bootstrap__().get().run()
}
