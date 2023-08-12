package io.ijk.domain.post

import io.ijk.domain.RepositoryError
import zio.*

trait PostRepository:
  def add(data: PostData): IO[RepositoryError, PostId]

  def delete(id: PostId): IO[RepositoryError, Long]

  def getAll(): IO[RepositoryError, List[Post]]

  def getById(id: PostId): IO[RepositoryError, Option[Post]]

  def update(userid: PostId, data: PostData): IO[RepositoryError, Option[Unit]]

object PostRepository:

  def add(data: PostData): ZIO[PostRepository, RepositoryError, PostId] =
    ZIO.serviceWithZIO[PostRepository](_.add(data))

  def delete(id: PostId): ZIO[PostRepository, RepositoryError, Long] =
    ZIO.serviceWithZIO[PostRepository](_.delete(id))

  def getAll(): ZIO[PostRepository, RepositoryError, List[Post]] =
    ZIO.serviceWithZIO[PostRepository](_.getAll())

  def getById(id: PostId): ZIO[PostRepository, RepositoryError, Option[Post]] =
    ZIO.serviceWithZIO[PostRepository](_.getById(id))

  def update(postid: PostId, data: PostData): ZIO[PostRepository, RepositoryError, Option[Unit]] =
    ZIO.serviceWithZIO[PostRepository](_.update(postid, data))
