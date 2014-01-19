package jp.co.guru.MyMine

trait DispSplite extends Splite {
  def display() = {
    (0 to bord.height - 1).map(y =>
      mask(y).collect({
        case Status.UNKNOWN      => '?'
        case x if x == Status(0) => ' '
        case x                   => x.toString.charAt(0)
      }).mkString(" ")).mkString("\n")
  }
}

trait BordUI extends Bord {
  def display() = bordAll(bord(_)(_)).map(_.mkString).mkString("\n")
}

