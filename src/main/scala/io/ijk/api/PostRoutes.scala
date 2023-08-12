package io.ijk.api

import io.ijk.api.Extensions.*
import io.ijk.application.PostService
import io.ijk.domain.post.PostRepository
import io.ijk.domain.post.*
import io.ijk.domain.user.UserId
import io.ijk.domain.{DomainError, NotFoundError}
import zio.*
import zio.http.*
import zio.json.*

object PostRoutes extends JsonSupport:

  val app: HttpApp[PostRepository, Nothing] = Http.collectZIO {
    case Method.GET -> !! / "posts" =>
      val effect: ZIO[PostRepository, DomainError, List[Post]] =
        PostService.getAllPosts()

      effect.foldZIO(Utils.handleError, _.toResponseZIO)

    case Method.GET -> !! / "posts" / postId =>
      val effect: ZIO[PostRepository, DomainError, Post] =
        for {
          id <- Utils.extractLong(postId)
          maybePost <- PostService.getPostById(PostId(id))
          post <- maybePost.map(ZIO.succeed(_)).getOrElse(ZIO.fail(NotFoundError))
        } yield post

      effect.foldZIO(Utils.handleError, _.toResponseZIO)

    case Method.DELETE -> !! / "posts" / postId =>
      val effect: ZIO[PostRepository, DomainError, Unit] =
        for {
          id <- Utils.extractLong(postId)
          nms <- PostService.deletePost(PostId(id))
          _  <- if(nms == 0) ZIO.fail(NotFoundError) else ZIO.unit
        } yield ()

      effect.foldZIO(Utils.handleError, _.toEmptyResponseZIO)

    case req @ Method.POST -> !! / "posts" =>
      val effect: ZIO[PostRepository, DomainError, Post] =
        for {
          createPost <- req.jsonBodyAs[CreatePostRequest]
          postId <- PostService.addPost(UserId(createPost.userId), createPost.content)
        } yield Post.withData(postId, PostData(UserId(createPost.userId), createPost.content))

      effect.foldZIO(Utils.handleError, _.toResponseZIO)
  }