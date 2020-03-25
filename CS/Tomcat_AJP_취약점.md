# AJP 취약점에 따른 Embedded Tomcat 버전 변경

Tomcat AJP Port에 취약점이 발견되었습니다. 

자세한 내용은  https://www.krcert.or.kr/data/secNoticeView.do?bulletin_writing_sequence=35292 

## 영향을 받는 Tomcat version

- 9.0.0.M1 ~ 9.0.30
- 8.5.0 ~ 8.5.50
- 7.0.0 ~ 7.0.99



## Spring boot Tomcat version 변경

Tomcat 버전을 올리는 방법은

1. spring boot의 버전을 올리는 방법
2. embedded tomcat의 버전만 올리는 방법

이 있겠지만, 1번의 경우 관련 dependency의 버전도 올려야 되는 경우가 있을 수 있기 때문에 2번을 택했습니다.

(1번의 경우도 Spring boot 2.2.5 이전 버전은 9.0.30 이하버전을 사용하고 있습니다.)



Tomcat의 버전 변경은 pom.xml에 

```xml
<properties>
    <tomcat.version>9.0.31</tomcat.version>
</properties>
```

을 명시하면 됩니다,



## AJP Connector 설정

버전만 올리고 실행을하면, 실행이 되지않는 문제가 발생합니다.

그 이유는 Tomcat-Apache 연동 설정을 기존대로 구성시 연동 실패가 발생하며 AJP 기본값이 루프백 주소를 수신하도록 변경되어 수정해주지 않을시 503에러가 발생한다고 합니다.

```java
	@Bean
	public ServletWebServerFactory servletContainer() {
		TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
		tomcat.addAdditionalTomcatConnectors(createAjpConnector());
		return tomcat;
	}

	private Connector createAjpConnector() {
		Connector ajpConnector = new Connector(ajpProtocol);
		ajpConnector.setPort(ajpPort);
		ajpConnector.setSecure(false);
		ajpConnector.setAllowTrace(false);
		ajpConnector.setScheme("http");
		((AbstractAjpProtocol) ajpConnector.getProtocolHandler()).setSecretRequired(false);
		try {
			((AbstractAjpProtocol) ajpConnector.getProtocolHandler()).setAddress(InetAddress.getLocalHost());
		} catch (UnknownHostException ex) {
			ex.printStackTrace();
		}
		
		return ajpConnector;
	}
```

- 기본 값으로 SSL을 사용하도록 설정되어 있으므로, SSL을 사용하지 않는 환경에서는 무한로딩이 발생합니다.
  ((AbstractAjpProtocol) ajpConnector.getProtocolHandler()).setSecretRequired(false);
  확인해보면 setRequiredSecret도 있는데 deprecated 되어있습니다.
- AJP가 기본값으로 루프백 주소를 사용하도록 되어있습니다. address="0.0.0.0"와 같이 네트워크 대역을 추가해야 합니다.
  ((AbstractAjpProtocol) ajpConnector.getProtocolHandler()).setAddress(InetAddress.getLocalHost());

해당 설정을 통해 Apache - Tomcat이 연동되는 것을 확인했습니다.



## 참고 자료

-  https://nirsa.tistory.com/131 
-  https://blog.alyac.co.kr/2772 
