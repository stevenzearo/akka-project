package demo

object Demo2 extends App {
    println("a")
    hello()
    new Demo2()

    def hello(): Unit = println("hello")
}

class Demo2(name: String) {
    def this() = {
        this("steve")
    }

    println(this.name)
}