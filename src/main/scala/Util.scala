package jp.co.guru.MyMine

object Util {
  
  /**
   * 位置をTuple2で表現する
   */
  type POS = (Int, Int)

  /**
   * POS同士の加算 (POS + OFFSETに書き直す必要がある)
   */
  implicit class MyTupple2(tp: POS) {
    def +(other: POS) = (tp._1 + other._1, tp._2 + other._2)
  }

  /**
   * POSのSeqのユーティリティー
   */
  implicit class MySeq[POS](list: Seq[POS]) {
    /**
     * POSに対して 与えた関数が 真となるますの数を返す
     */
    def count(f: (POS) => Boolean): Int = list.foldLeft(0)((x, y) => x + (if (f(y)) 1 else 0))
  }

  implicit class MyArray[A](ar: Array[Array[A]]) {
  /**
   *   2次元のArrayをPOSでアクセス出来るようにしておく
   */  
    def apply(p: POS): A = ar(p._1)(p._2)
    def update(p: POS, v: A): Unit = ar(p._1)(p._2) = v
  }
  
  def allCells(p: POS) = for(ox <- -1 to 1; oy <- -1 to 1) yield p + (ox, oy)
  def around(p: POS) = allCells(p).filter(_ != p)  

  trait MyRange {
    val width: Int
    val height: Int

    lazy val xrange = (0 to width - 1)
    lazy val yrange = (0 to height - 1)

    val isValidPos: ((POS) => Boolean) = p => xrange.contains(p._1) && yrange.contains(p._2)
  }


}

