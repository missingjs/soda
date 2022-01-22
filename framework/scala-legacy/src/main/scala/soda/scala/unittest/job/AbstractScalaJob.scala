package soda.scala.unittest.job

import soda.unittest.CommonJob

abstract class AbstractScalaJob extends CommonJob {
    
    def createSpec(): JobSpec

    override def execute(input: String): String = {
        new JobExecutor().exec(input, createSpec())
    }

}
