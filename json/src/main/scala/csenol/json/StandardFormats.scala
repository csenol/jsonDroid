package csenol.json

import scala.collection.mutable.ArrayBuffer
import android.util.JsonToken

trait StandardFormats {

  implicit def optionFormat[T: JsonFormat] = new JsonFormat[Option[T]] {
    def read(in: JsonReader): Option[T] = if (in.peek() != JsonToken.NULL) Some(implicitly[JsonReaderT[T]].read(in)) else None
    def write(t: Option[T], out: JsonWriter): JsonWriter = out
    override def wrapWrite(name: String, t: Option[T], out: JsonWriter): JsonWriter = {
      t match {
        case None => out.nullValue()
        case Some(v) => {
          implicitly[JsonWriterT[T]].wrapWrite(name, v, out)
        }
      }
    }

  }

}
