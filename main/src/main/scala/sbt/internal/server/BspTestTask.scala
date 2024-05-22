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
import sbt.protocol.testing.TestResult
import sbt.protocol.testing.TestResult.{ Passed, Failed, Error }
import sjsonnew.support.scalajson.unsafe.Converter

object BspTestTask {
  def start(
      targetId: BuildTargetIdentifier,
      project: ProjectRef,
      config: Configuration
  ): BspTestTask = {
    val taskId = TaskId(BuildServerTasks.uniqueId, Vector())
    val targetName = BuildTargetName.fromScope(project.project, config.name)
    val task = BspTestTask(targetId, targetName, taskId, System.currentTimeMillis())
    task.notifyStart()
    task
  }
}

case class BspTestTask private (
    targetId: BuildTargetIdentifier,
    targetName: String,
    id: sbt.internal.bsp.TaskId,
    startTimeMillis: Long
) {
  import sbt.internal.bsp.codec.JsonProtocol._

  private[sbt] def notifyStart(): Unit = {
    val message = s"Testing $targetName"
    val data = Converter.toJsonUnsafe(TestStart(targetName, None))
    val params = TaskStartParams(id, startTimeMillis, message, "test-start", data)
    StandardMain.exchange.notifyEvent("build/taskStart", params)
  }

  private[sbt] def notifyFinish(
      testResult: TestResult,
      testMessage: String,
      targetMess: String
  ): Unit = {
    // TODO: Report meaningful test status
    val message = "XYZ" + testMessage
    // TODO: Report test status
    val testStatus = testResultToCode(testResult)
    val data =
      Converter.toJsonUnsafe(TestFinish(targetMess, Some(message), testStatus, None, None, None))
    val params = TaskFinishParams(
      id,
      System.currentTimeMillis(),
      s"Finished testing $targetName",
      TestStatusCode.Passed,
      "test-finish",
      data
    )
    StandardMain.exchange.notifyEvent("build/taskFinish", params)
  }

  private def testResultToCode(testResult: TestResult): Int = {
    testResult match {
      case Passed => 1
      case Failed => 2
      case Error  => 3
    }
  }
}
