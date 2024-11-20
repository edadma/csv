package io.github.edadma.csv

import scala.util.Try
import scala.collection.mutable.ListBuffer
import io.github.edadma.char_reader._
import io.github.edadma.cross_platform.readableFile

import scala.annotation.tailrec

object CSVRead {

  def fromFile(file: String, delimiter: Char = ','): Try[Seq[Seq[String]]] = {
    require(readableFile(file), s"not readable: $file")

    fromReader(CharReader.fromFile(file), delimiter)
  }

  def fromString(s: String, delimiter: Char = ','): Try[Seq[Seq[String]]] =
    fromReader(CharReader.fromString(s), delimiter)

  def fromReader(in: CharReader, delimiter: Char = ','): Try[Seq[Seq[String]]] = {
    val records = new ListBuffer[Seq[String]]

    Try {
      fromReaderStreamed(in, records += _, delimiter)

      if records.isEmpty then Seq(Seq(""))
      else records.toSeq
    }
  }

  def fromFileStreamed(file: String, output: Seq[String] => Unit, delimiter: Char = ','): Unit = {
    require(readableFile(file), s"not readable: $file")

    fromReaderStreamed(CharReader.fromFile(file), output, delimiter)
  }

  def fromStringStreamed(s: String, output: Seq[String] => Unit, delimiter: Char = ','): Unit =
    fromReaderStreamed(CharReader.fromString(s), output, delimiter)

  def fromReaderStreamed(in: CharReader, output: Seq[String] => Unit, delimiter: Char = ','): Unit =
    var input = in

    @tailrec
    def readInput(): Unit = {
      def consume(except: Char*): String = {
        val buf = new StringBuilder

        @tailrec
        def consume: String =
          if (input.eoi || (except contains input.ch))
            buf.toString
          else {
            buf += input.ch
            advance()
            consume
          }

        consume
      }

      def field =
        if (input.more) {
          if (input.ch == '"') {
            advance()

            val chunks = new StringBuilder

            @tailrec
            def chunk(): Unit = {
              chunks ++= consume('"')

              if (input.next.more && input.next.ch == '"') {
                input = input.next.next
                chunks += '"'
                chunk()
              }
            }

            chunk()
            chr('"')
            chunks.toString
          } else
            consume(delimiter, '\r', '\n', '"')
        } else
          ""

      def record(): Unit = {
        val l = new ListBuffer[String]

        @tailrec
        def fields: Seq[String] = {
          l += field

          if (input.more && input.ch == delimiter) {
            advance()
            fields
          } else
            l.toSeq
        }

        output(fields)

        opt('\r')

        if (input.more)
          chr('\n')
      }

      def advance(): Unit = input = input.next

      def chr(c: Char): Unit = {
        val s =
          c match {
            case '\r' => "\\r"
            case '\n' => "\\n"
            case _    => s"$c"
          }

        if (input.eoi)
          input.error(s"expected '$s', but end of input")
        else if (input.ch == c)
          advance()
        else
          input.error(s"expected '$s', but encountered '${input.ch}'")
      }

      def opt(c: Char): Unit =
        if (input.more) {
          if (input.ch == c)
            chr(c)
        }

      if (input.more) {
        record()
        readInput()
      }
    }

    readInput()
  end fromReaderStreamed

//  def fromReader(in: CharReader, delimiter: Char = ','): Try[List[List[String]]] = {
//    var input   = in
//    val records = new ListBuffer[List[String]]
//
//    @tailrec
//    def readInput: List[List[String]] = {
//      def consume(except: Char*): String = {
//        val buf = new StringBuilder
//
//        @tailrec
//        def consume: String =
//          if (input.eoi || (except contains input.ch))
//            buf.toString
//          else {
//            buf += input.ch
//            advance()
//            consume
//          }
//
//        consume
//      }
//
//      def field =
//        if (input.more) {
//          if (input.ch == '"') {
//            advance()
//
//            val chunks = new StringBuilder
//
//            @tailrec
//            def chunk(): Unit = {
//              chunks ++= consume('"')
//
//              if (input.next.more && input.next.ch == '"') {
//                input = input.next.next
//                chunks += '"'
//                chunk()
//              }
//            }
//
//            chunk()
//            chr('"')
//            chunks.toString
//          } else
//            consume(delimiter, '\r', '\n', '"')
//        } else
//          ""
//
//      def record(): Unit = {
//        val l = new ListBuffer[String]
//
//        @tailrec
//        def fields: List[String] = {
//          l += field
//
//          if (input.more && input.ch == delimiter) {
//            advance()
//            fields
//          } else
//            l.toList
//        }
//
//        records += fields
//
//        opt('\r')
//
//        if (input.more)
//          chr('\n')
//      }
//
//      def advance(): Unit = input = input.next
//
//      def chr(c: Char): Unit = {
//        val s =
//          c match {
//            case '\r' => "\\r"
//            case '\n' => "\\n"
//            case _    => s"$c"
//          }
//
//        if (input.eoi)
//          input.error(s"expected '$s', but end of input")
//        else if (input.ch == c)
//          advance()
//        else
//          input.error(s"expected '$s', but encountered '${input.ch}'")
//      }
//
//      def opt(c: Char): Unit =
//        if (input.more) {
//          if (input.ch == c)
//            chr(c)
//        }
//
//      if (input.more) {
//        record()
//        readInput
//      } else if (records.isEmpty)
//        List(List("")) // according to spec a csv file has at least one record
//      else
//        records.toList
//    }
//
//    Try(readInput)
//  }
}
