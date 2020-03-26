package xyz.hyperreal.csv

import utest._

import scala.util.Success

object CSVReadTests extends TestSuite {

  val tests = Tests {
    test("tab as delimiter") {
      assert(CSVRead.fromString("a\tb", '\t') == Success(List(List("a", "b"))))
      assert(CSVRead.fromString("\tb", '\t') == Success(List(List("", "b"))))
      assert(CSVRead.fromString("a\t", '\t') == Success(List(List("a", ""))))
      assert(CSVRead.fromString("\t", '\t') == Success(List(List("", ""))))
    }
    test("empty file") {
      assert(CSVRead.fromString("") == Success(List(List(""))))
      assert(CSVRead.fromString("\n") == Success(List(List(""))))
      assert(CSVRead.fromString("\r\n") == Success(List(List(""))))
    }
    test("basic") {
      assert(
        CSVRead.fromString(
          """
          |11,12
          |21,22
          """.trim.stripMargin
        ) == Success(List(List("11", "12"), List("21", "22"))))
      assert(
        CSVRead.fromString(
          """
            |11
            |21
          """.trim.stripMargin
        ) == Success(List(List("11"), List("21"))))
      assert(
        CSVRead.fromString(
          """
            |11,12
          """.trim.stripMargin
        ) == Success(List(List("11", "12"))))
      assert(
        CSVRead.fromString(
          """
            |11
          """.trim.stripMargin
        ) == Success(List(List("11"))))
    }
    test("quoted fields") {
      assert(
        CSVRead.fromString(
          """
            |11,"12"
            |"2""1","22"
          """.trim.stripMargin
        ) == Success(List(List("11", "12"), List("2\"1", "22"))))
    }
    test("empty fields") {
      assert(
        CSVRead.fromString(
          """
            |,12
            |21,22
          """.trim.stripMargin
        ) == Success(List(List("", "12"), List("21", "22"))))
      assert(
        CSVRead.fromString(
          """
            |11,
            |21,22
          """.trim.stripMargin
        ) == Success(List(List("11", ""), List("21", "22"))))
      assert(
        CSVRead.fromString(
          """
            |11,12
            |,22
          """.trim.stripMargin
        ) == Success(List(List("11", "12"), List("", "22"))))
      assert(
        CSVRead.fromString(
          """
            |11,12
            |21,
          """.trim.stripMargin
        ) == Success(List(List("11", "12"), List("21", ""))))
      assert(
        CSVRead.fromString(
          """
            |
            |21
          """.trim.stripMargin
        ) == Success(List(List(""), List("21"))))
      assert(
        CSVRead.fromString(
          """
            |11
            |
            |
          """.trim.stripMargin
        ) == Success(List(List("11"), List(""))))
    }
  }

}
