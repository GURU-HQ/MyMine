package jp.co.guru.MyMine

import com.typesafe.scalalogging.slf4j.Logging
import org.scalatest.FunSuite

class BoardSuite extends FunSuite with Logging {
  val h = new Board(1000, 1000) with Manip
  
  test("Dummy") {
	  h.spaceList
  }
  
  test("Heavy1") {
    h.randBomb(100)
  }
  
  val b = new Board(10, 10) with BoardUI with Manip
  
  test("Test1") {
    info("\n" + b.display)
    info(b.spaceList.toString)
  }
  
  test("RandTest") {
    b.randBomb(30)
    info("\n" + b.display)
    info(b.spaceList.toString)
  }
  
  test("Splite Test") {
    b.randBomb(30)
    val s =new Splite(b) with DispSplite
    info("\n" + s.display)
  }
}

class SpliteSuite extends FunSuite {
  test("Update Test") {
    val b = new Board(10, 10) with BoardUI with Manip
    val s = new Splite(b)  with DispSplite
    (0 to 9).foreach(i => b.put(i, i))
    info("\n" + b.display)
    s.paint(7, 1)
    info("\n" + s.display)
  }
}
