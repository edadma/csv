package xyz.hyperreal.csv

import utest._

import scala.util.Success

object CSVWriteTests extends TestSuite {

  val tests = Tests {
    test("empty") {
      assert(CSVWrite.toString(List(List(""))) == "\n")
    }
    test("basic") {
      assert(CSVWrite.toString(List(List("a"))) == "a\n")
      assert(CSVWrite.toString(List(List("a", "b"))) == "a,b\n")
      assert(CSVWrite.toString(List(List("a1"), List("a2"))) == "a1\na2\n")
      assert(CSVWrite.toString(List(List("a1", "b1"), List("a2", "b2"))) == "a1,b1\na2,b2\n")
    }
    test("empty fields") {
      assert(CSVWrite.toString(List(List("", "b"))) == ",b\n")
      assert(CSVWrite.toString(List(List("a", ""))) == "a,\n")
      assert(CSVWrite.toString(List(List(""), List("a2"))) == "\na2\n")
      assert(CSVWrite.toString(List(List("a1"), List(""))) == "a1\n\n")
      assert(CSVWrite.toString(List(List("", "b1"), List("a2", ""))) == ",b1\na2,\n")
    }
    test("quoted fields") {
      assert(CSVWrite.toString(List(List("\"a"))) == "\"\"\"a\"\n")
      assert(CSVWrite.toString(List(List("a\"b"))) == "\"a\"\"b\"\n")
      assert(CSVWrite.toString(List(List("a\""))) == "\"a\"\"\"\n")
      assert(CSVWrite.toString(List(List("a\"", "b"))) == "\"a\"\"\",b\n")
      assert(CSVWrite.toString(List(List("a", "b\""))) == "a,\"b\"\"\"\n")
      assert(CSVWrite.toString(List(List("a\"1"), List("a\"2"))) == "\"a\"\"1\"\n\"a\"\"2\"\n")
    }
  }

}
