package soda.scala.unittest

import java.util._
import java.util.stream._

import soda.leetcode._

import soda.unittest._
import soda.unittest.LoggerHelper.logger

// setp [1]: implement object Solution
// object Solution

// step [2]: implement test job
class __Bootstrap0__ extends JobTemplate[Any, Any] {

    override def execute(req: TestRequest, resp: TestResponse): Any = {
        // TODO
        throw new RuntimeException("Not implemented");
    }

    override def serialize(res: Any): Any = {
        res
    }
    
    override def validate(req: TestRequest, resp: TestResponse): Boolean = {
        req.getExpected(classOf[Object]).equals(resp.getResult(classOf[Object]))
    }

}

object __Bootstrap0__ extends App {
    new ConsoleRunner().run(new __Bootstrap0__())
}
