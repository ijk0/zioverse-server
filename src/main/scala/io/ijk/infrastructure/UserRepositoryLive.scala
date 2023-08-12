package io.ijk.infrastructure

import io.getquill.*
import io.getquill.jdbczio.Quill
import io.ijk.domain.RepositoryError
import io.ijk.domain.user.*
import zio.*

import java.sql.SQLException

final class UserRepositoryLive(quill: Quill.Postgres[Literal]) extends UserRepository:

  import quill.*

  inline def users = quote {
    querySchema[User]("users")
  }

  override def add(data: UserData): IO[RepositoryError, UserId] =
    val effect: IO[SQLException, UserId] = run {
      quote {
        users
          .insertValue(lift(User.withData(UserId(0), data)))
          .returningGenerated(_.userId)
      }
    }

    effect
      .either
      .resurrect
      .refineOrDie {
        case e: NullPointerException => RepositoryError(e)
      }
      .flatMap {
        case Left(e: SQLException) => ZIO.fail(RepositoryError(e))
        case Right(userId: UserId) => ZIO.succeed(userId)
      }

  override def delete(id: UserId): IO[RepositoryError, Long] =
    val effect: IO[SQLException, Long] = run {
      quote {
        users.filter(u => u.userId == lift(id)).delete
      }
    }

    effect.refineOrDie {
      case e: SQLException => RepositoryError(e)
    }

  override def getAll(): IO[RepositoryError, List[User]] =
    val effect: IO[SQLException, List[User]] = run {
      quote {
        users
      }
    }

    effect.refineOrDie {
      case e: SQLException => RepositoryError(e)
    }

  override def getById(id: UserId): IO[RepositoryError, Option[User]] =
    val effect: IO[SQLException, List[User]] = run {
      quote {
        users.filter(u => u.userId == lift(id))
      }
    }

    effect
      .map(_.headOption)
      .refineOrDie {
      case e: SQLException => RepositoryError(e)
    }

  override def update(userid: UserId, data: UserData): IO[RepositoryError, Option[Unit]] =
    val effect: IO[SQLException, Long] = run {
      quote {
        users.
          filter(u => u.userId == lift(userid)).
//          update(_.username = lift(data.username), _.password = lift(data.password))
          updateValue(lift(User.withData(userid,data))) //todo create time stay unchanged
      }
    }

    effect
      .map(n => if (n > 0) Some(()) else None) // map一下和结果类型保持一致
      .refineOrDie {
      case e: SQLException => RepositoryError(e)
    }

object UserRepositoryLive:

  val layer: URLayer[Quill.Postgres[Literal], UserRepository] = ZLayer {
    for {
      quill <- ZIO.service[Quill.Postgres[Literal]]
    } yield UserRepositoryLive(quill)
  }