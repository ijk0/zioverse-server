package io.ijk.api

import io.ijk.api.Extensions.*
import io.ijk.application.UserService
import io.ijk.domain.{DomainError, NotFoundError}
import io.ijk.domain.user.*
import zio.*
import zio.http.*
import zio.json.*

object UserRoutes extends JsonSupport:
  val app: HttpApp[UserRepository, Nothing] = Http.collectZIO {
    case Method.GET -> !! / "users" =>
      val effect: ZIO[UserRepository, DomainError, List[User]] =
        UserService.getAllUsers()

      effect.foldZIO(Utils.handleError, _.toResponseZIO)



    case Method.GET -> !! / "users" / userId =>
      val effect: ZIO[UserRepository, DomainError, User] =
        for {
          id <- Utils.extractLong(userId)
          maybeUser <- UserService.getUserById(UserId(id))
          user <- maybeUser.map(ZIO.succeed(_)).getOrElse(ZIO.fail(NotFoundError))
        } yield user

      effect.foldZIO(Utils.handleError, _.toResponseZIO)

    case Method.DELETE -> !! / "users" / userId =>
      val effect: ZIO[UserRepository, DomainError, Unit] =
        for {
          id <- Utils.extractLong(userId)
          nms <- UserService.deleteUser(UserId(id))
          _  <- if(nms == 0) ZIO.fail(NotFoundError) else ZIO.unit
        } yield ()

      effect.foldZIO(Utils.handleError, _.toEmptyResponseZIO)

    case req @ Method.POST -> !! / "users" =>
      val effect: ZIO[UserRepository, DomainError, User] =
        for {
          createUser <- req.jsonBodyAs[UserData]
          userId <- UserService.addUser(createUser.username,
            createUser.password, createUser.email,createUser.avatar,createUser.description)
        } yield User.withData(userId, createUser)

      effect.foldZIO(Utils.handleError, _.toResponseZIO)
  }