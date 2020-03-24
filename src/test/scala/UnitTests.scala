package xyz.hyperreal.csv

import utest._

import scala.util.Success

object UnitTests extends TestSuite {

  val tests = Tests {
    test("basic") {
      assert(
        readFromString(
          """
          |11,12
          |21,22
          """.trim.stripMargin
        ) == Success(List(List("11", "12"), List("21", "22"))))
    }
  }

}
