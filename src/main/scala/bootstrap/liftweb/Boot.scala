/*
 * Copyright 2007-2008 WorldWide Conferencing, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 */
package bootstrap.liftweb

import _root_.net.liftweb._
import util.{Helpers, Box, Full, Empty, Failure, Log, NamedPF, Props}
import http._
import sitemap._
import Helpers._

import com.yuroyoro.liftropy.model._

import _root_.net.liftweb.mapper.{DB, ConnectionManager, Schemifier, DefaultConnectionIdentifier, ConnectionIdentifier}

import _root_.java.sql.{Connection, DriverManager}
import _root_.javax.servlet.http.{HttpServlet, HttpServletRequest , HttpServletResponse, HttpSession}
import _root_.scala.actors._
import Actor._

/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {
  def boot {

    LiftRules.addToPackages("com.yuroyoro.liftropy")

    LiftRules.early.append(makeUtf8)

    import Loc._
    val entries = Menu(Loc("Random", List("index"), "Random")) ::
      Menu(Loc("create",List("create"),"create")) ::
      Menu(Loc("show",List("show"),"show")) ::
      Menu(Loc("edit",List("edit"),"edit")) :: Nil

    LiftRules.setSiteMap(SiteMap(entries:_*))

    LiftRules.rewrite.append {
      case RewriteRequest(ParsePath( List("index"),_ , _, _), _, _) =>
        RewriteResponse("show"::Nil)
      case RewriteRequest(ParsePath( List("show", tropyId), _ , _ ,_ ), _, _) =>
        RewriteResponse("show"::Nil , Map("tropyId" -> tropyId))
      case RewriteRequest(ParsePath( List("edit", tropyId), _ , _ ,_ ), _, _) =>
        RewriteResponse("edit"::Nil , Map("tropyId" -> tropyId))
    }

  }

  private def makeUtf8(req: HttpServletRequest): Unit = {req.setCharacterEncoding("UTF-8")}
}


