/**
 * This code is generated using [[https://www.scala-sbt.org/contraband/ sbt-contraband]].
 */

// DO NOT EDIT MANUALLY
package sbt.internal.bsp
final class TestFinish private (
  val displayName: String,
  val message: Option[String],
  val status: Int,
  val location: Option[sbt.internal.bsp.Location],
  val dataKind: Option[String],
  val data: Option[sjsonnew.shaded.scalajson.ast.unsafe.JValue]) extends Serializable {
  
  
  
  override def equals(o: Any): Boolean = this.eq(o.asInstanceOf[AnyRef]) || (o match {
    case x: TestFinish => (this.displayName == x.displayName) && (this.message == x.message) && (this.status == x.status) && (this.location == x.location) && (this.dataKind == x.dataKind) && (this.data == x.data)
    case _ => false
  })
  override def hashCode: Int = {
    37 * (37 * (37 * (37 * (37 * (37 * (37 * (17 + "sbt.internal.bsp.TestFinish".##) + displayName.##) + message.##) + status.##) + location.##) + dataKind.##) + data.##)
  }
  override def toString: String = {
    "TestFinish(" + displayName + ", " + message + ", " + status + ", " + location + ", " + dataKind + ", " + data + ")"
  }
  private[this] def copy(displayName: String = displayName, message: Option[String] = message, status: Int = status, location: Option[sbt.internal.bsp.Location] = location, dataKind: Option[String] = dataKind, data: Option[sjsonnew.shaded.scalajson.ast.unsafe.JValue] = data): TestFinish = {
    new TestFinish(displayName, message, status, location, dataKind, data)
  }
  def withDisplayName(displayName: String): TestFinish = {
    copy(displayName = displayName)
  }
  def withMessage(message: Option[String]): TestFinish = {
    copy(message = message)
  }
  def withMessage(message: String): TestFinish = {
    copy(message = Option(message))
  }
  def withStatus(status: Int): TestFinish = {
    copy(status = status)
  }
  def withLocation(location: Option[sbt.internal.bsp.Location]): TestFinish = {
    copy(location = location)
  }
  def withLocation(location: sbt.internal.bsp.Location): TestFinish = {
    copy(location = Option(location))
  }
  def withDataKind(dataKind: Option[String]): TestFinish = {
    copy(dataKind = dataKind)
  }
  def withDataKind(dataKind: String): TestFinish = {
    copy(dataKind = Option(dataKind))
  }
  def withData(data: Option[sjsonnew.shaded.scalajson.ast.unsafe.JValue]): TestFinish = {
    copy(data = data)
  }
  def withData(data: sjsonnew.shaded.scalajson.ast.unsafe.JValue): TestFinish = {
    copy(data = Option(data))
  }
}
object TestFinish {
  
  def apply(displayName: String, message: Option[String], status: Int, location: Option[sbt.internal.bsp.Location], dataKind: Option[String], data: Option[sjsonnew.shaded.scalajson.ast.unsafe.JValue]): TestFinish = new TestFinish(displayName, message, status, location, dataKind, data)
  def apply(displayName: String, message: String, status: Int, location: sbt.internal.bsp.Location, dataKind: String, data: sjsonnew.shaded.scalajson.ast.unsafe.JValue): TestFinish = new TestFinish(displayName, Option(message), status, Option(location), Option(dataKind), Option(data))
}
