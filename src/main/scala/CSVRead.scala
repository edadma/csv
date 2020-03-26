package xyz.hyperreal.csv

import scala.util.Try
import java.nio.file.{Files, Path, Paths}

import scala.collection.mutable.ListBuffer

object CSVRead {

  def fromFile(file: String, delimiter: Char = ','): Try[List[List[String]]] = fromPath(Paths.get(file))

  def fromPath(path: Path, delimiter: Char = ','): Try[List[List[String]]] = {
    require(Files.exists(path), s"doesn't exist: $path")
    require(Files.isRegularFile(path), s"not a regular file: $path")
    require(Files.isReadable(path), s"not readable: $path")

    fromReader(CharReader.fromInputStream(Files.newInputStream(path)), delimiter)
  }

  def fromString(s: String, delimiter: Char = ',') = fromReader(new StringCharReader(s), delimiter)

  def fromReader(in: CharReader, delimiter: Char = ',') = {
    var input   = in
    val records = new ListBuffer[List[String]]

    def readInput: List[List[String]] = {
      def consume(except: Char*): String = {
        val buf = new StringBuilder

        def consume: String =
          if (input.eoi || (except contains input.ch))
            buf.toString
          else {
            buf += input.ch
            advance
            consume
          }

        consume
      }

      def field =
        if (input.more) {
          if (input.ch == '"') {
            advance

            val chunks = new StringBuilder

            def chunk: Unit = {
              chunks ++= consume('"')

              if (input.next.more && input.next.ch == '"') {
                input = input.next.next
                chunks += '"'
                chunk
              }
            }

            chunk
            chr('"')
            chunks.toString
          } else
            consume(delimiter, '\r', '\n', '"')
        } else
          ""

      def record = {
        val l = new ListBuffer[String]

        def fields: List[String] = {
          l += field

          if (input.more && input.ch == delimiter) {
            advance
            fields
          } else
            l.toList
        }

        records += fields

        opt('\r')

        if (input.more)
          chr('\n')
      }

      def advance = input = input.next

      def chr(c: Char) = {
        val s =
          c match {
            case '\r' => "\\r"
            case '\n' => "\\n"
            case _    => s"$c"
          }

        if (input.eoi)
          input.error(s"expected '$s', but end of input")
        else if (input.ch == c)
          advance
        else
          input.error(s"expected '$s', but encountered '${input.ch}'")
      }

      def opt(c: Char) =
        if (input.more) {
          if (input.ch == c)
            chr(c)
        }

      if (input.more) {
        record
        readInput
      } else if (records isEmpty)
        List(List("")) // according to spec a csv file has at least one record
      else
        records.toList
    }

    //    def field: String
    //      =
    //      {
    //
    //
    //
    //      }

    Try(readInput)
  }
}
