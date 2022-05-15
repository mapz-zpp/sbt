/*
 * sbt
 * Copyright 2011 - 2018, Lightbend, Inc.
 * Copyright 2008 - 2010, Mark Harrah
 * Licensed under Apache License 2.0 (see LICENSE)
 */

package sbt
package std

import Def.Initialize
import sbt.util.{ Applicative, Monad }
import sbt.internal.util.AList
import sbt.internal.util.Types.{ const, Id, Compose, idFun }
import sbt.internal.util.complete.{ DefaultParsers, Parser }

object InitializeInstance:
  given initializeMonad: Monad[Initialize] with
    type F[x] = Initialize[x]

    override def pure[A1](a: () => A1): Initialize[A1] = Def.pure(a)
    override def map[A1, A2](in: Initialize[A1])(f: A1 => A2): Initialize[A2] = Def.map(in)(f)
    override def ap[A1, A2](ff: Initialize[A1 => A2])(fa: Initialize[A1]): Initialize[A2] =
      Def.ap[A1, A2](ff)(fa)
    override def flatMap[A1, A2](fa: Initialize[A1])(f: A1 => Initialize[A2]) =
      Def.flatMap[A1, A2](fa)(f)
end InitializeInstance

private[std] object ComposeInstance:
  import InitializeInstance.initializeMonad
  val InitInstance = summon[Applicative[Initialize]]
  val F1F2: Applicative[Compose[Initialize, Task]] = summon[Applicative[Compose[Initialize, Task]]]
end ComposeInstance

object ParserInstance:
  // import sbt.internal.util.Classes.Applicative
  // private[this] implicit val parserApplicative: Applicative[M] = new Applicative[M] {
  //   def apply[S, T](f: M[S => T], v: M[S]): M[A1] = s => (f(s) ~ v(s)) map { case (a, b) => a(b) }
  //   def pure[S](s: => S) = const(Parser.success(s))
  //   def map[S, T](f: S => T, v: M[S]) = s => v(s).map(f)
  // }

  given Applicative[[a] =>> State => Parser[a]] with
    type F[x] = State => Parser[x]
    override def pure[A1](a: () => A1): State => Parser[A1] = const(DefaultParsers.success(a()))
    override def ap[A1, A2](ff: F[A1 => A2])(fa: F[A1]): F[A2] =
      (s: State) => (ff(s) ~ fa(s)).map { case (f, a) => f(a) }
    override def map[A1, A2](fa: F[A1])(f: A1 => A2) =
      (s: State) => fa(s).map(f)
end ParserInstance

/** Composes the Task and Initialize Instances to provide an Instance for [A1] Initialize[Task[A1]]. */
object FullInstance:
  type SS = sbt.internal.util.Settings[Scope]
  val settingsData = TaskKey[SS](
    "settings-data",
    "Provides access to the project data for the build.",
    KeyRanks.DTask
  )

  given Monad[Initialize] = InitializeInstance.initializeMonad
  val F1F2: Applicative[Compose[Initialize, Task]] = ComposeInstance.F1F2
  given Monad[Compose[Initialize, Task]] with
    type F[x] = Initialize[Task[x]]
    override def pure[A1](x: () => A1): Initialize[Task[A1]] = F1F2.pure(x)
    override def ap[A1, A2](ff: Initialize[Task[A1 => A2]])(
        fa: Initialize[Task[A1]]
    ): Initialize[Task[A2]] =
      F1F2.ap(ff)(fa)

    override def flatMap[A1, A2](fa: Initialize[Task[A1]])(
        f: A1 => Initialize[Task[A2]]
    ): Initialize[Task[A2]] =
      val nested: Initialize[Task[Initialize[Task[A2]]]] = F1F2.map(fa)(f)
      flatten(nested)

    override def flatten[A1](in: Initialize[Task[Initialize[Task[A1]]]]): Initialize[Task[A1]] =
      type K[L[x]] =
        AList.Tuple3K[Task[Initialize[Task[A1]]], Task[SS], [a] => Initialize[a] => Initialize[a]][
          L
        ]
      Def.app[K, Task[A1]]((in, settingsData, Def.capturedTransformations)) {
        case (a: Task[Initialize[Task[A1]]], data: Task[SS], f) =>
          import TaskExtra.multT2Task
          (a, data).flatMap { case (a, d) => f(a) evaluate d }
      }(AList.tuple3[Task[Initialize[Task[A1]]], Task[SS], [a] => Initialize[a] => Initialize[a]])

/*
  def flattenFun[S, T](
      in: Initialize[Task[S => Initialize[Task[A1]]]]
  ): Initialize[S => Task[A1]] = {
    type K[L[x]] =
      AList.T3K[Task[S => Initialize[Task[A1]]], Task[SS], Initialize ~> Initialize]#l[L]
    Def.app[K, S => Task[A1]]((in, settingsData, Def.capturedTransformations)) {
      case (a: Task[S => Initialize[Task[A1]]], data: Task[SS], f) => { (s: S) =>
        import TaskExtra.multT2Task
        (a, data) flatMap { case (af, d) => f(af(s)) evaluate d }
      }
    }(AList.tuple3)
  }
 */
end FullInstance
