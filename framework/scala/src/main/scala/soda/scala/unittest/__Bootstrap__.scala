package soda.scala.unittest

import scala.reflect.runtime.universe.typeOf

class Solution {}
// object Solution { ... }

class __Bootstrap__ {
  def get(): TestWork = {
    new TestWork(typeOf[Solution], "METHOD")
  }
}

object __Bootstrap__ extends App {
  new __Bootstrap__().get().run()
}
