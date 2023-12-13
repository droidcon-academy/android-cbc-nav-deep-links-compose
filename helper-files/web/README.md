# Links Web App
Accompanion for the Android app

# Setup

- In order for the routing to work correctly, put the content of `web` directory in the root of your web server. 
- Add the following directive to Apache configuration to allow passing requests for [itemName]s to index.html:
```
  FallbackResource index.html
```