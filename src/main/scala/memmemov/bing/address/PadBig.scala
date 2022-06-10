package memmemov.bing.address

sealed trait PadBig
case class PaddedBig(value: Instance) extends PadBig
object NotPaddedBigAlreadyGreater extends PadBig
