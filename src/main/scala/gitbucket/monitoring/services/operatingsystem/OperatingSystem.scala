package gitbucket.monitoring.services.operatingsystem

import scala.sys.process._
import gitbucket.monitoring.utils.Message

object OperatingSystem {
  sealed abstract class OSType
  case object Linux extends OSType
  case object Windows extends OSType
  case object Mac extends OSType
  case object Other extends OSType

  val osArch = System.getProperty("os.arch")
  val osName = System.getProperty("os.name")

  val osType: OSType =
    if (osName.toLowerCase.contains("linux")) Linux
    else if (osName.toLowerCase.contains("windows")) Windows
    else if (osName.toLowerCase.contains("mac")) Mac
    else Other

  val osVersion:String = osType match {
    case Windows => {
      (Process("powershell -Command Get-WmiObject Win32_OperatingSystem | %{ $_.Version }") !!).toString
    }
    case _ => {
      System.getProperty("os.version")
    }
  }

  val distribution: String = osType match {
    case Linux => {
      try {
        val result = Process("cat /etc/issue") !!
        val d = result.replace("\\n","").replace("\\l","").replace(" ","")
        d
      } catch {
        case e: Exception => Message.error
      }
    }
    case _ => {
      "-"
    }
  }

  val getInstance = osType match {
    case OperatingSystem.Linux => new Linux
    case OperatingSystem.Mac => new Mac
    case OperatingSystem.Windows => new Windows
    case _ => new Other
  }
}