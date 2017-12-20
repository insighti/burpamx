# AMX Authorization Burp Suite Extension

Extension for Burp Suite - to be used via **Session Handling Rules**.

Will add Authorization header looking somewhat like this:

```
Authorization: amx fd5a33d2ac52234ec6ff79ca526ef305:FjfyFpH8W8xIllBReGl0kiiXhYZTRQoyLXluwsEIhtE=:3265893b-aa4d-4d70-97ae-ea10ae73dc16:1513739146
```

Configure via newly added "AMX Auth" tab.

## How to compile

Standard Maven project, checkout and:

```
mvn install
```

use the **jar** file from ```target/``` directory.

