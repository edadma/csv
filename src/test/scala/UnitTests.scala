package xyz.hyperreal.csv

import utest._

import scala.util.Success

object UnitTests extends TestSuite {

  val tests = Tests {
    test("tab as delimiter") {
      assert(CSVReader.fromString("a\tb", '\t') == Success(List(List("a", "b"))))
      assert(CSVReader.fromString("\tb", '\t') == Success(List(List("", "b"))))
      assert(CSVReader.fromString("a\t", '\t') == Success(List(List("a", ""))))
      assert(CSVReader.fromString("\t", '\t') == Success(List(List("", ""))))
    }
    test("empty file") {
      assert(CSVReader.fromString("") == Success(List(List(""))))
      assert(CSVReader.fromString("\n") == Success(List(List(""))))
      assert(CSVReader.fromString("\r\n") == Success(List(List(""))))
    }
    test("basic") {
      assert(
        CSVReader.fromString(
          """
          |11,12
          |21,22
          """.trim.stripMargin
        ) == Success(List(List("11", "12"), List("21", "22"))))
      assert(
        CSVReader.fromString(
          """
            |11
            |21
          """.trim.stripMargin
        ) == Success(List(List("11"), List("21"))))
      assert(
        CSVReader.fromString(
          """
            |11,12
          """.trim.stripMargin
        ) == Success(List(List("11", "12"))))
      assert(
        CSVReader.fromString(
          """
            |11
          """.trim.stripMargin
        ) == Success(List(List("11"))))
    }
    test("quoted fields") {
      assert(
        CSVReader.fromString(
          """
            |11,"12"
            |"2""1","22"
          """.trim.stripMargin
        ) == Success(List(List("11", "12"), List("2\"1", "22"))))
    }
    test("empty fields") {
      assert(
        CSVReader.fromString(
          """
            |,12
            |21,22
          """.trim.stripMargin
        ) == Success(List(List("", "12"), List("21", "22"))))
      assert(
        CSVReader.fromString(
          """
            |11,
            |21,22
          """.trim.stripMargin
        ) == Success(List(List("11", ""), List("21", "22"))))
      assert(
        CSVReader.fromString(
          """
            |11,12
            |,22
          """.trim.stripMargin
        ) == Success(List(List("11", "12"), List("", "22"))))
      assert(
        CSVReader.fromString(
          """
            |11,12
            |21,
          """.trim.stripMargin
        ) == Success(List(List("11", "12"), List("21", ""))))
      assert(
        CSVReader.fromString(
          """
            |
            |21
          """.trim.stripMargin
        ) == Success(List(List(""), List("21"))))
      assert(
        CSVReader.fromString(
          """
            |11
            |
            |
          """.trim.stripMargin
        ) == Success(List(List("11"), List(""))))
    }
  }

}
