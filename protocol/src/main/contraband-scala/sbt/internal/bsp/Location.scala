/**
 * This code is generated using [[https://www.scala-sbt.org/contraband/ sbt-contraband]].
 */

// DO NOT EDIT MANUALLY
package sbt.internal.bsp
final class Location private (
  val uri: java.net.URI,
  val range: sbt.internal.bsp.Range) extends Serializable {
  
  
  
  override def equals(o: Any): Boolean = this.eq(o.asInstanceOf[AnyRef]) || (o match {
    case x: Location => (this.uri == x.uri) && (this.range == x.range)
    case _ => false
  })
  override def hashCode: Int = {
    37 * (37 * (37 * (17 + "sbt.internal.bsp.Location".##) + uri.##) + range.##)
  }
  override def toString: String = {
    "Location(" + uri + ", " + range + ")"
  }
  private[this] def copy(uri: java.net.URI = uri, range: sbt.internal.bsp.Range = range): Location = {
    new Location(uri, range)
  }
  def withUri(uri: java.net.URI): Location = {
    copy(uri = uri)
  }
  def withRange(range: sbt.internal.bsp.Range): Location = {
    copy(range = range)
  }
}
object Location {
  
  def apply(uri: java.net.URI, range: sbt.internal.bsp.Range): Location = new Location(uri, range)
}
