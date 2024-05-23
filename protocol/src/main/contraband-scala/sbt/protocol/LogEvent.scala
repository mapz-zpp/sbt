/**
 * This code is generated using [[https://www.scala-sbt.org/contraband/ sbt-contraband]].
 */

// DO NOT EDIT MANUALLY
package sbt.protocol
/** Log event. */
final class LogEvent private (
  val level: String,
  val message: String,
  val originId: Option[String]) extends sbt.protocol.EventMessage() with Serializable {
  
  
  
  override def equals(o: Any): Boolean = this.eq(o.asInstanceOf[AnyRef]) || (o match {
    case x: LogEvent => (this.level == x.level) && (this.message == x.message) && (this.originId == x.originId)
    case _ => false
  })
  override def hashCode: Int = {
    37 * (37 * (37 * (37 * (17 + "sbt.protocol.LogEvent".##) + level.##) + message.##) + originId.##)
  }
  override def toString: String = {
    "LogEvent(" + level + ", " + message + ", " + originId + ")"
  }
  private[this] def copy(level: String = level, message: String = message, originId: Option[String] = originId): LogEvent = {
    new LogEvent(level, message, originId)
  }
  def withLevel(level: String): LogEvent = {
    copy(level = level)
  }
  def withMessage(message: String): LogEvent = {
    copy(message = message)
  }
  def withOriginId(originId: Option[String]): LogEvent = {
    copy(originId = originId)
  }
  def withOriginId(originId: String): LogEvent = {
    copy(originId = Option(originId))
  }
}
object LogEvent {
  
  def apply(level: String, message: String, originId: Option[String]): LogEvent = new LogEvent(level, message, originId)
  def apply(level: String, message: String, originId: String): LogEvent = new LogEvent(level, message, Option(originId))
}
