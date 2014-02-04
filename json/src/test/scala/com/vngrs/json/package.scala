package com.vngrs.json

import com.vngrs.json._
import com.vngrs.json.DefaultJsonProtocols._


package object test {
  case class Simple(i: Int, d: Double, l: Long, s: String, b :Boolean)
  case class Complex(i: Int, s: Simple, sopt: Option[Simple], lInt: Vector[Int], 
                   lSimple: Vector[Simple], lOptSimple: Vector[Option[Simple]], optListSimple: Option[Vector[Simple]])

  object Implicits {

    implicit object SimpleFormat extends JsonFormat[Simple] {
      def read(in: JsonReader): Simple = {
        var defSimple = Simple(0 , 0.0 , 0L , "" , false)
        while(in.hasNext ){
          val name = in.nextName
          name match {
            case "i" => defSimple = defSimple.copy(i = in.getAs[Int].get)
            case "d" => defSimple = defSimple.copy(d = in.getAs[Double].get)
            case "l" => defSimple = defSimple.copy(l = in.getAs[Long].get)
            case "s" => defSimple = defSimple.copy(s = in.getAs[String].get)
            case "b" => defSimple = defSimple.copy(b = in.getAs[Boolean].get)
          }
        }
        defSimple
      }

      def write(t: Simple, out: JsonWriter): JsonWriter = {
        out.write("i", t.i)
        out.write("d", t.d)
        out.write("l", t.l)
        out.write("s", t.s)
        out.write("b", t.b)
      }
    }
  

    implicit object ComplexFormat extends JsonFormat[Complex] {
      def read(in: JsonReader): Complex = {
        var defComplex = Complex(1, null, None, null, null, null, None)
        while(in.hasNext) {
          val name = in.nextName
          name match {
            case "i" => defComplex = defComplex.copy(i = in.getAs[Int].get)
            case "s" => defComplex = defComplex.copy(s = in.getAs[Simple].get)
            case "sopt" => defComplex = defComplex.copy(sopt = in.getAs[Option[Simple]].get)
            case "lInt" => defComplex = defComplex.copy(lInt = in.getAs[Vector[Int]].get)
            case "lSimple" => defComplex = defComplex.copy(lSimple = in.getAs[Vector[Simple]].get)
            case "lOptSimple" => defComplex = defComplex.copy(lOptSimple = in.getAs[Vector[Option[Simple]]].get)
            case "optListSimple" => defComplex = defComplex.copy(optListSimple = in.getAs[Option[Vector[Simple]]].get)
            case _ => {in.skipValue}                                                     
          }
        }
        defComplex
      }
      
      def write(t: Complex, out: JsonWriter): JsonWriter = {
        out.write("i", t.i)
        out.write("s", t.s)
        out.write("sopt", t.sopt)
        out.write("lInt", t.lInt)
        out.write("lSimple", t.lSimple)
        out.write("lOptSimple", t.lOptSimple)
        out.write("optListSimple", t.optListSimple)
      }
      
    }
  }
  
}
