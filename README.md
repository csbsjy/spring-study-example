## spring-study-example 



> spring version: spring-boot 2.1.2<br/>
> IDE: STS 4.1.2 for Eclipse <br/>
> java version: 1.8.0_172


* 2019-03-10 --- [HandlerMethodArgumentResolver](#handlermethodargumentresolver)
* 2019-03-16 --- [Spring REST Docs](#spring-rest-docs)

---

## HandlerMethodArgumentResolver 

https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/method/support/HandlerMethodArgumentResolver.html

> Strategy interface for resolving method parameters into argument values in the context of a given request.

> 컨트롤러에 들어오는 파라미터를 커스터마이징할 때 사용할 수 있는 인터페이스로, 공통적으로 수행해야 하는 작업을 수행한 후 해당 Object를 반환함으로써 코드의 중복을 줄일 수 있다. 클라이언트의 요청이 담긴 파라미터를 컨트롤러보다 먼저 받아서 작업을 수행한다. 

| Instance Method                                              | Description                                                  |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| Object [resolveArgument](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/method/support/HandlerMethodArgumentResolver.html#resolveArgument-org.springframework.core.MethodParameter-org.springframework.web.method.support.ModelAndViewContainer-org.springframework.web.context.request.NativeWebRequest-org.springframework.web.bind.support.WebDataBinderFactory-)([MethodParameter](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/core/MethodParameter.html) parameter, [ModelAndViewContainer](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/method/support/ModelAndViewContainer.html) mavContainer, [NativeWebRequest](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/context/request/NativeWebRequest.html) webRequest,[WebDataBinderFactory](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/bind/support/WebDataBinderFactory.html) binderFactory) | 공통작업 수행 후 리턴                                        |
| boolean supportsParameter(MethodParameter parameter)         | parameter가 해당 resolver에 의해 수행될 수 있는 타입인지 true/false로 리턴. 이 메소드가 먼저 수행되고 true일 시 resolveArgument를 수행한다. |

------

### 예제

1. User 타입의 파라미터를 통해 사용자가 접속한다.
2. User 아이디는 a, b, c 셋 중 하나로 시작한다.
3. a 로 시작하면 관리자, b로 시작하면 vip회원, c로 시작하면 일반회원이다.
4. 각 사용자별로 페이지에 접속할 수 있는 권한이 차등지급된다.
5. 본인이 접속할 수 없는 페이지면 상태코드 403(FORBIDDEN)을 리턴한다. 



1) User 객체생성

```java
package com.edu.tistory.model;

import lombok.Getter;
import lombok.Setter;

@Getter
public class User {
	private String userId;
	private String userPassword;
	@Setter
	private UserType userType;
	
	public enum UserType {
		Manager, VIPMember, Member
	}
}
```



2) LoginUser 어노테이션 생성

```java
package com.edu.tistory.custom;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoginUser {

}

```





2) HandlerMethodArgumentResolver 구현 

```java
package com.edu.tistory.custom;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.edu.tistory.model.User;
import com.edu.tistory.model.User.UserType;

public class LoginUserResolver implements HandlerMethodArgumentResolver{

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		//parameter가 User Type인지 체크 
		return parameter.getParameterType().isAssignableFrom(User.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
	
		User user = new User();		
		
		//User의 id가 시작하는 값에 따라 UserType(Manager, VIPMemeber, Member)차등부여하고 리턴
		String userId= webRequest.getParameter("userId");
		if(userId.charAt(0) == 'a') user.setUserType(UserType.Manager);
		else if(userId.charAt(0)=='b') user.setUserType(UserType.VIPMember);
		else user.setUserType(UserType.Member);
		
		return user;
	}

}

```



4) Controller 구현

