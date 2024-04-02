/**
 * This code is generated using [[https://www.scala-sbt.org/contraband/ sbt-contraband]].
 */

// DO NOT EDIT MANUALLY
package sbt.internal.bsp.codec
import _root_.sjsonnew.{ Unbuilder, Builder, JsonFormat, deserializationError }
trait TestFinishFormats { self: sbt.internal.bsp.codec.LocationFormats with sbt.internal.bsp.codec.RangeFormats with sbt.internal.bsp.codec.PositionFormats with sjsonnew.BasicJsonProtocol with sbt.internal.util.codec.JValueFormats =>
implicit lazy val TestFinishFormat: JsonFormat[sbt.internal.bsp.TestFinish] = new JsonFormat[sbt.internal.bsp.TestFinish] {
  override def read[J](__jsOpt: Option[J], unbuilder: Unbuilder[J]): sbt.internal.bsp.TestFinish = {
    __jsOpt match {
      case Some(__js) =>
      unbuilder.beginObject(__js)
      val displayName = unbuilder.readField[String]("displayName")
      val message = unbuilder.readField[Option[String]]("message")
      val status = unbuilder.readField[Int]("status")
      val location = unbuilder.readField[Option[sbt.internal.bsp.Location]]("location")
      val dataKind = unbuilder.readField[Option[String]]("dataKind")
      val data = unbuilder.readField[Option[sjsonnew.shaded.scalajson.ast.unsafe.JValue]]("data")
      unbuilder.endObject()
      sbt.internal.bsp.TestFinish(displayName, message, status, location, dataKind, data)
      case None =>
      deserializationError("Expected JsObject but found None")
    }
  }
  override def write[J](obj: sbt.internal.bsp.TestFinish, builder: Builder[J]): Unit = {
    builder.beginObject()
    builder.addField("displayName", obj.displayName)
    builder.addField("message", obj.message)
    builder.addField("status", obj.status)
    builder.addField("location", obj.location)
    builder.addField("dataKind", obj.dataKind)
    builder.addField("data", obj.data)
    builder.endObject()
  }
}
}
