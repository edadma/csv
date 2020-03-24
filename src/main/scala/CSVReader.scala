package xyz.hyperreal

package object csv {

  import scala.util.Try
  import java.nio.file.{Files, Paths}

  import scala.collection.mutable.ListBuffer

  def read(file: String, delimiter: Char = ','): Try[List[List[String]]] = {

    val path = Paths.get(file)

    require(Files.exists(path), s"doesn't exist: $file")
    require(Files.isRegularFile(path), s"not a regular file: $file")
    require(Files.isReadable(path), s"not readable: $file")

    val records = new ListBuffer[List[String]]
    var input   = CharReader.fromInputStream(Files.newInputStream(path))

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

      def field = {
        consume(',', '\n')
      }

      def record = {
        records += List(field)

        opt('\r')

        if (!input.eoi)
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
          sys.error(s"expected '$s', but end of input")
        else if (input.ch == c)
          advance
        else
          sys.error(s"expected '$s', but encountered '${input.ch}'")
      }

      def opt(c: Char) =
        if (!input.eoi) {
          if (input.ch == c)
            chr(c)
        }

      if (!input.eoi) {
        record
        readInput
      } else
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
