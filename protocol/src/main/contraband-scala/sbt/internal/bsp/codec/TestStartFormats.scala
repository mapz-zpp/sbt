/**
 * This code is generated using [[https://www.scala-sbt.org/contraband/ sbt-contraband]].
 */

// DO NOT EDIT MANUALLY
package sbt.internal.bsp.codec
import _root_.sjsonnew.{ Unbuilder, Builder, JsonFormat, deserializationError }
trait TestStartFormats { self: sbt.internal.bsp.codec.LocationFormats with sbt.internal.bsp.codec.RangeFormats with sbt.internal.bsp.codec.PositionFormats with sjsonnew.BasicJsonProtocol =>
implicit lazy val TestStartFormat: JsonFormat[sbt.internal.bsp.TestStart] = new JsonFormat[sbt.internal.bsp.TestStart] {
  override def read[J](__jsOpt: Option[J], unbuilder: Unbuilder[J]): sbt.internal.bsp.TestStart = {
    __jsOpt match {
      case Some(__js) =>
      unbuilder.beginObject(__js)
      val displayName = unbuilder.readField[String]("displayName")
      val location = unbuilder.readField[Option[sbt.internal.bsp.Location]]("location")
      unbuilder.endObject()
      sbt.internal.bsp.TestStart(displayName, location)
      case None =>
      deserializationError("Expected JsObject but found None")
    }
  }
  override def write[J](obj: sbt.internal.bsp.TestStart, builder: Builder[J]): Unit = {
    builder.beginObject()
    builder.addField("displayName", obj.displayName)
    builder.addField("location", obj.location)
    builder.endObject()
  }
}
}
