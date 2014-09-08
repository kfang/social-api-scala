scala-instagram
===============

Scala Client for Instagram API
I should probably say that this is a **dogfooding** library. If something is missing that you *really* want, feel free to file an issue or create a pull-request. I'd be more than happy to take a look.

#AuthService
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

#UsersService
###Request User Information
```scala
//for self
UsersService.getInfo("ACCESS_TOKEN")

//for another user
UsersService.getInfo("USER_ID", "ACCESS_TOKEN")
```
