package common.utils.pojos


case class RetweetedStatus(
  createdAt: Option[String],
  id: Option[Double],
  text: Option[String],
  source: Option[String],
  isTruncated: Option[Boolean],
  inReplyToStatusId: Option[Double],
  inReplyToUserId: Option[Double],
  isFavorited: Option[Boolean],
  inReplyToScreenName: Option[String],
  retweetCount: Option[Double],
  isPossiblySensitive: Option[Boolean],
  userMentionEntities: Option[List[UserMentionEntities]],
  /*
  contributorsIDs: Option[List[ContributorsIDs]],
  urlEntities: Option[List[UrlEntities]],
  hashtagEntities: Option[List[HashtagEntities]],
  mediaEntities: Option[List[MediaEntities]],
  currentUserRetweetId: Option[Double],
  */
  user: Option[User]
)
