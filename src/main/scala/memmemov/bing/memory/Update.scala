package memmemov.bing.memory

sealed trait Update
object Updated extends Update
object NotUpdatedContentTooBig extends Update
object NotUpdated extends Update
