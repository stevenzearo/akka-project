package frp

import scala.language.implicitConversions

object SignalDemo extends App {
    val x = Signal(1)
    val j = Signal(x() + 1)
    x() = 2
    println(j())

    var x2 = Signal(1)
    val j2 = Signal(x2() + 1)
    x2 = Signal(2)
    println(j2)

}

