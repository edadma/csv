package xyz.hyperreal.csv

import scala.collection.AbstractIterator
import scala.util.{Failure, Success, Try}

import java.nio.file.{Files, Paths}

class CSVReader(file: String, delimiter: Char = ',') extends AbstractIterator[Try[List[String]]] {

  private val path                      = Paths.get(file)
  private var record: Try[List[String]] = _

  require(Files.exists(path), s"doesn't exist: $file")
  require(Files.isRegularFile(path), s"not a regular file: $file")
  require(Files.isReadable(path), s"not readable: $file")

  private val input = CharReader.fromInputStream(Files.newInputStream(path))

  readRecord

  private def readRecord = {
    def field = {}
  }

  def hasNext = record ne null

  def next =
    if (hasNext) {
      val res = record

      readRecord
      res
    } else
      throw new NoSuchElementException(s"no more records from $file")

}
