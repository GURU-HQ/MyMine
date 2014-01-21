package jp.co.guru.MyMine

import com.typesafe.scalalogging.slf4j.Logging
import org.scalatest.FunSuite
import Util._

trait TestModel extends Model {
  def getX() = 10
  def getY() = 10
  def getValue(x: Int, y: Int): Status = {
    Status(x * y % 10)
  }
  
  override def open(p: POS): Boolean = false
  override def flag(p: POS): Boolean = false

}

class SwingSuite extends FunSuite {
  ignore("Frame") {
    val f = new F(new Model with TestModel)
    f.visible = true
  }
  
  ignore("Swing Board") {
    val b = new Board(20, 20) with BoardUI with Manip
    b.randBomb(50)
    
    val s = new Splite(b) with DispSplite
    val f = new F(s)
    info("\n" + b.display)
    info("\n" + s.display)
    
    f.visible = true
  }
  
  test("Good Board") {
    val b = new Board(20, 20) with BoardUI with Manip
    b.randBomb(50)
    
    val s = new Splite(b) with DispSplite
    val f = new F(s, BetterRenderer)
    f.visible = true
  }
}
