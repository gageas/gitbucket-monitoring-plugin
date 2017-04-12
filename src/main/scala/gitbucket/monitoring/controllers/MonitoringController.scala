package gitbucket.monitoring.controllers

import io.github.gitbucket.scalatra.forms._
import gitbucket.core.controller.ControllerBase
import gitbucket.core.util.AdminAuthenticator
import gitbucket.core.view.helpers._
import gitbucket.monitoring.html
import java.net._
import java.io.File

import scala.collection.JavaConversions._
import gitbucket.monitoring.html._
import gitbucket.monitoring.models.{MachineResources, _}

class MonitoringController extends ControllerBase with AdminAuthenticator {

  get("/admin/monitoring")(adminOnly {
    redirect(s"/admin/monitoring/systeminformation");
  })

  get("/admin/monitoring/systeminformation")(adminOnly {
    html.systeminformation(new SystemInformation(System.getProperty("os.name"), System.getProperty("os.version"), System.getProperty("os.arch")));
  })

  get("/admin/monitoring/jvm")(adminOnly {
    val memTotal = (Runtime.getRuntime().totalMemory() / (1024 * 1024))
    val memFree = (Runtime.getRuntime().freeMemory() / (1024 * 1024))
    val memUsed = memTotal - memFree
    val memMax = (Runtime.getRuntime().maxMemory() / (1024 * 1024))
    html.jvm(new JVM(System.getProperty("java.vm.name"), System.getProperty("java.runtime.version"), memTotal, memFree, memUsed, memMax));
  })

  get("/admin/monitoring/machineresources")(adminOnly {
    html.machineresources(new MachineResources(Runtime.getRuntime().availableProcessors()));
  })
}