package io.ijk.api

import io.ijk.domain.post.{Post, PostId}
import io.ijk.domain.user.{User, UserData, UserId}
import io.ijk.domain.{Item, ItemId}
import zio.json.*

final case class UpdateItemRequest(name: String, price: BigDecimal)
final case class PartialUpdateItemRequest(name: Option[String], price: Option[BigDecimal])
final case class CreateItemRequest(name: String, price: BigDecimal)
//final case class CreateUserRequest(username: String, password: String, email: String, avater: String,
//                                   description: String)
final case class CreatePostRequest(userId: Long, content: String)

trait JsonSupport:
  implicit val itemIdEncoder: JsonEncoder[ItemId] = JsonEncoder[Long].contramap(_.value)
  implicit val itemEncoder: JsonEncoder[Item]     = DeriveJsonEncoder.gen[Item]

  implicit val updateItemDecoder: JsonDecoder[UpdateItemRequest] = DeriveJsonDecoder.gen[UpdateItemRequest]

  implicit val partialUpdateItemDecoder: JsonDecoder[PartialUpdateItemRequest] =
    DeriveJsonDecoder.gen[PartialUpdateItemRequest]

  implicit val createItemDecoder: JsonDecoder[CreateItemRequest] = DeriveJsonDecoder.gen[CreateItemRequest]

  implicit val userIdEncoder: JsonEncoder[UserId] = JsonEncoder[Long].contramap(_.value)

  implicit val userEncoder: JsonEncoder[User]     = DeriveJsonEncoder.gen[User]
  implicit val createUserDecoder: JsonDecoder[UserData] = DeriveJsonDecoder.gen[UserData]

  implicit val postIdEncoder: JsonEncoder[PostId] = JsonEncoder[Long].contramap(_.value)
  implicit val postEncoder: JsonEncoder[Post]     = DeriveJsonEncoder.gen[Post]
  implicit val createPostDecoder: JsonDecoder[CreatePostRequest] = DeriveJsonDecoder.gen[CreatePostRequest]
object JsonSupport extends JsonSupport
