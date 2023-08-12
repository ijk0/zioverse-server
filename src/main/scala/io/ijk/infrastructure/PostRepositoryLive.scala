package io.ijk.infrastructure

import io.getquill.*
import io.getquill.jdbczio.Quill
import io.ijk.domain.RepositoryError
import io.ijk.domain.post.*
import zio.*

import java.sql.SQLException

final class PostRepositoryLive(quill: Quill.Postgres[Literal]) extends PostRepository:

  import quill.*

  inline def posts = quote {
    querySchema[Post]("posts")
  }

  override def add(data: PostData): IO[RepositoryError, PostId] =
    val effect: IO[SQLException, PostId] = run {
      quote {
        posts
          .insertValue(lift(Post.withData(PostId(0), data)))
          .returningGenerated(_.postid)
      }
    }
    effect.either
      .resurrect
      .refineOrDie{
        case e: NullPointerException => RepositoryError(e)
      }
      .flatMap{
        case Right(postId: PostId) => ZIO.succeed(postId)
        case Left(e: SQLException) => ZIO.fail(RepositoryError(e))
    }

  override def delete(id: PostId): IO[RepositoryError, Long] = ???

  override def getAll(): IO[RepositoryError, List[Post]] = ???

  override def getById(id: PostId): IO[RepositoryError, Option[Post]] = ???

  override def update(userid: PostId, data: PostData): IO[RepositoryError, Option[Unit]] = ???

object PostRepositoryLive:
  val layer: URLayer[Quill.Postgres[Literal], PostRepository] = ZLayer {
    for {
      quill <- ZIO.service[Quill.Postgres[Literal]]
    } yield PostRepositoryLive(quill)
  }
end PostRepositoryLive

