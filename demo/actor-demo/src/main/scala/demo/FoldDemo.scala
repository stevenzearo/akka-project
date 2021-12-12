package demo

object FoldDemo extends App {
  val f: (Int, Int) => Int = (a, b) => {
    val res = a - b
    println(res)
    res
  }
  private val ls = List(5, 4, 3, 2, 1)
  ls.foldRight(0)(f) // 1-0=1, 2-1=1, 3-1=2, 4=2=2, 5-2=3
  println("-----")
  ls.foldLeft(0)(f) // 0-5=-5 -5-4=-9 -9-3=-12 -12-2=-14 -14-1=-15
}
