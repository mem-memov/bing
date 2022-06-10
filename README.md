# bing

This library provides an in-memory storage of addresses.
It enables you to send existing addresses to new places in memory.
You can run through all of them if you wish.
No thread safety is at your disposal.
Start with:

```scala
import memmemov.bing.{address, memory}
import scala.scalanative.unsigned.{UByte, UnsignedRichInt}

val memory = new memory.Instance
val firstAddress = memory.start

memory.append(firstAddress) match
  case memory.Appended(secondAddress) =>
    memory.append(secondAddress)
    ()
  case _ => 
    ()

memory.foreach{ a: address.Instance => 
  a.foreach { b: UByte =>
    // use the byte
    ()
  }
}
```

```bash
sudo apt install clang
sudo apt install libgc-dev # optional

sbt test
```
