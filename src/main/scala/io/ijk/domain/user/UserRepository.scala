package io.ijk.domain.user

import io.ijk.domain.RepositoryError
import io.ijk.domain.user.UserId
import io.ijk.domain.user.UserData
import io.ijk.domain.user.User
import zio.*

trait UserRepository:
  def add(data: UserData): IO[RepositoryError, UserId]

  def delete(id: UserId): IO[RepositoryError, Long]

  def getAll(): IO[RepositoryError, List[User]]

  def getById(id: UserId): IO[RepositoryError, Option[User]]

  def update(userid: UserId, data: UserData): IO[RepositoryError, Option[Unit]]

object UserRepository:

  def add(data: UserData): ZIO[UserRepository, RepositoryError, UserId] =
    ZIO.serviceWithZIO[UserRepository](_.add(data))

  def delete(id: UserId): ZIO[UserRepository, RepositoryError, Long] =
    ZIO.serviceWithZIO[UserRepository](_.delete(id))

  def getAll(): ZIO[UserRepository, RepositoryError, List[User]] =
    ZIO.serviceWithZIO[UserRepository](_.getAll())

  def getById(id: UserId): ZIO[UserRepository, RepositoryError, Option[User]] =
    ZIO.serviceWithZIO[UserRepository](_.getById(id))

  def update(userid: UserId, data: UserData): ZIO[UserRepository, RepositoryError, Option[Unit]] =
    ZIO.serviceWithZIO[UserRepository](_.update(userid, data))
