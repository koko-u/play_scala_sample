package controllers

import play._
import play.mvc._

object Application extends Controller {

  import views.Application._
  import models._
  import play.db.anorm._
  import java.util.{ Date }
  import play.data.validation._

  def index = {
    val allPosts = Post.allWithAuthorAndComments
    html.index(
      front = allPosts.headOption,
      older = allPosts.drop(1)
    )
  }

  def show(id: Long) = {
    Post.byIdWithAuthorAndComments(id).map { post =>
      html.show(post, post._1.prevNext)
    } getOrElse {
      NotFound("No such post")
    }
  }

  def postComment(postId: Long) = {
    val author = params.get("author")
    val content = params.get("content")
    Validation.required("author", author)
    Validation.required("content", content)
    if (Validation.hasErrors) {
      show(postId)
    } else {
      Comment.create(Comment(NotAssigned, author, content, new Date(), postId))
      flash += "success" -> ("Thanks for posting " + author)
      Action(show(postId))
    }
  }
}
