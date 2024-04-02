/**
 * This code is generated using [[https://www.scala-sbt.org/contraband/ sbt-contraband]].
 */

// DO NOT EDIT MANUALLY
package sbt.internal.bsp
final class TestStart private (
  val displayName: String,
  val location: Option[sbt.internal.bsp.Location]) extends Serializable {
  
  
  
  override def equals(o: Any): Boolean = this.eq(o.asInstanceOf[AnyRef]) || (o match {
    case x: TestStart => (this.displayName == x.displayName) && (this.location == x.location)
    case _ => false
  })
  override def hashCode: Int = {
    37 * (37 * (37 * (17 + "sbt.internal.bsp.TestStart".##) + displayName.##) + location.##)
  }
  override def toString: String = {
    "TestStart(" + displayName + ", " + location + ")"
  }
  private[this] def copy(displayName: String = displayName, location: Option[sbt.internal.bsp.Location] = location): TestStart = {
    new TestStart(displayName, location)
  }
  def withDisplayName(displayName: String): TestStart = {
    copy(displayName = displayName)
  }
  def withLocation(location: Option[sbt.internal.bsp.Location]): TestStart = {
    copy(location = location)
  }
  def withLocation(location: sbt.internal.bsp.Location): TestStart = {
    copy(location = Option(location))
  }
}
object TestStart {
  
  def apply(displayName: String, location: Option[sbt.internal.bsp.Location]): TestStart = new TestStart(displayName, location)
  def apply(displayName: String, location: sbt.internal.bsp.Location): TestStart = new TestStart(displayName, Option(location))
}
