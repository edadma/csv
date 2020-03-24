package xyz.hyperreal.csv

object Main extends App {

  val text =
    """
      |11,12
      |21,22
    """.trim.stripMargin

//  println(readFromFile("test.csv"))
  println(readFromString(text))

}
