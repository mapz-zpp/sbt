/* sbt -- Simple Build Tool
 * Copyright 2011  Mark Harrah
 */
package sbt

	import Project.{ScopedKey, ThisProject}
	import CommandSupport.logger
	import Load.BuildStructure
	import complete.{DefaultParsers, Parser}
	import DefaultParsers._
	import java.net.URI

object Act
{
	// this does not take delegation into account
	def scopedKey(index: KeyIndex, currentBuild: URI, currentProject: String, defaultConfig: ProjectRef => Option[String], keyMap: Map[String, AttributeKey[_]]): Parser[ScopedKey[_]] =
	{
		for {
			proj <- optProjectRef(index, currentBuild, currentProject)
			confAmb <- config( index configs proj )
			(key, conf) <- key(index, proj, configs(confAmb, defaultConfig, proj), keyMap) }
		yield
			ScopedKey( Scope( Select(proj), toAxis(conf map ConfigKey.apply, Global), Global, Global), key )
	}
	def examplesStrict(p: Parser[String], exs: Set[String]): Parser[String] =
		p examples exs filter exs

	def toAxis[T](opt: Option[T], ifNone: ScopeAxis[T]): ScopeAxis[T] =
		opt match { case Some(t) => Select(t); case None => ifNone }
	def defaultConfig(data: Settings[Scope])(project: ProjectRef): Option[String] =
		ThisProject in project get data flatMap( _.configurations.headOption.map(_.name))

	def config(confs: Set[String]): Parser[Option[String]] =
		token( examplesStrict(ID, confs) <~ ':' ).?

	def configs(explicit: Option[String], defaultConfig: ProjectRef => Option[String], proj: ProjectRef): List[Option[String]] =
		if(explicit.isDefined) explicit :: Nil else None :: defaultConfig(proj) :: Nil
	def key(index: KeyIndex, proj: ProjectRef, confs: Seq[Option[String]], keyMap: Map[String,AttributeKey[_]]): Parser[(AttributeKey[_], Option[String])] =
	{
		val confMap = confs map { conf => (conf, index.keys(proj, conf)) } toMap;
		val allKeys = (Set.empty[String] /: confMap.values)(_ ++ _)
		val filteredKeys = allKeys.filter(Command.validID)
		token(ID examples filteredKeys).flatMap { keyString =>
			val conf = confMap.flatMap { case (key, value) => if(value contains keyString) key :: Nil else Nil } headOption;
			getKey(keyMap, keyString, k => (k, conf.flatMap(identity)))
		}
	}
	def getKey[T](keyMap: Map[String,AttributeKey[_]], keyString: String, f: AttributeKey[_] => T): Parser[T] =
		keyMap.get(keyString) match {
			case Some(k) => success(f(k))
			case None => failure("Invalid key: " + keyString)
		}

	def projectRef(index: KeyIndex, currentBuild: URI): Parser[ProjectRef] =
	{
		val uris = index.buildURIs
		val build = token( '(' ~> Uri(uris).map(uri => Scope.resolveBuild(currentBuild, uri)) <~ ')') ?? currentBuild
		def projectID(uri: URI) = token( examplesStrict(ID, index projects uri) <~ '/' )

		for(uri <- build; id <- projectID(uri)) yield
			ProjectRef(Some(uri), Some(id))
	}
	def optProjectRef(index: KeyIndex, currentBuild: URI, currentProject: String) =
		projectRef(index, currentBuild) ?? ProjectRef(Some(currentBuild), Some(currentProject))

	def actParser(s: State): Parser[() => State] = requireSession(s, actParser0(s))

	private[this] def actParser0(state: State) =
	{
		val extracted = Project extract state
		showParser.flatMap { show =>
			scopedKeyParser(extracted) flatMap Aggregation.valueParser(state, extracted.structure, show)
		}
	}
	def showParser = token( ("show" ~ Space) ^^^ true) ?? false
	def scopedKeyParser(state: State): Parser[ScopedKey[_]] = scopedKeyParser(Project extract state)
	def scopedKeyParser(extracted: Extracted): Parser[ScopedKey[_]] =
	{
		import extracted._
		val defaultConf = (ref: ProjectRef) => if(Project.getProject(ref, structure).isDefined) defaultConfig(structure.data)(ref) else None
		scopedKey(structure.index.keyIndex, curi, cid, defaultConf, structure.index.keyMap)
	}


	def requireSession[T](s: State, p: => Parser[T]): Parser[T] =
		if(s get Project.SessionKey isEmpty) failure("No project loaded") else p
}