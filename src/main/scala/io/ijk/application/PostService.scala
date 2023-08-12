package io.ijk.application


import io.ijk.domain.DomainError
import io.ijk.domain.post._
import io.ijk.domain.user.UserId
import zio.*

object PostService :
  def addPost(userId: UserId, content: String): ZIO[PostRepository, DomainError, PostId] =
    ZIO.serviceWithZIO[PostRepository](_.add(PostData(userId, content)))

  def deletePost(id: PostId): ZIO[PostRepository, DomainError, Long] =
    ZIO.serviceWithZIO[PostRepository](_.delete(id))

  def getAllPosts(): ZIO[PostRepository, DomainError, List[Post]] =
    ZIO.serviceWithZIO[PostRepository](_.getAll())

  def getPostById(id: PostId): ZIO[PostRepository, DomainError, Option[Post]] =
    ZIO.serviceWithZIO[PostRepository](_.getById(id))

  def updatePost(id: PostId, userid:UserId,content: String): ZIO[PostRepository, DomainError, Option[Post]] =
    for{
      repo <- ZIO.service[PostRepository]
      data <- ZIO.succeed(PostData(userid, content))
      maybeupdated <- repo.update(id, data)
    } yield maybeupdated.map(_ => Post.withData(id, data))
