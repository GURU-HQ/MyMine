package jp.co.guru.MyMine

/**
 * 盤面の状態を示す
 */
class Status(val n: Int) extends AnyVal {
  override def toString(): String = {
    n.toString + " "
  }
}

object Status {
  def apply(n: Int): Status = new Status(n)

  val EMPTY = Status(0)
  val BOMB = Status(9)
  val UNKNOWN = Status(-1)
  val FLAG = Status(100)
}
