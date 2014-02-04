package com.vngrs.json.test

import com.vngrs.json._
import com.vngrs.json.DefaultJsonProtocols._

import collection.mutable.Stack
import org.scalatest._
import java.io.{ StringWriter, StringReader }
import scala.util.Try
import Implicits._

class SimpleFormatSpec extends FlatSpec with Matchers {
  
  val s = Simple(1, 3.14, 12345L, "simple", true)
  val sString = """{"i":1,"d":3.14,"l":12345,"s":"simple","b":true}"""

  "JsonReader" should "produce correct case class from json string" in {
    val jReader = new JsonReader( new StringReader( sString))
    assert( Option(s) === jReader.getAs[Simple])
  }

  "JsonWriter" should "produce correct json string from case class" in {
    val sWriter = new StringWriter()
    val jWriter = new JsonWriter( sWriter)
    jWriter.write(s)
    val json = sWriter.toString
    assert(sString === json)
  }
}



class ComplexFormatSpec extends FlatSpec with Matchers {

  val s = Simple(1, 3.14, 12345L, "simple", true)
  val sString = """{"i":1,"d":3.14,"l":12345,"s":"simple","b":true}"""

  val complexFull = Complex(
    i = 1,
    s = s,
    sopt = Some(s),
    lInt = Vector(1,2),
    lSimple = Vector(s),
    lOptSimple = Vector(Some(s)),
    optListSimple = Some(Vector(s))
  )

  val complexPartial = Complex(
    i = 1,
    s = s,
    sopt = None,
    lInt = Vector.empty[Int],
    lSimple = Vector.empty[Simple],
    lOptSimple = Vector.empty[Option[Simple]],
    optListSimple = None
)

  "JsonFormat" should "produce correct json string from complex case class" in {
    val sWriter = new StringWriter()
    val jWriter = new JsonWriter( sWriter)
    jWriter.write(complexFull)
    val json = sWriter.toString
    val cString = """{"i":1,"s":{"i":1,"d":3.14,"l":12345,"s":"simple","b":true},"sopt":{"i":1,"d":3.14,"l":12345,"s":"simple","b":true},"lInt":[1,2],"lSimple":[{"i":1,"d":3.14,"l":12345,"s":"simple","b":true}],"lOptSimple":[{"i":1,"d":3.14,"l":12345,"s":"simple","b":true}],"optListSimple":[{"i":1,"d":3.14,"l":12345,"s":"simple","b":true}]}"""
    assert(cString === json)
    val jReader = new JsonReader( new StringReader ( cString))
    jReader.setLenient(true)
    assert( Try(complexFull) === jReader.getAsTry[Complex])
    
    
  }
  
  "JsonWriter" should "produce correct json string from partial complex case class" in {
    val sWriter = new StringWriter()
    val jWriter = new JsonWriter( sWriter)
    jWriter.write(complexPartial)
    val json = sWriter.toString
    val cString = """{"i":1,"s":{"i":1,"d":3.14,"l":12345,"s":"simple","b":true},"lInt":[],"lSimple":[],"lOptSimple":[]}"""
    assert(cString === json)
    val jReader = new JsonReader( new StringReader ( cString))
    jReader.setLenient(true)
    assert( Try(complexPartial) === jReader.getAsTry[Complex])
  }

  "JsonFormat" should "work for top level Lists as well" in {
    val s1 = Simple(1, 3.14, 12345L, "simple1", true)
    val s2 = Simple(2, 3.14, 12345L, "simple2", true)
    val sString = """[{"i":1,"d":3.14,"l":12345,"s":"simple1","b":true},{"i":2,"d":3.14,"l":12345,"s":"simple2","b":true}]"""
    val sWriter = new StringWriter()
    val jWriter = new JsonWriter( sWriter)
    jWriter.write(Vector(s1, s2))
    val json = sWriter.toString
    assert(sString === json )

    val jReader = new JsonReader( new StringReader ( sString))
    assert( Try(Vector(s1, s2)) === jReader.getAsTry[Vector[Simple]])
    
  }



}
