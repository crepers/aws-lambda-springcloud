# Spring Cloud Function with AWS Lambda

## 소개
AWS Lambda 는 서버, OS, 확장 성 등의 구성을 줄이기 위해 Amazon에서 제공하는 서버리스 컴퓨팅 서비스입니다. AWS Lambda는 AWS 클라우드에서 코드를 실행할 수 있습니다.
AWS Lambda 기능을 트리거하는 다른 AWS 리소스의 이벤트에 대한 응답으로 실행됩니다.<br/>
Spring Cloud Fuction 을 이용하면 AWS Lambda를 개발할 수 있습니다.


## Maven Dependency
AWS Lambda를 사용하고 Event를 처리하기 위한 Dependency를 추가합니다.
```xml
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-function-adapter-aws</artifactId>
        <version>2.0.1.RELEASE</version>
    </dependency>
    <dependency>
        <groupId>com.amazonaws</groupId>
        <artifactId>aws-lambda-java-events</artifactId>
        <version>2.2.6</version>
        <scope>provided</scope>
    </dependency>
    <dependency>
        <groupId>com.amazonaws</groupId>
        <artifactId>aws-lambda-java-log4j</artifactId>
        <version>1.0.0</version>
        <scope>provided</scope>
    </dependency>
```

##  Maven Plugin
Maven으로 생성된 artifact를 AWS Lambda에 업로드하기 위해 크기를 줄일 수 있도록 plugin 설정을 추가합니다.
```xml
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <configuration>
            <source>1.8</source>
            <target>1.8</target>
        </configuration>
    </plugin>
    <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
        <dependencies>
            <dependency>
                <groupId>org.springframework.boot.experimental</groupId>
                <artifactId>spring-boot-thin-layout</artifactId>
                <version>1.0.11.RELEASE</version>
            </dependency>
        </dependencies>
    </plugin>
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-shade-plugin</artifactId>
        <configuration>
            <createDependencyReducedPom>false</createDependencyReducedPom>
            <shadedArtifactAttached>true</shadedArtifactAttached>
            <shadedClassifierName>aws</shadedClassifierName>
        </configuration>
    </plugin>
```

## Request Object
```java
public class Request {
	private String input;

	public String getInput() {
		return input;
	}

	public void setInput(final String input) {
		this.input = input;
	}
}
```

## Response Object
```java
public class Response {
	private String result;

	public String getResult() {
		return result;
	}

	public void setResult(final String result) {
		this.result = result;
	}
}
```


## AWS Lambda Handler
Spring Cloud Function에서 AWS Lambda 전용으로 제공하는 Adapter라는 추상화 계층이 있습니다. AWS Adapter에는 SpringBootRequestHandler, SpringBootStreamHandler, FunctionInvokingS3EventHandler 등과 같이 사용할 수있는 여러 가지 reuest handler가 있습니다.
```java
public class AwsLambdaFunctionHandler extends SpringBootRequestHandler<Request, Response> { 

} 
```

## Component
Function 처리를 위한 함수를 구현합니다.
```java
@Component("awsLambdaFunction")
public class AwsLambdaFunction implements Function<Request, Response> {

    private final AwsLambdaService service;

    public AwsLambdaFunction(AwsLambdaService service) {
        this.service = service;
    }

    @Override
    public Response apply(final Request request) {
        final Response result = new Response();
        result.setResult(service.uppercase(request.getInput()));
        return result;
    }
}
```

## Service
서비스 함수를 구현합니다.
```java
@Service
public class AwsLambdaService {
	public String uppercase(final String input) {
		return input.toUpperCase(Locale.ENGLISH);
	}
}
```

## Main Application
의존성 주입 및 구성 요소 스캔이 지원을 위해 @SpringBootApplication 클래스를 추가합니다.
```java
@SpringBootApplication
public class AwsLambdaSpringBootApplication  {

    public static void main(String[] args) throws Exception {
    	SpringApplication.run(AwsLambdaSpringBootApplication.class, args);
    }
}
```

## AWS Lambda 
- Maven Build : mvn clean install
- AWS Console > Lambda > Create a Lambda function
- runtime : Java 8
- upload : target 폴더 아래에 2개의 jar 파일이 생성 되는데 <b>-aws</b> 로 끝나는 jar를 업로드 합니다.
- lambda function handler : com.wcjung.sample.handler.AwsLambdaFunctionHandler
- memory : 192
- timeout : 60 sec


## Test
- Request
```json
    {
        "input": "Hello Spring Cloud!!!"
    }
```
- Response
```json
    {
        "result": "HELLO SPRING CLOUD!!!"
    }
```


## References
- [Spring Cloud Function](https://spring.io/projects/spring-cloud-function)
- [github : spring-cloud-function](https://github.com/spring-cloud/spring-cloud-function/tree/master/spring-cloud-function-samples/function-sample-aws)
- [AWS Lambda](https://aws.amazon.com/ko/lambda/)
