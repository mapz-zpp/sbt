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
  TestTask,
  TestStart,
  TestStatusCode,
  TestReport
}
import sbt.testing.Status
import sbt.protocol.testing.TestResult
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

  private var testStatusCountMap: Map[Status, Int] = Map.empty

  private[sbt] def notifyTestGroupStart(): Unit = {
    val message = s"Testing $targetName"
    val data = Converter.toJsonUnsafe(TestTask(targetId))
    val params = TaskStartParams(id, startTimeMillis, message, "test-task", data, originId.orNull)
    StandardMain.exchange.notifyEvent("build/taskStart", params)
  }

  private[sbt] def notifySingleTestStart(displayName: String): Unit = {
    val message = s"Testing $targetName"
    val data = Converter.toJsonUnsafe(
      TestStart(displayName, None)
    )
    val params = TaskStartParams(id, startTimeMillis, message, "test-start", data, originId.orNull)
    StandardMain.exchange.notifyEvent("build/taskStart", params)
  }

  private[sbt] def notifyFinish(status: Status, displayName: String): Unit = {
    val message = s"Finished testing $displayName"
    val statusCode = statusToCode(status)
    val data =
      Converter.toJsonUnsafe(TestFinish(displayName, Some(message), statusCode, None, None, None))
    val currentCount = testStatusCountMap.getOrElse(status, 0)
    testStatusCountMap = testStatusCountMap + (status -> (currentCount + 1))
    val params = TaskFinishParams(
      id,
      originId.orNull,
      System.currentTimeMillis(),
      s"Finished testing $displayName",
      statusCode,
      "test-finish",
      data
    )
    StandardMain.exchange.notifyEvent("build/taskFinish", params)
  }

  private[sbt] def statusToCode(status: Status) = {
    status match {
      case Status.Success  => TestStatusCode.Passed
      case Status.Error    => TestStatusCode.Failed
      case Status.Skipped  => TestStatusCode.Skipped
      case Status.Failure  => TestStatusCode.Failed
      case Status.Ignored  => TestStatusCode.Ignored
      case Status.Canceled => TestStatusCode.Cancelled
      case Status.Pending  => TestStatusCode.Ignored
    }
  }
  private[sbt] def resultToCode(result: TestResult) = {
    result match {
      case TestResult.Passed => TestStatusCode.Passed
      case TestResult.Error  => TestStatusCode.Failed
      case TestResult.Failed => TestStatusCode.Failed
    }
  }

  private[sbt] def notifyReport(testResult: TestResult): Unit = {
    val data = Converter.toJsonUnsafe(
      TestReport(
        originId.orNull,
        targetId,
        testStatusCountMap.getOrElse(Status.Success, 0),
        testStatusCountMap.getOrElse(Status.Failure, 0) + testStatusCountMap
          .getOrElse(Status.Error, 0),
        testStatusCountMap.getOrElse(Status.Ignored, 0),
        testStatusCountMap.getOrElse(Status.Canceled, 0),
        testStatusCountMap.getOrElse(Status.Skipped, 0),
        (System.currentTimeMillis() - startTimeMillis).toInt
      )
    )
    val params = TaskFinishParams(
      id,
      originId.orNull,
      System.currentTimeMillis(),
      s"Finished testing $targetName",
      resultToCode(testResult),
      "test-report",
      data
    )
    StandardMain.exchange.notifyEvent("build/taskFinish", params)
  }
}
