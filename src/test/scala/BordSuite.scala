package jp.co.guru.MyMine

import com.typesafe.scalalogging.slf4j.Logging
import org.scalatest.FunSuite

class BordSuite extends FunSuite with Logging {
  val h = new Bord(1000, 1000) with Manip
  
  test("Dummy") {
	  h.spaceList
  }
  
  test("Heavy1") {
    h.randBomb(100)
  }
  
  val b = new Bord(10, 10) with BordUI with Manip
  
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
    val s =new Splite(b)
    info("\n" + s.display)
  }
  
  test("Splite Test2") {
    b.randBomb(30)
    val s =new Splite(b)
    info("\n" + s.display)
  }
}

class SpliteSuite extends FunSuite {
  test("Update Test") {
    val b = new Bord(10, 10) with BordUI with Manip
    val s = new Splite(b)
    (0 to 9).foreach(i => b.put(i, i))
//    b.put(5, 5)
    info("\n" + b.display)
//    b.around(5, 5).foreach(tp => s.update(tp._1, tp._2))
    s.paint(7, 1)
    info("\n" + s.display)
  }
}