```java
package com.edu.tistory.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edu.tistory.custom.LoginUser;
import com.edu.tistory.model.User;
import com.edu.tistory.model.User.UserType;

@RestController
public class LoginController {

	
	@GetMapping("/login/manager")
	public ResponseEntity<String> pageForManager(@LoginUser User user) {
		// Page for manager
		return getResponseEntity(user, UserType.Manager);
	}
	
	@GetMapping("/login/vip")
	public ResponseEntity<String> pageForVIPMember(@LoginUser User user) {
		// Page for vip
		return getResponseEntity(user, UserType.VIPMember);
	}
	
	@GetMapping("/login/member")
	public ResponseEntity<String> pageForMember(@LoginUser User user) {
		// Page for member
		return getResponseEntity(user,UserType.Member);
	}
	
	public ResponseEntity<String> getResponseEntity(User user, UserType userType) {
		if(user.getUserType()!=userType)
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
}

```



5) Config 작성 

```java
package com.edu.tistory.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.edu.tistory.custom.LoginUserResolver;

@Configuration
public class WebConfig implements WebMvcConfigurer{
	
	@Bean
	public LoginUserResolver loginUserResolver() {
		return new LoginUserResolver();
	}
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(loginUserResolver());
		WebMvcConfigurer.super.addArgumentResolvers(resolvers);
	}
}

```



위와 같이 작성 후 userId 에 차이를 주고 url에 접근하면 적절히 resolver가 수행되는 것을 알 수 있다. 



---

## Spring REST Docs

참고링크

<https://docs.spring.io/spring-restdocs/docs/2.0.3.RELEASE/reference/html5/>

<https://cheese10yun.github.io/spring-rest-docs/>

<http://woowabros.github.io/experience/2018/12/28/spring-rest-docs.html>



Spring REST Docs은 snippets을 이용하여 API 명세서를 생성하는 도구로, 기본적으로 [Asciidoctor](http://asciidoctor.org/)를 통해(+markdown도 가능하긴한데 뭔가 많이 복잡하다.)  HTML형식의 문서를 출력한다. snippets을 생성하는 방법에는 Spring MVC의 Test framework(ex: JUnit) WebTestClient, REST Assured3가 있다.



*cf) Spring REST Docs vs Swagger(우아한형제들 기술블로그 펌)*

|      | Spring Rest Docs                | Swagger                                     |
| ---- | ------------------------------- | ------------------------------------------- |
| 장점 | 제품코드에 영향 없다.           | API 를 테스트 해 볼수 있는 화면을 제공한다. |
|      | 테스트가 성공해야 문서작성된다. | 적용하기 쉽다.                              |
| 단점 | 적용하기 어렵다.                | 제품코드에 어노테이션 추가해야한다.         |
|      |                                 | 제품코드와 동기화가 안될수 있다.            |



*실제 Swagger를 적용한 Controller 코드 일부*

```java
@GetMapping()
	@ApiOperation(value="게시글 리스트")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "start", value="글시작번호", required = true, dataType = "int", paramType="query", defaultValue="0"),
		@ApiImplicitParam(name = "end", value="글종료번호", required = true, dataType = "int", paramType="query", defaultValue="10")
	})
	public ResponseEntity<List<Board>> list(@RequestParam(value="start", defaultValue="0") int start,
			@RequestParam(value="end", defaultValue="10") int end ) throws Exception{
		
		List<Board> list= boardService.boardList(start, end);
		return new ResponseEntity<>(list, HttpStatus.OK);	
	}
```

*상단 어노테이션에 해당하는 Api 명세설명과 실제 코드를 일치시키기 위해 결국 개발자가 "직접" 동기화시켜야하고 이에 따라 human error가 일어날 수 있다. 또한, Swagger코드가 길어지면 실제 로직에 대한 가독성이 낮아질 수 있다. 하지만, Postman과 같이 테스트를 할 수 있는 쉽고 예쁜(?) ui를 제공한다는 장점이 있다.*



### Sample Code



**Requirements**

> java 8
>
> Spring Framework5



**Development Environment**

> Spring boot 2.1.3
>
> java8
>
> maven
>
> JUnit4





**pom.xml**

