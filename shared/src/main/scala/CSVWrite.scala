package xyz.hyperreal.csv

import java.io.Writer
import java.nio.charset.Charset
import java.nio.file.{Files, Path, Paths}

object CSVWrite {

  def toFile(file: String, cs: Charset, data: List[List[String]], delimiter: Char = ',', cr: Boolean = false) =
    toPath(Paths.get(file), cs, data, delimiter, cr)

  def toWriter(out: Writer, data: List[List[String]], delimiter: Char = ',', cr: Boolean = false) =
    out write toString(data, delimiter, cr)

  def toPath(path: Path, cs: Charset, data: List[List[String]], delimiter: Char = ',', cr: Boolean = false) =
    toWriter(Files.newBufferedWriter(path, cs), data, delimiter, cr)

  def toString(data: List[List[String]], delimiter: Char = ',', cr: Boolean = false) = {
    val buf = new StringBuilder
    val q   = "\""

    for (r <- data) {
      val r1 =
        r map { f =>
          if ((f contains ',') || (f contains '\r') || (f contains '\n') || (f contains '"'))
            s"$q${f.replace("\"", "\"\"")}$q"
          else
            f
        }

      buf ++= r1 mkString delimiter.toString
      buf ++= (if (cr) "\r\n" else "\n")
    }

    buf.toString
  }
}
