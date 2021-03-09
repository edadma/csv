package xyz.hyperreal.csv

import xyz.hyperreal.cross_platform.writeFile

object CSVWrite {

  def toFile(file: String, data: List[List[String]], delimiter: Char = ',', cr: Boolean = false): Unit =
    writeFile(file, toString(data, delimiter, cr))

  def toString(data: List[List[String]], delimiter: Char = ',', cr: Boolean = false): String = {
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
