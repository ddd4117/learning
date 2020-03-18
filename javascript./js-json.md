# Javscript json key field contains .

```json
{
	"version": 1,
	"registration": {
	"name": "UI",
	"managementUrl": "http://localhost:8080/actuator",
	"healthUrl": "http://localhost:8080/actuator/health",
	"serviceUrl": "http://localhost:8080/",
	"source": "discovery",
	"metadata": {
		"management.port": "8080"
	}
}
```

javascript에서 다음과같은 json을 처리할 때

```javascript
var port = meatadata.management.port
```

를 하게되면  Cannot read property 'port' of undefined 라는 에러가 나타난다.

이렇듯, javascript에서 .(dot)가 포함되는 경우에는

```javascript
var port = metadata["management.port"]
```

로 처리해야한다.

Javascript에서는 .연산자를 통해 모든 필드에 접근 할 수있는데, []안에 문자열을 넣어 필드이름을 처리할 수 있다.
