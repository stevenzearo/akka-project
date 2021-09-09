package app.receiver

import scala.collection.mutable

object Demo extends App {
    val aMap: mutable.HashMap[String, Int] = mutable.HashMap.empty
    aMap.put("c", 1)
    aMap.put("d", 2)
    aMap.foreach(entry => {
        println(entry._1 + ", " + entry._2)
    })

    val bMap: Map[String, Int] = Map("a" -> 1, "b" -> 2)
    bMap.foreach(entry => {
        println(entry._1 + ", " + entry._2)
    })
}
