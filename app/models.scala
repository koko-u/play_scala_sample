package models

import play.db.anorm._
import play.db.anorm.SqlParser._
import play.db.anorm.defaults._

import java.util.{ Date }

// User
case class User(
  id: Pk[Long],
  email: String, password: String, fullname: String, isAdmin: Boolean
)

object User extends Magic[User] {
  def connect(email: String, password: String) =
    User.find("email = {email} and password = {password}")
        .on("email" -> email, "password" -> password)
        .first()
}

// Post
case class Post(
  id: Pk[Long],
  title: String, content: String, postedAt: Date, author_id: Long
) {
  def prevNext = {
    SQL(
      """
      (SELECT *, 'next' as pos
         FROM Post
        WHERE postedAt < {date}
        ORDER BY postedAt DESC
        LIMIT 1)
      UNION
      (SELECT *, 'prev' as pos
         FROM Post
        WHERE postedAt > {date}
        ORDER BY postedAt DESC
        LIMIT 1)
      ORDER BY postedAt DESC
      """
    ).on("date" -> postedAt).as(
      opt('pos.is("prev") ~> Post.on("")) ~ opt('pos.is("next") ~> Post.on(""))
      ^^ flatten
    )
  }
}

object Post extends Magic[Post] {
  def allWithAuthor: List[(Post, User)] =
    SQL(
      """
      SELECT * FROM Post p
        JOIN User u ON p.author_id = u.id
       ORDER BY p.postedAt DESC
      """
    ).as( Post~< User ^^ flatten * )

  def allWithAuthorAndComments: List[(Post, User, List[Comment])] =
    SQL(
      """
      SELECT * FROM Post p
               JOIN User u on p.author_id = u.id
          LEFT JOIN Comment c on c.post_id = p.id
           ORDER BY p.postedAt DESC
      """
    ).as( Post ~< User ~< Post.spanM( Comment ) ^^ flatten * )

  def byIdWithAuthorAndComments(id: Long): Option[(Post, User, List[Comment])] =
    SQL(
      """
      SELECT * FROM Post p
               JOIN User u ON p.author_id = u.id
          LEFT JOIN Comment c ON c.post_id = p.id
              WHERE p.id = {id}
      """
    ).on("id" -> id).as( Post ~< User ~< Post.spanM( Comment ) ^^ flatten ? )

}

// Comment
case class Comment(
  id: Pk[Long],
  author: String, content: String, postedAt: Date, post_id: Long
)

object Comment extends Magic[Comment]

