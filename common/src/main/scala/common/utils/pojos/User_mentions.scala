package common.utils.pojos

case class User_mentions(
  name: Option[String],
  id_str: Option[String],
  id: Option[Double],
  indices: Option[List[Double]],
  screen_name: Option[String]
)