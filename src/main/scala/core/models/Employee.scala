package core.models

import scalafx.beans.property._
import core.db._

case class Employee(fio: String, salary: Double) extends Identifiable {
  lazy val vFio    = new StringProperty(this, "fio", fio)
  lazy val vSalary = new StringProperty(this, "salary", salary.toString)
}
