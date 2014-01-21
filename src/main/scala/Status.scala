package jp.co.guru.MyMine

/**
 * 盤面の状態を示す
 */
case class Status(val n: Int) {
  override def toString(): String = {
    n.toString + " "
  }
  
  def is(other: Int): Boolean = {
    n == other
  }
}

object Status {
//  def apply(n: Int): Status = new Status(n)
  
  import scala.language.implicitConversions  
  implicit def int2Status(i: Int) = Status(i)

  val EMPTY = Status(0)
  val BOMB = Status(9)
  val UNKNOWN = Status(-1)
  val FLAG = Status(100)

  
}
