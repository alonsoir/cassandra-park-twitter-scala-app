package common.utils.pojos

case class UserMentionEntities(
  name: Option[String],
  screenName: Option[String],
  id: Option[Double],
  start: Option[Double],
  end: Option[Double]
)