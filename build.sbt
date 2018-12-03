lazy val root = project
  .in(file("."))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    inThisBuild(List(
      organization := "me.yuanqingfei",
      version      := "1.0.0",
      scalaVersion := "2.12.7"
    )),
    name := "daigou",
    libraryDependencies ++= Seq(
        "org.scala-js"             %%% "scalajs-dom" % "0.9.6",
        "com.thoughtworks.binding" %%% "dom"         % "11.3.0",
        "com.thoughtworks.binding" %%% "binding"     % "11.3.0"
        // "org.scala-js"             %%% "scalajs-dom" % "0.9.2",
        // "com.thoughtworks.binding" %%% "dom"         % "10.0.2",
        // "com.thoughtworks.binding" %%% "binding"     % "10.0.2"
    ),    
    addCompilerPlugin(
      "org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full
    ),
    scalaJSUseMainModuleInitializer := false
  )


// Automatically generate index-dev.html which uses *-fastopt.js
resourceGenerators in Compile += Def.task {
  val source = (resourceDirectory in Compile).value / "index.html"
  val target = (resourceManaged in Compile).value / "index-dev.html"

  val fullFileName = (artifactPath in (Compile, fullOptJS)).value.getName
  val fastFileName = (artifactPath in (Compile, fastOptJS)).value.getName

  IO.writeLines(target,
    IO.readLines(source).map {
      line => line.replace(fullFileName, fastFileName)
    }
  )

  Seq(target)
}.taskValue