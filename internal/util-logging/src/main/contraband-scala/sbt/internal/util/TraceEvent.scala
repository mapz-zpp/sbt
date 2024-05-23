/**
 * This code is generated using [[https://www.scala-sbt.org/contraband/ sbt-contraband]].
 */

// DO NOT EDIT MANUALLY
package sbt.internal.util
final class TraceEvent private (
  val level: String,
  val message: Throwable,
  channelName: Option[String],
  execId: Option[String],
  originId: Option[String]) extends sbt.internal.util.AbstractEntry(channelName, execId, originId) with Serializable {
  
  
  
  override def equals(o: Any): Boolean = this.eq(o.asInstanceOf[AnyRef]) || (o match {
    case x: TraceEvent => (this.level == x.level) && (this.message == x.message) && (this.channelName == x.channelName) && (this.execId == x.execId) && (this.originId == x.originId)
    case _ => false
  })
  override def hashCode: Int = {
    37 * (37 * (37 * (37 * (37 * (37 * (17 + "sbt.internal.util.TraceEvent".##) + level.##) + message.##) + channelName.##) + execId.##) + originId.##)
  }
  override def toString: String = {
    "TraceEvent(" + level + ", " + message + ", " + channelName + ", " + execId + ", " + originId + ")"
  }
  private[this] def copy(level: String = level, message: Throwable = message, channelName: Option[String] = channelName, execId: Option[String] = execId, originId: Option[String] = originId): TraceEvent = {
    new TraceEvent(level, message, channelName, execId, originId)
  }
  def withLevel(level: String): TraceEvent = {
    copy(level = level)
  }
  def withMessage(message: Throwable): TraceEvent = {
    copy(message = message)
  }
  def withChannelName(channelName: Option[String]): TraceEvent = {
    copy(channelName = channelName)
  }
  def withChannelName(channelName: String): TraceEvent = {
    copy(channelName = Option(channelName))
  }
  def withExecId(execId: Option[String]): TraceEvent = {
    copy(execId = execId)
  }
  def withExecId(execId: String): TraceEvent = {
    copy(execId = Option(execId))
  }
  def withOriginId(originId: Option[String]): TraceEvent = {
    copy(originId = originId)
  }
  def withOriginId(originId: String): TraceEvent = {
    copy(originId = Option(originId))
  }
}
object TraceEvent {
  
  def apply(level: String, message: Throwable, channelName: Option[String], execId: Option[String], originId: Option[String]): TraceEvent = new TraceEvent(level, message, channelName, execId, originId)
  def apply(level: String, message: Throwable, channelName: String, execId: String, originId: String): TraceEvent = new TraceEvent(level, message, Option(channelName), Option(execId), Option(originId))
}
