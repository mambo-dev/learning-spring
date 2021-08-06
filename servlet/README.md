# Servlet
서블릿(Servlet)과 서블릿 컨테이너(Servlet Container)에 대해서 알고 계신가요?

선뜻 떠올리지 못했다면 지금이라도 자바 웹 프로그래밍을 시작했을때로 돌아가서 `서블릿`에 대한 개념을 숙지해봅시다. [서블릿](https://jakarta.ee/specifications/servlet/)은 자바 웹 애플리케이션의 기본이라고할 수 있는 개념이며 HTTP 요청을 처리하고 응답할 수 있도록 정의된 자바 클래스입니다. 하지만, 이 서블릿 자체는 스스로 동작하지않으며 서블릿 컨테이너에 의해 서블릿의 라이프사이클이 관리됩니다. 

서블릿의 동작을 관리하는 `서블릿 컨테이너`는 일반적으로 잘 알려져있는 [아파치 톰캣](http://tomcat.apache.org/), [제티](https://www.eclipse.org/jetty/) 그리고 [언더토우](https://undertow.io/) 등 WAS라고 하는 웹 애플리케이션 서버입니다. 

## Deployment Descriptor
서블릿 컨테이너는 `배포 서술자(Deployment Descriptor)`라고 하는 web.xml 파일을 참조하여 HTTP 요청을 어떻게 위임하고 처리할 것인지를 결정하고 서블릿을 생성하고 관리하여 요청을 위임하게 됩니다. 

## Dispatcher Servlet
`디스패처 서블릿(Dispathcer Servlet)`은 배포 서술자에 요청을 처리하게될 서블릿을 매핑하는 것을 보완하기 위하여 사용되는 개념으로 서블릿 컨테이너는 배포 서술자에 정의된 디스패처 서블릿으로 모든 요청을 위임하게 되며 디스패처 서블릿은 요청을 분석하여 실제로 처리를 담당해야할 서블릿을 찾아서 요청을 위임하고 처리된 결과를 서블릿 컨테이너에 전달합니다. 

## Spring Servlet Stack
스프링 프레임워크의 [서블릿 스택](https://docs.spring.io/spring-framework/docs/current/reference/html/web.html)은 앞서 알아본 서블릿, 배포 서술자, 디스패처 서블릿등과 같은 서블릿 관련 개념을 추상화하고 쉽게 적용할 수 있는 여러가지 클래스를 제공합니다.

먼저, 스프링의 서블릿 스택에서 사용되는 배포 서술자 파일 정의의 예시를 보시겠습니다.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app version="2.5" xmlns="http://java.sun.com/xml/ns/javaee"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://java.sun.com/xml/ns/javaee  http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd">
       <context-param>
              <param-name>contextConfigLocation</param-name>
              <param-value>/WEB-INF/application-context.xml</param-value>
       </context-param>
       
       <listener>
              <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
       </listener>
       
       <servlet>
              <servlet-name>dispatcher</servlet-name>
              <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
              <init-param>
                     <param-name>contextConfigLocation</param-name>
                     <param-value></param-value>
              </init-param>
              <load-on-startup>1</load-on-startup>
       </servlet>
              
       <servlet-mapping>
              <servlet-name>dispatcher</servlet-name>
              <url-pattern>/</url-pattern>
       </servlet-mapping>
</web-app>
```

그리고 서블릿 3.0부터는 이 배포 서술자 마저도 web.xml 파일로 정의하지않고 자바 코드로 정의할 수 있는 SPI의 추가로 인하여 스프링 프레임워크에서는 WebApplicationInitializer 인터페이스를 구현해 배포 서술자를 정의하는 것을 지원하게 됩니다. 

다음은 자바 코드로 배포 서술자를 정의하는 예시입니다.

```java
public class WebServletInitializer implementation WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) {
        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
        applicationContext.setConfigLocation("com.example.demo.config");
        servletContext.addListener(new ContextLoaderListener(applicationContext));
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet(applicationContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
    }
}
```

서블릿 그리고 서블릿 컨테이너를 시작으로 서블릿 컨테이너가 참조하는 배포 서술자를 알아보고 최종적으로는 스프링에서 배포서술자를 자바 코드 방식으로 정의할 수 있음을 알았습니다. 어느정도 머리속에 들어왔다면 각 개념에 대해서 자세히 설명하는 블로그를 통해 정확한 개념을 숙지해보시기 바랍니다. 