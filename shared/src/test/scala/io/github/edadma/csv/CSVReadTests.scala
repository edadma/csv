package io.github.edadma.csv

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

import scala.util.Success

class CSVReadTests extends AnyFreeSpec with Matchers {

  "tab as delimiter" in {
    CSVRead.fromString("a\tb", '\t') shouldBe Success(Seq(Seq("a", "b")))
    assert(CSVRead.fromString("\tb", '\t') == Success(Seq(Seq("", "b"))))
    assert(CSVRead.fromString("a\t", '\t') == Success(Seq(Seq("a", ""))))
    assert(CSVRead.fromString("\t", '\t') == Success(Seq(Seq("", ""))))
  }

  "empty file" in {
    assert(CSVRead.fromString("") == Success(Seq(Seq(""))))
    assert(CSVRead.fromString("\n") == Success(Seq(Seq(""))))
    assert(CSVRead.fromString("\r\n") == Success(Seq(Seq(""))))
  }

  "basic" in {
    assert(
      CSVRead.fromString(
        """
          |11,12
          |21,22
          """.trim.stripMargin,
      ) == Success(Seq(Seq("11", "12"), Seq("21", "22"))),
    )
    assert(
      CSVRead.fromString(
        """
            |11
            |21
          """.trim.stripMargin,
      ) == Success(Seq(Seq("11"), Seq("21"))),
    )
    assert(
      CSVRead.fromString(
        """
            |11,12
          """.trim.stripMargin,
      ) == Success(Seq(Seq("11", "12"))),
    )
    assert(
      CSVRead.fromString(
        """
            |11
          """.trim.stripMargin,
      ) == Success(Seq(Seq("11"))),
    )
  }

  "quoted fields" in {
    assert(
      CSVRead.fromString(
        """
            |11,"12"
            |"2""1","22"
          """.trim.stripMargin,
      ) == Success(Seq(Seq("11", "12"), Seq("2\"1", "22"))),
    )
  }

  "empty fields" in {
    assert(
      CSVRead.fromString(
        """
            |,12
            |21,22
          """.trim.stripMargin,
      ) == Success(Seq(Seq("", "12"), Seq("21", "22"))),
    )
    assert(
      CSVRead.fromString(
        """
            |11,
            |21,22
          """.trim.stripMargin,
      ) == Success(Seq(Seq("11", ""), Seq("21", "22"))),
    )
    assert(
      CSVRead.fromString(
        """
            |11,12
            |,22
          """.trim.stripMargin,
      ) == Success(Seq(Seq("11", "12"), Seq("", "22"))),
    )
    assert(
      CSVRead.fromString(
        """
            |11,12
            |21,
          """.trim.stripMargin,
      ) == Success(Seq(Seq("11", "12"), Seq("21", ""))),
    )
    assert(
      CSVRead.fromString(
        """
            |
            |21
          """.trim.stripMargin,
      ) == Success(Seq(Seq(""), Seq("21"))),
    )
    assert(
      CSVRead.fromString(
        """
            |11
            |
            |
          """.trim.stripMargin,
      ) == Success(Seq(Seq("11"), Seq(""))),
    )
  }

}
