import sbt._
import sbt.Keys._
import ohnosequences.sbt.SbtS3Resolver._
object DwnldSettings {
  lazy val noDoc = Seq(publishArtifact in (LocalProject("network-common"), Compile, packageDoc) := false)
  lazy val publishSettings = Seq(
    publishMavenStyle := false,
    resolvers += {
      val prefix = if (isSnapshot.value) "snapshots" else "releases"
      toSbtResolver(s3resolver.value(s"$prefix s3 bucket", s3(prefix+".mvn-repo.dwnld.me")) withIvyPatterns)
    },
    publishTo := {
      val prefix = if (isSnapshot.value) "snapshots" else "releases"
      Some(s3resolver.value(s"$prefix s3 bucket", s3(prefix+".mvn-repo.dwnld.me")) withIvyPatterns)
    }
  )

  lazy val settings = noDoc ++ S3Resolver.defaults ++ publishSettings
}
