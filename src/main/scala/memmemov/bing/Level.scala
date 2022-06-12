package memmemov.bing

import scala.scalanative.unsigned.UByte

private[bing] class Level(
  private[Level] val number: Int = 0
) extends Ordered[Level]:

  def createStore: Store =

    new Store(
      new Array[Block](number + 1).map(_ => new Block)
    )

  def createStock: Stock =

    val nextLevel = new Level(number + 1)
    new Stock(
      new Array[Element](Level.size).map(_ => new Element(nextLevel))
    )

  sealed trait PadBig
  case class PaddedBig(padded: Address) extends PadBig
  object NotPaddedBigAlreadyGreater extends PadBig

  def padBig(content: Address): PadBig =

    content.padBig(number + 1) match
      case content.NotPaddedBigAlreadyGreater =>
        NotPaddedBigAlreadyGreater
      case content.PaddedBig(paddedContent) =>
        PaddedBig(content)

  override def compare(that: Level): Int =

    this.number - that.number

  override def equals(that: Any): Boolean =

    that match
      case that: Level => compare(that) == 0
      case _ => false

object Level:

  private lazy val size: Int = UByte.MaxValue.toInt + 1