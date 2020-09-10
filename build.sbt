// *****************************************************************************
// Projects
// *****************************************************************************

lazy val `finch-zio-slick` =
  project
    .in(file("."))
    .enablePlugins(AutomateHeaderPlugin)
    .settings(commonSettings)
    .settings(
      libraryDependencies ++= Seq(
          library.circeGeneric,
          library.dupinCore,
          library.finchCore,
          library.finchCirce,
          library.flyway,
          library.postgresql,
          library.slick,
          library.slickHikari,
          library.zio,
          library.zioInteropCats,
          library.scalaCheck % Test,
          library.scalaTest  % Test
        )
    )

// *****************************************************************************
// Library dependencies
// *****************************************************************************

lazy val library =
  new {
    object Version {
      val circe          = "0.13.0"
      val dupin          = "0.1.1"
      val finch          = "0.32.1"
      val flyway         = "6.5.2"
      val postgresql     = "42.2.16.jre7"
      val slick          = "3.3.3"
      val zio            = "1.0.1"
      val zioInteropCats = "2.1.4.0"
      val scalaCheck     = "1.14.3"
      val scalaTest      = "3.1.0"
    }
    val circeGeneric   = "io.circe"           %% "circe-generic"    % Version.circe
    val dupinCore      = "com.github.yakivy"  %% "dupin-core"       % Version.dupin
    val finchCore      = "com.github.finagle" %% "finchx-core"      % Version.finch
    val finchCirce     = "com.github.finagle" %% "finchx-circe"     % Version.finch
    val flyway         = "org.flywaydb"        % "flyway-core"      % Version.flyway
    val postgresql     = "org.postgresql"      % "postgresql"       % Version.postgresql
    val slick          = "com.typesafe.slick" %% "slick"            % Version.slick
    val slickHikari    = "com.typesafe.slick" %% "slick-hikaricp"   % Version.slick
    val zio            = "dev.zio"            %% "zio"              % Version.zio
    val zioInteropCats = "dev.zio"            %% "zio-interop-cats" % Version.zioInteropCats
    val scalaCheck     = "org.scalacheck"     %% "scalacheck"       % Version.scalaCheck
    val scalaTest      = "org.scalatest"      %% "scalatest"        % Version.scalaTest
  }

// *****************************************************************************
// Settings
// *****************************************************************************

lazy val commonSettings =
  Seq(
    scalaVersion := "2.13.3",
    organization := "codeeng",
    organizationName := "Branislav Lazic",
    startYear := Some(2020),
    licenses += ("MIT", url("https://opensource.org/licenses/MIT")),
    scalacOptions ++= Seq(
        "-unchecked",
        "-deprecation",
        "-language:_",
        "-target:jvm-11",
        "-encoding",
        "UTF-8",
        "-Ywarn-unused:imports"
      ),
    testFrameworks += new TestFramework("munit.Framework"),
    scalafmtOnCompile := true
  )
