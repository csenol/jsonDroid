package csenol.json

import scala.collection.mutable.ArrayBuffer
import com.google.gson.stream.JsonToken

trait StandardFormats {

  implicit def optionFormat[T: JsonFormat] = new JsonFormat[Option[T]] {
    def read(in: JsonReader): Option[T] = Some(implicitly[JsonReaderT[T]].wrapRead(in)) 
    override def wrapRead(in: JsonReader): Option[T] = 
      if (in.peek() != JsonToken.NULL) { 
        read(in)
      }
      else None

    def write(t: Option[T], out: JsonWriter): JsonWriter = out
    override def wrapWrite(name: String, t: Option[T], out: JsonWriter): JsonWriter = {
      t match {
        case None => out
        case Some(v) => {
          implicitly[JsonWriterT[T]].wrapWrite(name, v, out)
        }
      }
    }

    override def wrapWrite(t: Option[T], out: JsonWriter): JsonWriter = {
      t match {
        case None => out
        case Some(v) => {
          implicitly[JsonWriterT[T]].wrapWrite(v, out)
        }
      }
    }


  }

}
