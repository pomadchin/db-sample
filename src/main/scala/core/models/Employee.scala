package core.models

import core.db._

import upickle.default.{ReadWriter => RW, macroRW}
import scalafx.beans.property._

case class Employee(fio: String, salary: Double) extends Identifiable {
  lazy val vFio    = new StringProperty(this, "fio", fio)
  lazy val vSalary = new StringProperty(this, "salary", salary.toString)
}

object Employee {
  implicit val rw: RW[Employee] = macroRW
}
