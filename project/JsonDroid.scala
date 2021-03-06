import sbt._
import sbt.Keys._

object BuildSettings {
  val buildVersion = "0.1-SNAPSHOT"

  val filter = { (ms: Seq[(File, String)]) =>
    ms filter {
      case (file, path) =>
        path != "logback.xml" && !path.startsWith("toignore") && !path.startsWith("samples")
    }
  }

  val buildSettings = Defaults.defaultSettings ++ Seq(
    organization := "com.vngrs.jsondroid",
    version := buildVersion,
    scalaVersion := "2.10.3",
    crossScalaVersions := Seq("2.10.3"),
    crossVersion := CrossVersion.binary,
    javaOptions in test ++= Seq("-Xmx512m", "-XX:MaxPermSize=512m"),
    scalacOptions ++= Seq("-unchecked", "-deprecation"),
    scalacOptions in (Compile, doc) ++= Seq("-unchecked", "-deprecation", "-diagrams", "-implicits", "-skip-packages", "samples"),
    scalacOptions in (Compile, doc) ++= Opts.doc.title("JsonDroid API"),
    scalacOptions in (Compile, doc) ++= Opts.doc.version(buildVersion),
    shellPrompt := ShellPrompt.buildShellPrompt,
    mappings in (Compile, packageBin) ~= filter,
    mappings in (Compile, packageSrc) ~= filter,
    mappings in (Compile, packageDoc) ~= filter) ++ Publish.settings//   ++ Format.settings
}

object Format {
  import com.typesafe.sbt.SbtScalariform._

  lazy val settings = scalariformSettings ++ Seq(
    ScalariformKeys.preferences := formattingPreferences)

  lazy val formattingPreferences = {
    import scalariform.formatter.preferences._
    FormattingPreferences().
      setPreference(AlignParameters, true).
      setPreference(AlignSingleLineCaseStatements, true).
      setPreference(CompactControlReadability, false).
      setPreference(CompactStringConcatenation, false).
      setPreference(DoubleIndentClassDeclaration, true).
      setPreference(FormatXml, true).
      setPreference(IndentLocalDefs, false).
      setPreference(IndentPackageBlocks, true).
      setPreference(IndentSpaces, 2).
      setPreference(MultilineScaladocCommentsStartOnFirstLine, false).
      setPreference(PreserveSpaceBeforeArguments, false).
      setPreference(PreserveDanglingCloseParenthesis, false).
      setPreference(RewriteArrowSymbols, false).
      setPreference(SpaceBeforeColon, false).
      setPreference(SpaceInsideBrackets, false).
      setPreference(SpacesWithinPatternBinders, true)
  }
}


object Publish {
  def targetRepository: Project.Initialize[Option[sbt.Resolver]] = version { (version: String) =>
    val nexus = "https://oss.sonatype.org/"
    if (version.trim.endsWith("SNAPSHOT"))
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases" at nexus + "service/local/staging/deploy/maven2")
   }
  lazy val settings = Seq(
    publishMavenStyle := true,
    publishTo <<= targetRepository,
    publishArtifact in Test := false,
    pomIncludeRepository := { _ => false },
    licenses := Seq("LGPLv3" -> url("http://www.gnu.org/licenses/lgpl-3.0.html")),
    homepage := Some(url("https://github.com/vngrs/jsonDroid")),
    pomExtra := (
      <scm>
        <url>git://github.com/vngrs/jsonDroid.git</url>
        <connection>scm:git://github.com/vngrs/jsonDroid.git</connection>
      </scm>
      <developers>
        <developer>
          <id>csenol</id>
          <name>Cagdas Senol</name>
          <url>>https://github.com/csenol</url>
        </developer>
      </developers>))
}

// Shell prompt which show the current project,
// git branch and build version
object ShellPrompt {
  object devnull extends ProcessLogger {
    def info(s: => String) {}

    def error(s: => String) {}

    def buffer[T](f: => T): T = f
  }

  def currBranch = (
    ("git status -sb" lines_! devnull headOption)
    getOrElse "-" stripPrefix "## ")

  val buildShellPrompt = {
    (state: State) =>
      {
        val currProject = Project.extract(state).currentProject.id
        "%s:%s:%s> ".format(
          currProject, currBranch, BuildSettings.buildVersion)
      }
  }
}

object Resolvers {
  val typesafe = Seq(
    "Typesafe repository snapshots" at "http://repo.typesafe.com/typesafe/snapshots/",
    "Typesafe repository releases" at "http://repo.typesafe.com/typesafe/releases/")
  val resolversList = typesafe
}

object Dependencies {
  val gson = "com.google.code.gson" % "gson" % "2.2.4"
  val scalaTest = "org.scalatest" % "scalatest_2.10" % "2.0" % "test"
}

object JsonDroidBuild extends Build {
  import BuildSettings._
  import Resolvers._
  import Dependencies._
  import sbtunidoc.{ Plugin => UnidocPlugin }

  lazy val jsonDroid =
    Project(
      "root",
      file("."),
      settings = buildSettings ++ (publishArtifact := false) ).
    settings(UnidocPlugin.unidocSettings: _*).
    aggregate(json).
    settings( ScoverageSbtPlugin.instrumentSettings: _*).
    settings( CoverallsPlugin.coverallsSettings: _*) 

  lazy val json = Project(
    "json",
    file("json"),
    settings = buildSettings ++ Seq( libraryDependencies ++= Seq(gson, scalaTest))).
    settings( ScoverageSbtPlugin.instrumentSettings: _*).
    settings( CoverallsPlugin.coverallsSettings: _*)
  
}