```xml
<dependency> <!-- test 기반이기 때문에 test scope이면 됨-->
	<groupId>org.springframework.restdocs</groupId>
	<artifactId>spring-restdocs-mockmvc</artifactId>
	<version>2.0.3.RELEASE</version>
	<scope>test</scope>
</dependency>

<build>
	<plugins>
		<plugin> <!-- asciidoctor plugin 추가-->
			<groupId>org.asciidoctor</groupId>
			<artifactId>asciidoctor-maven-plugin</artifactId>
			<version>1.5.3</version>
			<executions>
				<execution>
					<id>generate-docs</id>
					<phase>prepare-package</phase> <!-- documentation이 패키지 안에 포함될 수 있도록 허용함-->
					<goals>
						<goal>process-asciidoc</goal>
					</goals>
					<configuration>
						<backend>html</backend>
						<doctype>book</doctype>
					</configuration>
				</execution>
			</executions>
			<dependencies>
				<dependency> <!--자동으로 target/generated-snippets에 snippets 생성-->
					<groupId>org.springframework.restdocs</groupId>
					<artifactId>spring-restdocs-asciidoctor</artifactId>
					<version>2.0.2.RELEASE</version>
				</dependency>
			</dependencies>
		</plugin>
        <plugin> 
	<groupId>org.asciidoctor</groupId>
	<artifactId>asciidoctor-maven-plugin</artifactId>
	<!-- … -->
</plugin>
<plugin> 
	<artifactId>maven-resources-plugin</artifactId>
	<version>2.7</version>
	<executions>
		<execution>
			<id>copy-resources</id>
			<phase>prepare-package</phase>
			<goals>
				<goal>copy-resources</goal>
			</goals>
			<configuration> 
				<outputDirectory><!--생성된 docs를 jar안에 packaging-->
					${project.build.outputDirectory}/static/docs
				</outputDirectory>
				<resources>
					<resource>
						<directory>
							${project.build.directory}/generated-docs
						</directory>
					</resource>
				</resources>
			</configuration>
		</execution>
	</executions>
</plugin>
	</plugins>
</build>
```



기본 설정은 끝났고, 테스팅을 위한 간단한 testcontroller 를 작성했다. 

**SampleTestController.java*

```java
package com.example.demo;

import com.example.demo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserInfoById(@PathVariable("id") String id){
        User user = new User();
        user.setId(id);
        user.setName("User1");
        user.setAge(25);
        user.setInfo("This is Test User!!!");
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/user")
    public ResponseEntity<User> createUserByUserModel(User user){
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}

```



이제 REST Docs 생성과 직접적으로 관련된 Test 코드를 작성한다. 

JUnit4 기반으로 작성하였다.



**SampleControllerTest.java**

```java
package com.example.demo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class SampleControllerTest {

     /* snippets이 생성될 위치를 지정하는 부분으로 아무것도 지정하지 않을 시
    Maven의 경우, target/generated-snippets
    Gradle의 경우, build/generated-snippets
    에 생성된다.*/
    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

    private MockMvc mockMvc;


    @Autowired
    private WebApplicationContext context;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation))
                .alwaysDo(document("{method-name}/{class-name}"))
                .build();
    }

    @Test
    public void getUserInfoById() throws Exception{
        this.mockMvc.perform(get("/user/user1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("index",
                        responseFields(
                                fieldWithPath("id").description("The Board's number"),
                                fieldWithPath("name").description("The Board's title"),
                                fieldWithPath("age").description("The Board's contents"),
                                fieldWithPath("info").description("The Board's writeName")
                               )
                ));

    }

    @Test
    public void createUserByUserModel() throws  Exception{
        this.mockMvc.perform(post("/user")
                                .param("id", "user1")
                                .param("name","username1")
                                .param("age", "22")
                                .param("info", "Is this Alright?"))
                            .andDo(print())
                            .andExpect(status().isOk())
                            .andDo(document("index",requestParameters(
                                    parameterWithName("id").description("User's id"),
                                    parameterWithName("name").description("User's name"),
                                    parameterWithName("age").description("User's age"),
                                    parameterWithName("info").description("User's info")),
                                    responseFields(
                                        fieldWithPath("id").description("The Board's number"),
                                        fieldWithPath("name").description("The Board's title"),
                                        fieldWithPath("age").description("The Board's contents"),
                                        fieldWithPath("info").description("The Board's writeName")
                                )
                            ));



    }
}





```



