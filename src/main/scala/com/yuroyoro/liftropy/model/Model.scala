package com.yuroyoro.liftropy.model

import javax.persistence.Persistence

import _root_.org.scala_libs.jpa.LocalEMF
import _root_.net.liftweb.jpa.RequestVarEM

object Model extends LocalEMF("transactions-optional") with RequestVarEM
