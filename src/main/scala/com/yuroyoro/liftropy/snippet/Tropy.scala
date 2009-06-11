package com.yuroyoro.liftropy.snippet

import scala.xml.{NodeSeq,Text,Group}

import net.liftweb.http.{RequestVar, S, SHtml}
import net.liftweb.util.{Helpers, Log}
import S._
import Helpers._

import net.liftweb.http._
import net.liftweb.util._
import com.yuroyoro.liftropy.model._

class TropyOp {

  object selectedEntry extends RequestVar(new Tropy() )
  def tropy = selectedEntry.is

  def create(xhtml:Group) :NodeSeq = {
    bind("tropy", xhtml ,
      "id"     -> SHtml.hidden( ()=> selectedEntry(tropy) ),
      "entry"  -> SHtml.textarea(tropy.entry, tropy.entry=_, "cols" -> "80", "rows" -> "8"),
      "submit" -> SHtml.submit(?("Save"), ()=>saveEntry(tropy))
    )
  }

  def edit(xhtml:Group) :NodeSeq = {
    val current = tropy
    bind("tropy", xhtml ,
      "id"     -> SHtml.hidden( ()=> selectedEntry(current) ),
      "entry"  -> SHtml.textarea(tropy.entry, tropy.entry=_, "cols" -> "80", "rows" -> "8"),
      "submit" -> SHtml.submit(?("Save"), ()=>saveEntry(current))
    )
  }

  def saveEntry( t:Tropy) = t.validate match {
    case None    => {
      if( t.id == null ){
        Counter.incr()
        t.tropyId = Counter.count()
      }
      Model.mergeAndFlush(t)
      redirectTo("/show/" + t.tropyId)
    }
    case Some(x) => error(x);selectedEntry(t)
  }

  def show(xhtml:Group) :NodeSeq = S.param("tropyId") match {
    case Empty => Counter.getRandomTropyId match{
      case 0 => redirectTo("/create")
      case tropyId => redirectTo("/show/" + tropyId)
    }
    case tropyId => Tropy.findByTropyId( tropyId.open_!.toLong) match{
      case None => redirectTo("/index")
      case Some(entry) => {
        bind("tropy", xhtml ,
          "id"    -> SHtml.hidden( ()=> {
            tropy.id = entry.id
            tropy.tropyId = entry.tropyId} ),
          "entry" -> Text(entry.entry ),
          "edit"  -> SHtml.link("/edit.html", () => selectedEntry(entry), Text(?("Edit")))
        )
      }
    }
  }
}
