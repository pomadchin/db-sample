package core.models

import scalafx.beans.property._
import core.db._

case class Employee(fio: String, salary: Double) extends Identifiable {
  val vFio    = new StringProperty(this, "fio", fio)
  val vSalary = new StringProperty(this, "salary", salary.toString)
}

