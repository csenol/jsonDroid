package com.vngrs.json

import scala.collection.mutable.ArrayBuffer
import com.google.gson.stream.JsonToken

trait StandardFormats {

  implicit def optionFormat[T: JsonFormat] = new JsonFormat[Option[T]] {
    def read(in: JsonReader): Option[T] = Some(implicitly[JsonReaderT[T]].rootRead(in)) 
    override def rootRead(in: JsonReader): Option[T] = 
      if (in.peek() != JsonToken.NULL) { 
        read(in)
      }
      else None

    def write(t: Option[T], out: JsonWriter): JsonWriter = out
    override def namedWrite(name: String, t: Option[T], out: JsonWriter): JsonWriter = {
      t match {
        case None => out
        case Some(v) => {
          implicitly[JsonWriterT[T]].namedWrite(name, v, out)
        }
      }
    }

    override def rootWrite(t: Option[T], out: JsonWriter): JsonWriter = {
      t match {
        case None => out
        case Some(v) => {
          implicitly[JsonWriterT[T]].rootWrite(v, out)
        }
      }
    }


  }

}
