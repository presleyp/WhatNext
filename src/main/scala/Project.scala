package whatnext

//TODO main (think about interface)
//TODO proper tests
//TODO change constructor so you give a list of tasks, not a map. also avoid unnecessary cycle checking.
//TODO improve whatNext interface
//TODO uncomment edit when implemented in Task and add error checking for cycles
//TODO make task ready or not based on time, not just parents
//TODO visualization

case class ParentException(message: String) extends Exception(message)
case class TaskNotFoundException(message: String) extends Exception(message)

class Project(initialTasks: Map[String, Task] = Map()) {
    val tasks = fillTasks(initialTasks, Map())
  
    def fillTasks(inputMap: Map[String, Task], resultMap: Map[String, Task]): Map[String, Task] = {
      val (topLevel, rest) = inputMap partition {
        case (name, task) => task.parents.forall(parent => resultMap contains parent)
      }
      if (rest.isEmpty){
        topLevel ++ resultMap
      }
      else {
        if (topLevel.isEmpty){
          throw ParentException(s"""Something is wrong with the following task(s): ${rest.keys.mkString(", ")}.
              Either their parents aren't in the project or they are prerequisites to tasks that are prerequisites
              to them, so that they could never be ready to do.""")
        }
        else {
          fillTasks(inputMap -- topLevel.map({case (name, task) => name}), resultMap ++ topLevel)
        }
      }
    }

  def get(key: String): Option[Task] = {
    if (tasks contains key) Some(tasks(key))
    else None
  }
  
  def + (task: Task): Project = { // error if task's parents aren't in tasks
    if (task.parents.forall(parent => tasks contains parent)){
      new Project(tasks + ((task.title, task)))
    } else {
      throw ParentException("That task's prerequisites aren't in the project. Add them and then try again.")
    }

  }
  
  def whatNext(): Set[String] = { // tell user if there aren't any more tasks
    val nextTasks = tasks filter {case (title, task) => task.parents.isEmpty}
    nextTasks map {case (title, task) => title} toSet
  }
  
/*  
  def edit(task: Task, change: Map[String, Any]): Project = { // search for cycles
    val newTask = task.edit(change)
    new Project(tasks - task.title + ((newTask.title, newTask)))
  }

  def whatNext[T](filterFunction: Task => Boolean = (x => true))
                 (sortFunction: (Task => T))
                 (implicit ord: Ordering[T]): List[String] = {
    val nextTasks: Iterable[Task] = tasks map {case (_, task) => task} filter (x => x.parents.isEmpty)
    val filtered = nextTasks filter filterFunction
    val sorted = filtered.toList sortBy sortFunction
    sorted map (_.title)
  }
*/    
  def checkOff(title: String): Project = { // warn user if they try to check off a task that isn't here
    if (tasks contains title) {
          //remove this task
    val checkedOff = tasks - title
    
    //remove old children from tasks
    val children = tasks filter {case (name, task) => task hasParent title}
    val tasksMinusChildren = checkedOff -- (children map {case (name, child) => name})
    
    //add new children to tasks
    val newChildren = for ((name, child) <- children) yield (name, child.removeParent(title))
    val newTasks = tasksMinusChildren ++ newChildren
    
    new Project(newTasks)
    } else {
      throw TaskNotFoundException("That task isn't in this project.")
    }
  }
  
  def showAll(): Set[Task] = {
    (for ((title, task) <- tasks) yield task).toSet
  }
}