(1) 테스팅 결과로 나오는 파일을 명명 규칙을 지정한다. 해당 설정을 해주면 generated-snippets/{method-name}/{class-name} 디렉토리가 생성되고 그 아래 adoc파일이 생성된다. 

여기에서

```java
  responseFields(
                                    fieldWithPath("id").description("The Board's number"),
                                    fieldWithPath("name").description("The Board's title"),
                                    fieldWithPath("age").description("The Board's contents"),
                                    fieldWithPath("info").description("The Board's writeName")
                                )
```

이부분만 각자의 api 응답에 따라 작성해주면된다.

fieldWithPath("응답데이터").description("설명") 

이런식으로!



그 후, <u>테스트가 정상적으로 성공한다면</u> target 디렉토리에 다음과 같은 폴더들이 생성된다.

![image-20190317234639922](/Users/seojaeyeon/IdeaProjects/assets/SpringRESTDocs-1.png)





**src/main아래에 asciidoc 폴더를 생성하고 api-docs.adoc 라는 파일을 하나 생성한 뒤 다음과 같이 입력한다.**



asciidoc 문법은 <https://asciidoctor.org/docs/asciidoc-syntax-quick-reference/> 에서 볼 수 있다. 마크다운이랑 비슷한 수준인 것 같다.



**src/main/asciidoc/api-docs.adoc**

```asciiarmor
= Sample Project API 명세 (Spring REST Docs)

== getUserInfoById

=== Curl request

include::{snippets}/get-user-info-by-id/sample-controller-test/curl-request.adoc[]

=== HTTP request

include::{snippets}/get-user-info-by-id/sample-controller-test/http-request.adoc[]

=== HTTP response

include::{snippets}/get-user-info-by-id/sample-controller-test/http-response.adoc[]


=== request body

include::{snippets}/get-user-info-by-id/sample-controller-test/request-body.adoc[]


=== response body

include::{snippets}/get-user-info-by-id/sample-controller-test/response-body.adoc[]



== CreateUserByUserModel

=== Curl request

include::{snippets}/create-user-by-user-model/sample-controller-test/curl-request.adoc[]

=== HTTP request

include::{snippets}/create-user-by-user-model/sample-controller-test/http-request.adoc[]

=== HTTP response

include::{snippets}/create-user-by-user-model/sample-controller-test/http-response.adoc[]


=== request body

include::{snippets}/create-user-by-user-model/sample-controller-test/request-body.adoc[]


=== response body

include::{snippets}/create-user-by-user-model/sample-controller-test/response-body.adoc[]



```





이제 정말 끝 

**maven install을 실행한다.**



빌드가 성공적으로 되었다면, target/generated-docs에 api-docs.html 파일이 생길 것이다.

이제 서버를 기동시키고 localhost:port/docs/api-docs.html 을 접속해보자.

![image-20190318000719794](/Users/seojaeyeon/IdeaProjects/assets/SpringRESTDocs-2.png)

-끝-





Swagger에 비해 훨씬 복잡하고 test를 하나하나 작성해야 문서에 반영된다는 점이 좀 귀찮긴 했지만, 다시 생각해 보면 "테스트"를 작성해야 한다는 점이 장점이라는 생각이 들었다. 테스팅을 성공하지 않으면 문서 자체를 만들 수 없기 때문에 실제 코드와 일치하는 API 명세를 보장할 수 있다. 혼자 개발할 땐 편하게 쓸 수 있는 Swagger를, 실제 명세가 필요할 땐 REST Docs를 이용하지 않을까싶다.