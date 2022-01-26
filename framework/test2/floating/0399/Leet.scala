import soda.scala.unittest._

import scala.reflect.runtime.universe.typeOf

import collection.mutable
import util.control.Breaks.{break, breakable}

object Solution {
  def calcEquation(equations: List[List[String]], values: Array[Double], queries: List[List[String]]): Array[Double] = {
    val indexMap = getIndexMap(equations)
    val N = indexMap.size
    val table = Array.fill(N){Array.fill(N)(-1.0)}

    for (k <- values.indices) {
      val p = equations(k)
      val i = indexMap(p(0))
      val j = indexMap(p(1))
      table(i)(j) = values(k)
      table(j)(i) = 1.0 / values(k)
    }

    val res = Array.fill(queries.size)(0.0)
    val visited = Array.fill(N)(false)

    for (i <- res.indices) {
      val a = queries(i)(0)
      val b = queries(i)(1)
      val ai = indexMap.get(a)
      val bi = indexMap.get(b)
      if (ai.isEmpty || bi.isEmpty) {
        res(i) = -1.0
      } else if (ai.get == bi.get) {
        res(i) = 1.0
      } else {
        for (j <- visited.indices) {
          visited(j) = false
        }
        res(i) = dfs(ai.get, bi.get, table, visited)
      }
    }

    res
  }

  private def getIndexMap(eqs: List[List[String]]): Map[String, Int] = {
    val imap = mutable.Map[String, Int]()
    for (e <- eqs) {
      val a = e(0)
      val b = e(1)
      imap.getOrElseUpdate(a, imap.size)
      imap.getOrElseUpdate(b, imap.size)
    }
    imap.toMap
  }

  private def dfs(ai: Int, bi: Int, table: Array[Array[Double]], visited: Array[Boolean]): Double = {
    if (table(ai)(bi) >= 0.0) {
      return table(ai)(bi)
    }

    visited(ai) = true
    var res = -1.0
    breakable {
      for (adj <- table.indices) {
        if (table(ai)(adj) >= 0.0 && !visited(adj)) {
          val v = dfs(adj, bi, table, visited)
          if (v >= 0.0) {
            res = table(ai)(adj) * v
            break()
          }
        }
      }
    }
    table(ai)(bi) = res
    table(bi)(ai) = 1.0 / res
    res
  }
}

class Solution {}

class Leet {
  def get(): TestWork = {
    val work = new TestWork(typeOf[Solution], "calcEquation")
    // val work = TestWork.forStruct(...)
    // work.setValidator((R, R) => Boolean)
    // work.compareSerial = true
    work
  }
}

object Leet extends App {
  new Leet().get().run()
}
