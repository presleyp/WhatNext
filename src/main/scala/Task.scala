package whatnext
import scala.util.Sorting
//import com.github.nscala_time.time.Imports._

//TODO time stuff
//TODO edit method (and convenience methods like snooze)
//TODO visualization

case class Task(val title: String,
                val parents: List[String],
                //val children: List[String] = List(), 
                val description: String = "",
                val importance: Int = 0,
                val context: String = ""
                //,val duration: Duration,
                //val dueDate: DateTime
               ){
  
  /*
  object TaskField extends Enumeration {
    type TaskField = Value
    val title, parents, description, importance, context = Value
  }
  def edit(change: Map[TaskField, Any]): Task = {
    
  }
  */
  
  def hasParent(title: String): Boolean = {
    parents contains title
  }
  
  def addParents(tasks: List[String]): Task = {
    val newParents = parents ++ tasks
    new Task(title, newParents, description, importance, context)
  }
  
  def removeParent(task: String): Task = {
    val newParents = parents filterNot (parent => task == parent)
    new Task(title, newParents, description, importance, context)
  }
}