package io.ijk.application

import io.ijk.domain.DomainError
import io.ijk.domain.user.*
import zio.*

object UserService:

  def addUser(username: String, password: String, email: String, avater: String,
              description: String): ZIO[UserRepository, DomainError, UserId] =
    ZIO.serviceWithZIO[UserRepository](_.add(UserData(username, password, email, avater, description)))

  def deleteUser(id: UserId): ZIO[UserRepository, DomainError, Long] =
    ZIO.serviceWithZIO[UserRepository](_.delete(id))

  def getAllUsers(): ZIO[UserRepository, DomainError, List[User]] =
    ZIO.serviceWithZIO[UserRepository](_.getAll())

  def getUserById(id: UserId): ZIO[UserRepository, DomainError, Option[User]] =
    ZIO.serviceWithZIO[UserRepository](_.getById(id))

  def updateUser(id: UserId, username: String, password: String, email: String, avater: String,
                 description: String): ZIO[UserRepository, DomainError, Option[User]] =
    for {
      repo <- ZIO.service[UserRepository]
      data <- ZIO.succeed(UserData(username, password, email, avater, description))
      maybeUpdated <- repo.update(id, data)
    } yield maybeUpdated.map(_ => User.withData(id, data))