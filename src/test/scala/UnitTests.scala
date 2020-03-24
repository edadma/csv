package xyz.hyperreal.csv

import utest._

import scala.util.Success

object UnitTests extends TestSuite {

  val tests = Tests {
    test("tab as delimiter") {
      assert(readFromString("a\tb", '\t') == Success(List(List("a", "b"))))
      assert(readFromString("\tb", '\t') == Success(List(List("", "b"))))
      assert(readFromString("a\t", '\t') == Success(List(List("a", ""))))
      assert(readFromString("\t", '\t') == Success(List(List("", ""))))
    }
    test("empty file") {
      assert(readFromString("") == Success(List(List(""))))
      assert(readFromString("\n") == Success(List(List(""))))
      assert(readFromString("\r\n") == Success(List(List(""))))
    }
    test("basic") {
      assert(
        readFromString(
          """
          |11,12
          |21,22
          """.trim.stripMargin
        ) == Success(List(List("11", "12"), List("21", "22"))))
      assert(
        readFromString(
          """
            |11
            |21
          """.trim.stripMargin
        ) == Success(List(List("11"), List("21"))))
      assert(
        readFromString(
          """
            |11,12
          """.trim.stripMargin
        ) == Success(List(List("11", "12"))))
      assert(
        readFromString(
          """
            |11
          """.trim.stripMargin
        ) == Success(List(List("11"))))
    }
    test("empty fields") {
      assert(
        readFromString(
          """
            |,12
            |21,22
          """.trim.stripMargin
        ) == Success(List(List("", "12"), List("21", "22"))))
      assert(
        readFromString(
          """
            |11,
            |21,22
          """.trim.stripMargin
        ) == Success(List(List("11", ""), List("21", "22"))))
      assert(
        readFromString(
          """
            |11,12
            |,22
          """.trim.stripMargin
        ) == Success(List(List("11", "12"), List("", "22"))))
      assert(
        readFromString(
          """
            |11,12
            |21,
          """.trim.stripMargin
        ) == Success(List(List("11", "12"), List("21", ""))))
      assert(
        readFromString(
          """
            |
            |21
          """.trim.stripMargin
        ) == Success(List(List(""), List("21"))))
      assert(
        readFromString(
          """
            |11
            |
            |
          """.trim.stripMargin
        ) == Success(List(List("11"), List(""))))
    }
  }

}
