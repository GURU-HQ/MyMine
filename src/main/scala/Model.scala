package jp.co.guru.MyMine

import Util._

/**
 * データモデルインターフェイス
 */
trait Model {
  def getX(): Int
  def getY(): Int
  def getValue(x: Int, y: Int): Status
  def open(p: POS): Boolean
  def flag(p: POS): Boolean
}
