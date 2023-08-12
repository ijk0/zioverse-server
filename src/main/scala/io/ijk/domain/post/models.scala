package io.ijk.domain.post
import io.ijk.domain.user.UserId

import java.time.ZonedDateTime

final case class PostId(value: Long) extends AnyVal
final case class PostData(userId: UserId, content: String)
final case class Post(
  postid: PostId,
  userId: UserId,
  content: String,
  create_time: ZonedDateTime,
  edit_time: ZonedDateTime) {
  def postData = PostData(userId, content)
}

object Post {
  def withData(
      postid: PostId,
      postData: PostData
  ): Post = Post(
    postid,
    postData.userId,
    postData.content,
    ZonedDateTime.now(),
    ZonedDateTime.now()
  )
}
