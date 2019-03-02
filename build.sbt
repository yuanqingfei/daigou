scalaVersion in Global := "2.12.8"
lazy val js = project
val indexHtml = taskKey[File]("Generate an index.html that follows Application Specification")

indexHtml := {
  val linkedJs = (scalaJSLinkedFile in js in Compile).value.asInstanceOf[org.scalajs.core.tools.io.FileVirtualJSFile]
  val document = <html lang="en" data-framework="binding-scala">
    <head>
      <meta charset="UTF-8" />
      <title>Binding.scala Deposit Calculator</title>
      <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons"/>
      <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.98.2/css/materialize.min.css" />
    </head>
    <body>
      <header>
        <nav role="navigation">
          <div class="nav-wrapper container">
            <a href="#" class="brand-logo">订单维护系统</a>
          </div>
        </nav>
      </header>
      <main class="container">
        <div id="application-container"></div>
      </main>
      <footer class="page-footer">
        <div class="container white-text">
          For Test
        </div>
        <div class="footer-copyright">
          <div class="container">
            Copyright &copy; 2018 <a href="https://yuanqingfei.me" class="deep-purple-text text-lighten-4">Yuanqingfei</a>
          </div>
        </div>
      </footer>
      <script type="text/javascript" src={ linkedJs.file.relativeTo(baseDirectory.value).get.toString }></script>
      <script type="text/javascript"> Main.main(document.getElementById('application-container')) </script>
    </body>
  </html>
  val outputFile = baseDirectory.value / "index.html"
  IO.writeLines(outputFile, Seq("<!DOCTYPE html>", xml.Xhtml.toXhtml(document)))
  outputFile
}