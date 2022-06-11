package memmemov.bing

import scala.scalanative.unsigned.UByte

class Level(
  private val number: Int = 0
):

  def createStore: Store =
    new Store(
      new Array[Block](number + 1).map(_ => new Block)
    )

  def createStock: Stock =
    val nextLevel = new Level(number + 1)
    new Stock(
      new Array[Element](Level.size).map(_ => new Element(nextLevel))
    )

object Level:

  private lazy val size: Int = UByte.MaxValue.toInt + 1