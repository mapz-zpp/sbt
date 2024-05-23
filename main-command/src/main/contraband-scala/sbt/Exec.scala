/**
 * This code is generated using [[https://www.scala-sbt.org/contraband/ sbt-contraband]].
 */

// DO NOT EDIT MANUALLY
package sbt
final class Exec private (
  val commandLine: String,
  val execId: Option[String],
  val source: Option[sbt.CommandSource],
  val originId: Option[String]) extends Serializable {
  
  private def this(commandLine: String, source: Option[sbt.CommandSource], originId: Option[String]) = this(commandLine, None, source, originId)
  
  override def equals(o: Any): Boolean = this.eq(o.asInstanceOf[AnyRef]) || (o match {
    case x: Exec => (this.commandLine == x.commandLine) && (this.execId == x.execId) && (this.source == x.source) && (this.originId == x.originId)
    case _ => false
  })
  override def hashCode: Int = {
    37 * (37 * (37 * (37 * (37 * (17 + "sbt.Exec".##) + commandLine.##) + execId.##) + source.##) + originId.##)
  }
  override def toString: String = {
    "Exec(" + commandLine + ", " + execId + ", " + source + ", " + originId + ")"
  }
  private[this] def copy(commandLine: String = commandLine, execId: Option[String] = execId, source: Option[sbt.CommandSource] = source, originId: Option[String] = originId): Exec = {
    new Exec(commandLine, execId, source, originId)
  }
  def withCommandLine(commandLine: String): Exec = {
    copy(commandLine = commandLine)
  }
  def withExecId(execId: Option[String]): Exec = {
    copy(execId = execId)
  }
  def withExecId(execId: String): Exec = {
    copy(execId = Option(execId))
  }
  def withSource(source: Option[sbt.CommandSource]): Exec = {
    copy(source = source)
  }
  def withSource(source: sbt.CommandSource): Exec = {
    copy(source = Option(source))
  }
  def withOriginId(originId: Option[String]): Exec = {
    copy(originId = originId)
  }
  def withOriginId(originId: String): Exec = {
    copy(originId = Option(originId))
  }
}
object Exec {
  def newExecId: String = java.util.UUID.randomUUID.toString
  def apply(commandLine: String, source: Option[sbt.CommandSource], originId: Option[String]): Exec = new Exec(commandLine, source, originId)
  def apply(commandLine: String, source: sbt.CommandSource, originId: String): Exec = new Exec(commandLine, Option(source), Option(originId))
  def apply(commandLine: String, execId: Option[String], source: Option[sbt.CommandSource], originId: Option[String]): Exec = new Exec(commandLine, execId, source, originId)
  def apply(commandLine: String, execId: String, source: sbt.CommandSource, originId: String): Exec = new Exec(commandLine, Option(execId), Option(source), Option(originId))
}
