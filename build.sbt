scalaVersion := "3.1.1"

// Set to false or remove if you want to show stubs as linking errors
nativeLinkStubs := true

import scala.scalanative.build._

nativeConfig ~= {
  _.withLTO(LTO.thin)
    .withMode(Mode.debug)
    .withGC(GC.none)
}

enablePlugins(ScalaNativePlugin)

libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.12"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.12" % "test"




