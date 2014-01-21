package jp.co.guru.MyMine

import Util._

trait DispSplite extends Splite {
  def display() = {
    (0 to board.height - 1).map(y =>
      mask(y).collect({
        case Status.UNKNOWN      => '?'
        case x if x == Status(0) => ' '
        case x                   => x.toString.charAt(0)
      }).mkString(" ")).mkString("\n")
  }
}

trait BoardUI extends Board {
  def display() = boardAll(board(_)(_)).map(_.mkString).mkString("\n")
}

