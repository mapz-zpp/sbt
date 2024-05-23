/**
 * This code is generated using [[https://www.scala-sbt.org/contraband/ sbt-contraband]].
 */

// DO NOT EDIT MANUALLY
import _root_.sjsonnew.{ Unbuilder, Builder, JsonFormat, deserializationError }
trait ExecFormats { self: CommandSourceFormats with sjsonnew.BasicJsonProtocol =>
implicit lazy val ExecFormat: JsonFormat[sbt.Exec] = new JsonFormat[sbt.Exec] {
  override def read[J](__jsOpt: Option[J], unbuilder: Unbuilder[J]): sbt.Exec = {
    __jsOpt match {
      case Some(__js) =>
      unbuilder.beginObject(__js)
      val commandLine = unbuilder.readField[String]("commandLine")
      val execId = unbuilder.readField[Option[String]]("execId")
      val source = unbuilder.readField[Option[sbt.CommandSource]]("source")
      val originId = unbuilder.readField[Option[String]]("originId")
      unbuilder.endObject()
      sbt.Exec(commandLine, execId, source, originId)
      case None =>
      deserializationError("Expected JsObject but found None")
    }
  }
  override def write[J](obj: sbt.Exec, builder: Builder[J]): Unit = {
    builder.beginObject()
    builder.addField("commandLine", obj.commandLine)
    builder.addField("execId", obj.execId)
    builder.addField("source", obj.source)
    builder.addField("originId", obj.originId)
    builder.endObject()
  }
}
}
