package xyz.hyperreal.csv

import java.io.Writer
import java.nio.charset.Charset
import java.nio.file.{Files, Path}

object CSVWrite {

  def toWriter(out: Writer, data: List[List[String]], delimiter: Char = ',') =
    out write toString(data, delimiter)

  def toPath(path: Path, cs: Charset, data: List[List[String]], delimiter: Char = ',') =
    toWriter(Files.newBufferedWriter(path, cs), data)

  def toString(data: List[List[String]], delimiter: Char = ',') = {
    val buf = new StringBuilder
    val q   = "\""

    for (r <- data) {
      val r1 =
        r map { f =>
          if ((r contains ',') || (r contains '\r') || (r contains '\n') || (r contains '"'))
            s"$q${f.replace("\"", "\"\"")}$q"
          else
            f
        }

      buf ++= r1 mkString delimiter.toString
      buf ++= "\r\n"
    }

    buf.toString
  }
}
