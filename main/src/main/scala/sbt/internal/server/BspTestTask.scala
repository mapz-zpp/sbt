package sbt.internal.server

import sbt.librarymanagement.Configuration
import sbt.{ ProjectRef, StandardMain }
import sbt.internal.bsp.{
  BuildServerTasks,
  BuildTargetIdentifier,
  BuildTargetName,
  TaskFinishParams,
  TaskId,
  TaskStartParams,
  TestFinish,
  TestStart,
  TestStatusCode
}
import sjsonnew.support.scalajson.unsafe.Converter

object BspTestTask {
  def start(
      targetId: BuildTargetIdentifier,
      project: ProjectRef,
      config: Configuration,
      originId: Option[String]
  ): BspTestTask = {
    val taskId = TaskId(BuildServerTasks.uniqueId, Vector())
    val targetName = BuildTargetName.fromScope(project.project, config.name)
    val task = BspTestTask(targetId, targetName, taskId, System.currentTimeMillis(), originId)
    task.notifyStart();
    task
  }
}

case class BspTestTask private (
    targetId: BuildTargetIdentifier,
    targetName: String,
    id: sbt.internal.bsp.TaskId,
    startTimeMillis: Long,
    originId: Option[String]
) {
  import sbt.internal.bsp.codec.JsonProtocol._

  private[sbt] def notifyStart(): Unit = {
    val message = s"Testing $targetName"
    val data = Converter.toJsonUnsafe(TestStart(targetName, None))
    val params = TaskStartParams(id, startTimeMillis, message, "test-start", data, originId.orNull)
    StandardMain.exchange.notifyEvent("build/taskStart", params)
  }

  private[sbt] def notifyFinish(tmp: Any): Unit = {
    // TODO: Report meaningful test status
    val message = s"21DUPAXYZ $targetName" + tmp
    // TODO: Report test status
    val data = Converter.toJsonUnsafe(TestFinish(targetName, Some(message), 1, None, None, None))
    val params = TaskFinishParams(
      id,
      originId.orNull,
      System.currentTimeMillis(),
      s"Finished testing $targetName",
      TestStatusCode.Passed,
      "test-finish",
      data
    )
    StandardMain.exchange.notifyEvent("build/taskFinish", params)
  }
}
