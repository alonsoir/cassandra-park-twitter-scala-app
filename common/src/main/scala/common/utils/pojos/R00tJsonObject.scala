package common.utils.pojos

case class R00tJsonObject(
  createdAt: Option[String],
  id: Option[Double],
  text: Option[String],
  source: Option[String],
  isTruncated: Option[Boolean],
  inReplyToStatusId: Option[Double],
  inReplyToUserId: Option[Double],
  isFavorited: Option[Boolean],
  retweetCount: Option[Double],
  isPossiblySensitive: Option[Boolean],
  
  //contributorsIDs: Option[List[ContributorsIDs]],
  retweetedStatus: Option[RetweetedStatus],
  userMentionEntities: Option[List[UserMentionEntities]],
  //urlEntities: Option[List[UrlEntities]],
  //hashtagEntities: Option[List[HashtagEntities]],
  //mediaEntities: Option[List[MediaEntities]],
  
  currentUserRetweetId: Option[Double],
  user: Option[User]
)
