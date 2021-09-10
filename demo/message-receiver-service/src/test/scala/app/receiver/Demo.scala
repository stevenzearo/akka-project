package app.receiver

object Demo extends App {
    private val a = new A("hello")
    println(s"a.s = ${a.s}")
}

class A {
    var s: String = _
    def this(s: String) = {
        this()
    }
}