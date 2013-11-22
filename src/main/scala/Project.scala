package whatnext

//TODO handle edge cases (see comments)
//TODO proper tests
//TODO improve whatNext interface
//TODO uncomment edit when implemented in Task
//TODO visualization

class Project(val tasks: Map[String, Task] = Map()) {
    
  def get(key: String): Option[Task] = {
    if (tasks contains key) Some(tasks(key))
    else None
  }
  
  def + (task: Task): Project = { // warn user if task's parents aren't in tasks
    new Project(tasks + ((task.title, task)))
  }
  
  def whatNext(): Set[String] = { // tell user if there aren't any more tasks
    val nextTasks = tasks filter {case (title, task) => task.parents.isEmpty}
    nextTasks map {case (title, task) => title} toSet
  }
  
/*  
  def edit(task: Task, change: Map[String, Any]): Project = {
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
    //remove this task
    val checkedOff = tasks - title
    
    //remove old children from tasks
    val children = tasks filter {case (name, task) => task hasParent title}
    val tasksMinusChildren = checkedOff -- (children map {case (name, child) => name})
    
    //add new children to tasks
    val newChildren = for ((name, child) <- children) yield (name, child.removeParent(title))
    val newTasks = tasksMinusChildren ++ newChildren
    
    new Project(newTasks)

  }
  
  def showAll(): Set[Task] = {
    (for ((title, task) <- tasks) yield task).toSet
  }
}