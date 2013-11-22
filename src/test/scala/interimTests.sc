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
  
  val p = new Project()                           //> p  : whatnext.Project = whatnext.Project@165973ea
  //Map()
  p.tasks                                         //> res0: Map[String,whatnext.Task] = Map()
  // Set()
  p.whatNext                                      //> res1: Set[String] = Set()
  
  //TODO what should the behavior of checkOff be here?
  
  val p1 = p + t1                                 //> p1  : whatnext.Project = whatnext.Project@58f39b3a
  //Map("go camping", t1)
  p1.tasks                                        //> res2: Map[String,whatnext.Task] = Map(go camping -> Task(go camping,List(),,
                                                  //| 0,))
  //Set("go camping")
  p1.whatNext                                     //> res3: Set[String] = Set(go camping)
  
  
  val p2 = p1 + t2                                //> p2  : whatnext.Project = whatnext.Project@7e78fc6
  //Set("go camping")
  p2.whatNext                                     //> res4: Set[String] = Set(go camping)
  
  val p3 = p2.checkOff("go camping")              //> p3  : whatnext.Project = whatnext.Project@68da4b71
  //Set("wash clothes")
  p3.whatNext                                     //> res5: Set[String] = Set(wash clothes)
  
  // TODO if I add t4 here it will think it's not ready even
  // though it is.
  
  val p4 = p3 + t5                                //> p4  : whatnext.Project = whatnext.Project@538f1d7e
  //Set("wash clothes", "read")
  p4.whatNext                                     //> res6: Set[String] = Set(wash clothes, read)
 
  val p5 = p4.checkOff("wash clothes").checkOff("read")
                                                  //> p5  : whatnext.Project = whatnext.Project@28bb0d0d
  //Set() FIXME should maybe return "all done!"
  p5.whatNext                                     //> res7: Set[String] = Set()
  
  //TODO should warn user
  val p6 = p5.checkOff("phantom task")            //> p6  : whatnext.Project = whatnext.Project@1055e55f
                                                  
}