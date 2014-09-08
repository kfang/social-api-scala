Social Network API Integration Library for Scala
===============

Scala Client for Instagram, Facebook, Google Plus, Twitter API
I should probably say that this is a **dogfooding** library. If something is missing that you *really* want, feel free to file an issue or create a pull-request. I'd be more than happy to take a look.

#Config
This library uses the typesafe Config library to store/read out Client ID, Secret, and redirect URLs.
```
common-client {
  read-timeout: 10000,
  connect-timeout: 10000
}

instagram-client {
  id: "CLIENT-ID",
  secret: "CLIENT-SECRET",
  redirect-uri: "REDIRECT-URL"
}
```

#Usage
check out the [wiki](https://github.com/kfang/social-api-scala/wiki) for instructions
