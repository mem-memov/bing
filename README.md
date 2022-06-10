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
val address = new address.Instance(List(0.toUByte))
memory.append(address)
```

```bash
sudo apt install clang
sudo apt install libgc-dev # optional

sbt test
```
