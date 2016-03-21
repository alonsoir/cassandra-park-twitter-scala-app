package common.utils.cassandra

import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._
import org.scalatest.{Matchers, FunSpec}

import scala.io.Source._
import common.utils.pojos._

class Json4sTest extends FunSpec with Matchers{
	
	describe("parsing a json twitter post") {
    it("should just work") {
        
        val data = parse("""{"createdAt":"Mar 15, 2016 5:08:32 PM","id":709773284624179200,"text":"RT @An_141: وزعت التداكر ونسيت ابقي لي تذكره ههههههههههههه منيب وجه دعم\nياعيال اللي معه تذكره زايده يعطيني","source":"\u003ca href\u003d\"http://twitter.com/download/iphone\" rel\u003d\"nofollow\"\u003eTwitter for iPhone\u003c/a\u003e","isTruncated":false,"inReplyToStatusId":-1,"inReplyToUserId":-1,"isFavorited":false,"retweetCount":0,"isPossiblySensitive":false,"contributorsIDs":[],"retweetedStatus":{"createdAt":"Mar 15, 2016 5:00:19 PM","id":709771217461452801,"text":"وزعت التداكر ونسيت ابقي لي تذكره ههههههههههههه منيب وجه دعم\nياعيال اللي معه تذكره زايده يعطيني","source":"\u003ca href\u003d\"http://tapbots.com/tweetbot\" rel\u003d\"nofollow\"\u003eTweetbot for iΟS\u003c/a\u003e","isTruncated":false,"inReplyToStatusId":709769576658161665,"inReplyToUserId":577121968,"isFavorited":false,"inReplyToScreenName":"An_141","retweetCount":69,"isPossiblySensitive":false,"contributorsIDs":[],"userMentionEntities":[],"urlEntities":[],"hashtagEntities":[],"mediaEntities":[],"currentUserRetweetId":-1,"user":{"id":577121968,"name":"الدحمي ،","screenName":"An_141","location":"Riyadh","description":"أخبث مما تظُن، وأفضل مما تتوقع\nSnapChat : D7mi.R / Kik: An_141\n للإعلانات : ads.an141@gmail.com","descriptionURLEntities":[],"isContributorsEnabled":false,"profileImageUrl":"http://pbs.twimg.com/profile_images/566660024059969536/O_uXX0Dd_normal.jpeg","profileImageUrlHttps":"https://pbs.twimg.com/profile_images/566660024059969536/O_uXX0Dd_normal.jpeg","url":"http://ask.fm/iAn141","isProtected":false,"followersCount":220000,"profileBackgroundColor":"131516","profileTextColor":"333333","profileLinkColor":"009999","profileSidebarFillColor":"EFEFEF","profileSidebarBorderColor":"EEEEEE","profileUseBackgroundImage":true,"showAllInlineMedia":false,"friendsCount":450,"createdAt":"May 11, 2012 3:04:40 PM","favouritesCount":506,"utcOffset":10800,"timeZone":"Baghdad","profileBackgroundImageUrl":"http://abs.twimg.com/images/themes/theme14/bg.gif","profileBackgroundImageUrlHttps":"https://abs.twimg.com/images/themes/theme14/bg.gif","profileBannerImageUrl":"https://pbs.twimg.com/profile_banners/577121968/1451779110","profileBackgroundTiled":true,"lang":"ar","statusesCount":14078,"isGeoEnabled":false,"isVerified":false,"translator":false,"listedCount":423,"isFollowRequestSent":false}},"userMentionEntities":[{"name":"الدحمي ،","screenName":"An_141","id":577121968,"start":3,"end":10}],"urlEntities":[],"hashtagEntities":[],"mediaEntities":[],"currentUserRetweetId":-1,"user":{"id":707479262211735552,"name":"الكـــوتـش||١٩٥٧||٥٧","screenName":"yasseralyeheli1","description":"ي أبن آدم لو بلغت ذنوبك عنان السمآء ثم إستغفرتني غفرت لك ولا أُبالي \u0027❤️","descriptionURLEntities":[],"isContributorsEnabled":false,"profileImageUrl":"http://pbs.twimg.com/profile_images/707492840415338496/6EFQtIu8_normal.jpg","profileImageUrlHttps":"https://pbs.twimg.com/profile_images/707492840415338496/6EFQtIu8_normal.jpg","isProtected":false,"followersCount":69,"profileBackgroundColor":"F5F8FA","profileTextColor":"333333","profileLinkColor":"2B7BB9","profileSidebarFillColor":"DDEEF6","profileSidebarBorderColor":"C0DEED","profileUseBackgroundImage":true,"showAllInlineMedia":false,"friendsCount":220,"createdAt":"Mar 9, 2016 9:12:55 AM","favouritesCount":5,"utcOffset":-1,"profileBackgroundImageUrl":"","profileBackgroundImageUrlHttps":"","profileBannerImageUrl":"https://pbs.twimg.com/profile_banners/707479262211735552/1457514485","profileBackgroundTiled":false,"lang":"en","statusesCount":390,"isGeoEnabled":false,"isVerified":false,"translator":false,"listedCount":0,"isFollowRequestSent":false}}""")
        println("data: " + data)
		println("------------------------------------")
		println
		implicit val formats = DefaultFormats // Brings in default date formats etc.
        

        val createdAt = (data\\"createdAt")
        val id = (data\\"id")
        val text = (data\\"text")
        val source = (data\\"source")
        
        println(compact(render("createdAt is " +createdAt)))
        /*
        println(compact(render("id is " +id)))
        println(compact(render("text is " +text)))
        println(compact(render("source is " +source)))
        */      
		val r00tJsonObject = data.extract[R00tJsonObject]
        println("r00tJsonObject: " + r00tJsonObject)  
		
		println("---------------------------")
		println
		val user = data.extract[User]
		println("user: " + user)

		println("---------------------------")
		println

		val user_mentions = data.extract[User_mentions]
		println("user_mentions: " + user_mentions)

		println("---------------------------")
		println
		val entities = data.extract[Entities]
		println("entities: " + entities)

		println("---------------------------")
		println		
        //id should be ("709773284624179200")
    }
  }

}