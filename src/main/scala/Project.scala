package whatnext

//TODO main (think about interface)
//TODO proper tests
//TODO improve whatNext interface
//TODO uncomment edit when implemented in Task and add error checking for cycles
//TODO make task ready or not based on time, not just parents
//TODO visualization


class Project private (val tasks: Map[String, Task]) {
  // this constructor is private and doesn't check for cycles and orphans 
  // because it will always be called with a good graph structure
  // (if the methods in this class are written correctly)
    
  // this constructor checks for cycles and orphans
  def this(initialTasks: List[Task] = List()) = {
    this((GraphUtils.structureCheck(initialTasks) map {case task: Task => (task.getTitle, task)}).toMap)
  }

  def get(key: String): Option[Task] = {
    if (tasks contains key) Some(tasks(key))
    else None
  }
  
  def + (task: Task): Project = { // error if task's parents aren't in tasks (helps avoid unreachable nodes/cycles)
    if (task.parents.forall(parent => tasks contains parent)){
      new Project(tasks + ((task.title, task)))
    } else {
      throw ParentException("That task's prerequisites aren't in the project. Add them and then try again.")
    }

  }
  
  def whatNext(): Set[String] = {
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
  def checkOff(title: String): Project = { // error if task isn't there; should be caught by interface
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