package common.utils.pojos


case class User(
  id: Option[Double],
  name: Option[String],
  screenName: Option[String],
  description: Option[String],
  //descriptionURLEntities: Option[List[DescriptionURLEntities]],
  isContributorsEnabled: Option[Boolean],
  profileImageUrl: Option[String],
  profileImageUrlHttps: Option[String],
  isProtected: Option[Boolean],
  followersCount: Option[Double],
  profileBackgroundColor: Option[String],
  profileTextColor: Option[String],
  profileLinkColor: Option[String],
  profileSidebarFillColor: Option[String],
  profileSidebarBorderColor: Option[String],
  profileUseBackgroundImage: Option[Boolean],
  showAllInlineMedia: Option[Boolean],
  friendsCount: Option[Double],
  createdAt: Option[String],
  favouritesCount: Option[Double],
  utcOffset: Option[Double]
  /*,
  profileBackgroundImageUrl: Option[String],
  profileBackgroundImageUrlHttps: Option[String],
  profileBannerImageUrl: Option[String],
  profileBackgroundTiled: Option[Boolean],
  lang: Option[String],
  statusesCount: Option[Double],
  isGeoEnabled: Option[Boolean],
  isVerified: Option[Boolean],
  translator: Option[Boolean],
  listedCount: Option[Double],
  isFollowRequestSent: Option[Boolean]
  */
)