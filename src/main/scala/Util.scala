package jp.co.guru.MyMine

object Util {
  type POS = (Int, Int)

  implicit class MyTupple2(tp: POS) {
    def +(other: POS) = (tp._1 + other._1, tp._2 + other._2)
  }

  implicit class MyArray[A](ar: Array[Array[A]]) {
    def apply(p: POS): A = ar(p._1)(p._2)
    def update(p: POS, v: A): Unit = ar(p._1)(p._2) = v
  }
  def LRUD = List((-1, 0), (1, 0), (0, -1), (0, 1))

  trait MyRange {
    val width: Int
    val height: Int

    lazy val xrange = (0 to width - 1)
    lazy val yrange = (0 to height - 1)

    val isValidPos: ((POS) => Boolean) = p => xrange.contains(p._1) && yrange.contains(p._2)
  }
}

