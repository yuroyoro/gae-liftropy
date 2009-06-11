package com.yuroyoro.liftropy.model

import scala.Math._
import javax.persistence._
import com.google.appengine.api.datastore.Key
import org.hibernate.annotations.Type


@Entity
class Counter{
  @Id
  @GeneratedValue(){val strategy = GenerationType.IDENTITY}
  var id : Key = _

  @Column{val nullable = true}
  var count: Long = 1
}

object Counter{
  val counter_size = 10

  def findCounter():Option[Counter] ={
    return  Model.createQuery[Counter]("select from com.yuroyoro.liftropy.model.Counter").findOne
  }

  def count():Long = findCounter() match {
    case Some(c) => c.count
    case None    => 0
  }

  def incr():Unit = {
    var counter = findCounter()
    counter match {
      case Some(c) => c.count = c.count + 1;Model.mergeAndFlush(c)
      case None => Model.mergeAndFlush(new Counter())
    }
  }

  def getRandomTropyId : Long ={
    val rand = new Random()
    findCounter() match {
      case Some(c) => abs(rand.nextLong % c.count ) + 1
      case None    => 0
    }
  }
}

