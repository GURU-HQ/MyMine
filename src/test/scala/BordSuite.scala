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
  
  val b = new Bord(10, 10) with BordUI with Test with Manip
  
  test("Test1") {
    logger.info("\n" + b.display)
    logger.info(b.spaceList.toString)
  }
  
  test("RandTest") {
    b.randBomb(30)
    logger.info("\n" + b.display)
    logger.info(b.spaceList.toString)
  }
  
  test("Splite Test") {
    b.randBomb(30)
    val s =new Splite(b)
    logger.info("\n" + s.display)
  }
  
  test("Splite Test2") {
    b.randBomb(30)
    val s =new Splite(b)
    logger.info("\n" + s.display)
  }
}