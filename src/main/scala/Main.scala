package xyz.hyperreal.csv

import scala.util.{Success, Try}

object Main extends App {

//  val text =
//    """
//      |11,12
//      |21,22
//    """.trim.stripMargin
//
//  println(readFromString(text))
  println(readFromFile("test.csv"))
//  println(readFromString(""""a",b"""))

}
