
jsonDroid
=========

[![Build Status](https://travis-ci.org/vngrs/jsonDroid.png?branch=master)](https://travis-ci.org/vngrs/jsonDroid)
A Scala Wrapper for google-gson (mostly aiming Android)
Highly inspired by spray-json and reactivemongo macros

## Installation

## Usage
 
Almost same as in spray-json, import relevant elements.

```scala
import csenol.json._
import csenol.json.DefaultJsonProtocols._
```
These will import to implicit classes ```JsonWriterWrapper``` and ```JsonReaderWrapper``` into scope, so that 
you can use ```JsonReader``` with ```getAs[T]: Option[T]``` or ```getAsTry[T]: Try[T]``` methods and ```JsonWriter``` with ```write(t: T): JsonWriter``` method

In order to use those implicit classes, related type classes should be proivded. 
 * To read from JsonReader one has to implement ```JsonReader[T]``` 
 * To write to JsonWriter you need to implement ```JsonWriter[T]``` 
 * You can use bot of them with ```JsonFormat[T]```

 DefaultJsonProtocols defines following type classes for JsomFormat

 * String, Int, Long, Double, Boolean
 * Option
 * Collections based on Vector.
  * immutable => Iterable, Seq, IndexedSeq, LinearSeq, Set


### Reading from JsonReader

Use JsonReader which is provided by google-gson to convet json-input to an Scala object

```scala
  val s = Simple(1, 3.14, 12345L, "simple", true)
  val sString = """{"i":1,"d":3.14,"l":12345,"s":"simple","b":true}"""
  val jReader = new JsonReader( new StringReader( sString))
  assert( Option(s) === jReader.getAs[Simple])
```

#### Reader Type Class

```scala
    implicit object SimpleFormat extends JsonReaderT[Simple] {
      def read(in: JsonReader): Simple = {
        var defSimple = Simple(0 , 0.0 , 0L , "" , false)
        while(in.hasNext ){
          val name = in.nextName
          name match {
            case "i" => defSimple = defSimple.copy(i=in.getAs[Int].get)
            case "d" => defSimple = defSimple.copy(d = in.getAs[Double].get)
            case "l" => defSimple = defSimple.copy(l = in.getAs[Long].get)
            case "s" => defSimple = defSimple.copy(s = in.getAs[String].get)
            case "b" => defSimple = defSimple.copy(b = in.getAs[Boolean].get)
          }
        }
        defSimple
      }
```

### Writing to JsonWriter

```scala 
    val sWriter = new StringWriter()
    val jWriter = new JsonWriter( sWriter)
    val s = Simple(1, 3.14, 12345L, "simple", true)
    val sString = """{"i":1,"d":3.14,"l":12345,"s":"simple","b":true}"""
    jWriter.write(s)
    val json = sWriter.toString
    assert(sString === json)
```

#### Writer Type Class

```scala
    implicit object SimpleFormat extends JsonWriterT[Simple] {
      def write(t: Simple, out: JsonWriter): JsonWriter = {
        out.write("i", t.i)
        out.write("d", t.d)
        out.write("l", t.l)
        out.write("s", t.s)
        out.write("b", t.b)
      }
    }
```

## Shortcomings
  Implementing type classes is cumbersome. I will try to write some implicit macros for this

## Credits

## Contributors


