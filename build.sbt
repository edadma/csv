ThisBuild / licenses += "ISC" -> url("https://opensource.org/licenses/ISC")
ThisBuild / versionScheme     := Some("semver-spec")

publish / skip := true

lazy val csv = crossProject(JSPlatform, JVMPlatform, NativePlatform)
  .in(file("."))
  .settings(
    name         := "csv",
    version      := "0.1.5",
    scalaVersion := "3.5.2",
    scalacOptions ++=
      Seq(
        "-deprecation",
        "-feature",
        "-unchecked",
        "-language:postfixOps",
        "-language:implicitConversions",
        "-language:existentials",
        "-language:dynamics",
      ),
    organization                               := "io.github.edadma",
    githubOwner                                := "edadma",
    githubRepository                           := name.value,
    libraryDependencies += "org.scalatest"    %%% "scalatest"      % "3.2.19" % "test",
    libraryDependencies += "io.github.edadma" %%% "char-reader"    % "0.1.16",
    libraryDependencies += "io.github.edadma" %%% "cross-platform" % "0.0.3",
    publishMavenStyle                          := true,
    Test / publishArtifact                     := false,
    licenses += "ISC"                          -> url("https://opensource.org/licenses/ISC"),
  )
  .jvmSettings(
    libraryDependencies += "org.scala-js" %% "scalajs-stubs" % "1.1.0" % "provided",
  )
  .nativeSettings(
  )
  .jsSettings(
    scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.CommonJSModule) },
    jsEnv := new org.scalajs.jsenv.nodejs.NodeJSEnv(),
    //    Test / scalaJSUseMainModuleInitializer := true,
    //    Test / scalaJSUseTestModuleInitializer := false,
    Test / scalaJSUseMainModuleInitializer := false,
    Test / scalaJSUseTestModuleInitializer := true,
    scalaJSUseMainModuleInitializer        := true,
  )
