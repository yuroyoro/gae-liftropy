package com.yuroyoro.liftropy.model

import java.util.Date
import javax.persistence._
import com.google.appengine.api.datastore.Key
import org.hibernate.annotations.Type


@Entity
class Tropy {
  @Id
  @GeneratedValue(){val strategy = GenerationType.IDENTITY}
  var id : java.lang.Long = _

  @Column{val nullable = false}
  var tropyId:Long = 0

  @Column{val nullable = false}
  var entry: String = ""

  @Column{val nullable = true}
  var published : Date = new Date()

  def validate():Option[String] = entry match {
    case "" => Some("entry must not be Nil")
    case _  => None
  }

}

object Tropy {
  def findByTropyId(id:Long ) = {
    Model.createQuery[Tropy]("select from com.yuroyoro.liftropy.model.Tropy a where a.tropyId = :id"  ).setParameter("id",  id).findOne
  }

}

