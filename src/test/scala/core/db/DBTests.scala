package core.db

import core.models._
import org.junit.Test
import org.junit.Assert._

class DBTests {

  @Test
  def testEmployeeTableFunctions {

    val e0 = Employee("e0", 0.0)
    val e1 = Employee("e1", 1.0)
    val e2 = Employee("e2", 2.0)
    val e3 = Employee("e1", 2.0)

    EmployeeTable.Add(e0)
    e0.id = Some(0)
    assertEquals(e0, EmployeeTable.Get(0))

    EmployeeTable.Add(e1)
    e1.id = Some(1)
    assertEquals(e1, EmployeeTable.Get(1))

    EmployeeTable.Add(e2)
    e2.id = Some(2)
    assertEquals(e2, EmployeeTable.Get(2))

    EmployeeTable.Add(e3)
    e3.id = Some(3)
    assertEquals(e3, EmployeeTable.Get(3))

    assertEquals(List(e0, e1, e2, e3), EmployeeTable.list)

    assertEquals(
      List(e1, e3), EmployeeTable.find(Option("e1") -> None)
    )

    assertEquals(
      List(e2, e3), EmployeeTable.find(None -> Option(2.0))
    )

    assertEquals(
      List(e1), EmployeeTable.find(Option("e1") -> Option(1.0))
    )

    assertEquals(
      List(), EmployeeTable.find(None -> None)
    )

    assertEquals(
      List(), EmployeeTable.find(Option("e123") -> None)
    )

    EmployeeTable.Delete(1)
    assertEquals(List(e0, e2, e3), EmployeeTable.list)

    EmployeeTable.Delete(2)
    assertEquals(List(e0, e3), EmployeeTable.list)

    EmployeeTable.Delete(0)
    EmployeeTable.Delete(3)
    assertEquals(List(), EmployeeTable.list)
  }

  @Test
  def testManagerTableFunctions {

    val m0 = Manager("m0", "p0")
    val m1 = Manager("m1", "p1")
    val m2 = Manager("m2", "p2")
    val m3 = Manager("m1", "p2")

    ManagerTable.Add(m0)
    m0.id = Some(0)
    assertEquals(m0, ManagerTable.Get(0))

    ManagerTable.Add(m1)
    m1.id = Some(1)
    assertEquals(m1, ManagerTable.Get(1))

    ManagerTable.Add(m2)
    m2.id = Some(2)
    assertEquals(m2, ManagerTable.Get(2))

    ManagerTable.Add(m3)
    m3.id = Some(3)
    assertEquals(m3, ManagerTable.Get(3))

    assertEquals(List(m0, m1, m2, m3), ManagerTable.list)

    assertEquals(
      List(m1, m3), ManagerTable.find(Option("m1") -> None)
    )

    assertEquals(
      List(m2, m3), ManagerTable.find(None -> Option("p2"))
    )

    assertEquals(
      List(m1), ManagerTable.find(Option("m1") -> Option("p1"))
    )

    assertEquals(
      List(), ManagerTable.find(None -> None)
    )

    assertEquals(
      List(), ManagerTable.find(Option("m123") -> None)
    )

    ManagerTable.Delete(1)
    assertEquals(List(m0, m2, m3), ManagerTable.list)

    ManagerTable.Delete(2)
    assertEquals(List(m0, m3), ManagerTable.list)

    ManagerTable.Delete(0)
    ManagerTable.Delete(3)
    assertEquals(List(), ManagerTable.list)
  }

  @Test
  def testTaskTableFunctions {
    
    val t0 = Task("t0")
    val t1 = Task("t1")
    val t2 = Task("t2")
    val t3 = Task("t1")

    TaskTable.Add(t0)
    t0.id = Some(0)
    assertEquals(t0, TaskTable.Get(0))

    TaskTable.Add(t1)
    t1.id = Some(1)
    assertEquals(t1, TaskTable.Get(1))

    TaskTable.Add(t2)
    t2.id = Some(2)
    assertEquals(t2, TaskTable.Get(2))

    TaskTable.Add(t3)
    t3.id = Some(3)
    assertEquals(t3, TaskTable.Get(3))

    assertEquals(List(t0, t1, t2, t3), TaskTable.list)

    assertEquals(
      List(t1, t3), TaskTable.find(Option("t1"))
    )

    assertEquals(
      List(t0), TaskTable.find(Option("t0"))
    )

    assertEquals(
      List(), TaskTable.find(None)
    )

    assertEquals(
      List(), TaskTable.find(Option("m123"))
    )

    TaskTable.Delete(1)
    assertEquals(List(t0, t2, t3), TaskTable.list)

    TaskTable.Delete(2)
    assertEquals(List(t0, t3), TaskTable.list)

    TaskTable.Delete(0)
    TaskTable.Delete(3)
    assertEquals(List(), TaskTable.list)
  }

  @Test
  def testEmployeeTaskTableFunctions {

    EmployeeTaskTable.AddLink(0,0)
    EmployeeTaskTable.AddLink(EmployeeTask(1,0))
    EmployeeTaskTable.AddLink(2,0)
    EmployeeTaskTable.AddLink(2,2)
    EmployeeTaskTable.AddLink(2,3)

    assertEquals(
      List(
        EmployeeTask(0,0), EmployeeTask(1,0),
        EmployeeTask(2,0), EmployeeTask(2,2),
        EmployeeTask(2,3)
      ), EmployeeTaskTable.list
    )

    EmployeeTaskTable.DeleteLink(2,3)

    assertEquals(
      List(
        EmployeeTask(0,0), EmployeeTask(1,0),
        EmployeeTask(2,0), EmployeeTask(2,2)
      ), EmployeeTaskTable.list
    )

    assertEquals(List(0), EmployeeTaskTable.GetTargetIds(0))

    assertEquals(0, EmployeeTaskTable.GetSourceId(0))

    assertEquals(EmployeeTask(2,2), EmployeeTaskTable.GetLink(2,2))
  }

  @Test
  def testManagerTaskTableFunctions {

    ManagerTaskTable.AddLink(0,0)
    ManagerTaskTable.AddLink(ManagerTask(1,0))
    ManagerTaskTable.AddLink(2,0)
    ManagerTaskTable.AddLink(2,2)
    ManagerTaskTable.AddLink(2,3)

    assertEquals(
      List(
        ManagerTask(0,0), ManagerTask(1,0),
        ManagerTask(2,0), ManagerTask(2,2),
        ManagerTask(2,3)
      ), ManagerTaskTable.list
    )

    ManagerTaskTable.DeleteLink(2,3)

    assertEquals(
      List(
        ManagerTask(0,0), ManagerTask(1,0),
        ManagerTask(2,0), ManagerTask(2,2)
      ), ManagerTaskTable.list
    )

    assertEquals(List(0), ManagerTaskTable.GetTargetIds(0))

    assertEquals(0, ManagerTaskTable.GetSourceId(0))

    assertEquals(ManagerTask(2,2), ManagerTaskTable.GetLink(2,2))

    ManagerTaskTable.list = List(
      ManagerTask(0,0), ManagerTask(1,0),
      ManagerTask(2,0), ManagerTask(2,2),
      ManagerTask(2,3)
    )
  }
}
