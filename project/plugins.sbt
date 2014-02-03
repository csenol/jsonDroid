addSbtPlugin("com.typesafe.sbt" % "sbt-scalariform" % "1.2.1")

resolvers += "jgit-repo" at "http://download.eclipse.org/jgit/maven"

resolvers ++= Seq(
    Classpaths.typesafeResolver,
    "scct-github-repository" at "http://mtkopone.github.com/scct/maven-repo"
)

addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "0.6.2")

addSbtPlugin("com.eed3si9n" % "sbt-unidoc" % "0.3.0")

addSbtPlugin("com.sqality.scct" % "sbt-scct" % "0.3")

addSbtPlugin("com.sksamuel.scoverage" % "sbt-scoverage" % "0.95.1")

addSbtPlugin("com.sksamuel.scoverage" %% "sbt-coveralls" % "0.0.5")

//addSbtPlugin("com.github.theon" %% "xsbt-coveralls-plugin" % "0.0.4")

