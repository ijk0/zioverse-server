package io.ijk.domain.user

import java.time.ZonedDateTime

final case class UserId(value: Long) extends AnyVal
final case class UserData(username: String, password: String,
                          email: String,
                          avatar: String,
                          description: String)
final case class User(
                       userId: UserId,
                       username: String,
                       password: String,
                       email: String,
                       avatar: String,
                       description: String,
                       register_time: ZonedDateTime,
                       login_time: ZonedDateTime
) {
  def userData = UserData(username, password, email, avatar, description)
}

object User {
  def withData(
      userid:UserId, userdata: UserData
  ): User = User(
    userid,
    userdata.username,
    userdata.password,
    userdata.email,
    userdata.avatar,
    userdata.description,
    ZonedDateTime.now(),
    ZonedDateTime.now()
  )
}