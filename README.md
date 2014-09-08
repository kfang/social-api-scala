Social Network API Integration Library for Scala
===============

Scala Client for Instagram API
I should probably say that this is a **dogfooding** library. If something is missing that you *really* want, feel free to file an issue or create a pull-request. I'd be more than happy to take a look.

#Config
This library uses the typesafe Config library to store/read out Client ID, Secret, and redirect URLs.
```json
instagram-client {
  read-timeout: 10000,
  connect-timeout: 10000,
  id: "CLIENT-ID",
  secret: "CLIENT-SECRET"
  redirect-uri: "REDIRECT-URL"
}
```

#Authentication Service
###Request Explicit Login Flow Url
```scala
AuthService.requestExplicitUrl(Basic, Comments, Relationships, Likes)
```

###Request Implicit Login Flow Url
```scala
AuthService.requestImplicitUrl(Basic, Comments, Relationships, Likes)
```

###Request Access Token
```scala
AuthService.requestAccessToken(code)
```

#Users Service
###Request User Information
```scala
//for self
UsersService.getInfo("ACCESS_TOKEN")

//for another user
UsersService.getInfo("USER_ID", "ACCESS_TOKEN")
```

#Relationships Service
###Request User's Follows
```scala
//for self
val fr: Future[FollowsResponse] = RelationshipsService.getFollows("ACCESS_TOKEN")

//for another user
val fr: Future[FollowsResponse] = RelationshipsService.getFollows("USER_ID", "ACCESS_TOKEN")

//pagination
// - if there is no next, it will return an empty list in 'data'
// - you can also check if there is a next with 'fr.map(_.hasNext)'
fr.flatMap(_.next)
```
