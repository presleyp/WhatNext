package test.scala
import whatnext._

//JUnit and ScalaTest aren't working yet so I'll check things here
 
object interimTests {
  val t1 = new Task("go camping", List())         //> t1  : whatnext.Task = Task(go camping,List(),,0,)
  val t2 = new Task("wash clothes", List("go camping"))
                                                  //> t2  : whatnext.Task = Task(wash clothes,List(go camping),,0,)
  val t3 = new Task("dry clothes", List("wash clothes"))
                                                  //> t3  : whatnext.Task = Task(dry clothes,List(wash clothes),,0,)
  val t4 = new Task("walk dog", List("go camping"))
                                                  //> t4  : whatnext.Task = Task(walk dog,List(go camping),,0,)
  val t5 = new Task("read", List())               //> t5  : whatnext.Task = Task(read,List(),,0,)
  
  val p = new Project()                           //> p  : whatnext.Project = whatnext.Project@52c05d3b
  //Map()
  p.tasks                                         //> res0: Map[String,whatnext.Task] = Map()
  // Set()
  p.whatNext                                      //> res1: Set[String] = Set()
  
  val p1 = p + t1                                 //> p1  : whatnext.Project = whatnext.Project@49ff0dde
  //Map("go camping", t1)
  p1.tasks                                        //> res2: Map[String,whatnext.Task] = Map(go camping -> Task(go camping,List(),,
                                                  //| 0,))
  //Set("go camping")
  p1.whatNext                                     //> res3: Set[String] = Set(go camping)
  
  
  val p2 = p1 + t2                                //> p2  : whatnext.Project = whatnext.Project@2b20bf2c
  //Set("go camping")
  p2.whatNext                                     //> res4: Set[String] = Set(go camping)
  
  val p3 = p2.checkOff("go camping")              //> p3  : whatnext.Project = whatnext.Project@303bc257
  //Set("wash clothes")
  p3.whatNext                                     //> res5: Set[String] = Set(wash clothes)
  
  //ParentException
  val p4 = try {p3 + t4}
  catch {
    case ParentException(msg) => msg
  }                                               //> p4  : Object = That task's prerequisites aren't in the project. Add them and
                                                  //|  then try again.
  
  val p5 = p3 + t5                                //> p5  : whatnext.Project = whatnext.Project@2353f67e
  //Set("wash clothes", "read")
  p5.whatNext                                     //> res6: Set[String] = Set(wash clothes, read)
 
  val p6 = p5.checkOff("wash clothes").checkOff("read")
                                                  //> p6  : whatnext.Project = whatnext.Project@39dd3812
  //Set()
  p6.whatNext                                     //> res7: Set[String] = Set()
  
  //TaskNotFoundException
  val p7 = try {p6.checkOff("phantom task")}
  catch {
    case TaskNotFoundException(msg) => msg
  }                                               //> p7  : Object = That task isn't in this project.
  
  //ParentException
  val a = new Task("a", List("b"))                //> a  : whatnext.Task = Task(a,List(b),,0,)
  val b = new Task("b", List("a"))                //> b  : whatnext.Task = Task(b,List(a),,0,)
  val bad = try {new Project(List(a, b))}
  catch {
    case ParentException(msg) => msg
  }                                               //> bad  : Object = Something is wrong with the following node(s): a, b.
                                                  //|             Either their parents aren't in the project or they are prerequi
                                                  //| sites to tasks that are prerequisites
                                                  //|             to them, so that they could never be ready to do.
  val c = new Task("c", List())                   //> c  : whatnext.Task = Task(c,List(),,0,)
  val d = new Task("d", List("c"))                //> d  : whatnext.Task = Task(d,List(c),,0,)
  val good = new Project(List(c, d))              //> good  : whatnext.Project = whatnext.Project@2e958bb8
                                                  
}