# Unmatched_System<br>
创建一个SpringMVC工程，仅用于复习回顾Spring的知识，并不是一个项目。当然这边已经
搭好了框架，可以直接用这个框架来做项目<br>

##SpringMVC<br>
本次创建的MVC工程是纯JAVA CONFIG，没有.xml文件。在这个工程中，我们扩展了
*AbstractAnnotationConfigDispatcherServletInitializer*抽象类，扩展了该类
的任意类都会自动的配置DispatcherServlet和Spring应用上下文，Spring的应用
上下文会位于应用程序的Servlet上下文之中。<br>
###AbstractAnnotationConfigDispatcherServletInitializer剖析<br>
在 _Servlet3.0_ 环境中，容器会在 _类路径中_ 查找实现``javax.servlet.ServletContainerInitializer``
接口的类，如果能发现的话，就会用它来配置Servlet容器。Spring提供了这个接口的实现，名为
*SpringServletContainerInitializer*，这个类又会查找实现*WebApplicationInitializer*的类
并将配置的任务交给它们来完成。_Spring 3.2_ 引入了一个便利的*WebApplicationInitializer*
基础实现，也就是*AbstractAnnotationConfigDispatcherServletInitializer*。<br>
_getServletMappings():_ 该方法定义了SpringMvc起作用的URL模式，常见的URL模式有三种：<br>
【/】：它会拦截所有的URL，如：/test,/test.html,/1.jpg...，但是除了以jsp结尾的url不会
交给前端控制器（DispatcherServlet）。（注：前端控制器在收到请求后自己不进行任何处理，
而是根据URL映射规则委托给其它的页面控制器进行处理）如果某个Servlet的映射路径仅仅为一
个正斜杠（/），那么这个Servlet就成为当前web应用程序的缺省Servlet。当我们访问服务器中
的某个静态Html和图片时，实际上是在访问这个缺省的Servlet<br>
【/\*】：它是包含【/】的，可以拦截以\*.jsp结尾的url<br>
【\*.xxx】：拦截固定结尾的url请求，常见的有\*.do,\*.json<br>
【例：】我们定义的URL模式为\*.do,Controller层定义的路径为``@RequestMapping(value = "/ajax1")``，
则我们在浏览器输入【localhost:端口号/应用的上下文/ajax1.do】<br>
_getServletConfigClasses():_ 实际上用于创建特定于DispatcherServlet的Bean，例如ViewResolvers，
ArgumentResolvers，Interceptor，Controller等。
_getServletConfigClasses()方法与getRootConfigClasses()的区别：_ 在Spring MVC中我们其实是可以
创建多个DispatcherServlet的。而每个DispatcherServlet有自己的 _应用上下文_ ,这个应用上下文只
针对这个DispatcherServlet有用。getServletConfigClasses()的作用就是获取这个DispatcherServlet
的应用上下文的配置类。getRootConfigClasses()用于返回 _根应用上下文_ 的配置类。Spring框架机制
保证如果在当前的应用上下文中没有找到想要的bean时，会去根应用上下文去找。我们一般会将Services，
Repository在getRootConfigClasses返回的类中配置。(注：先加载RootConfigClasses)<br>
##解析Multipart数据<br>
multipart格式的数据会将一个表单拆分为多个部分（part），每个部分对应一个输入域。想要mvc处理
multipart请求。需要这么几步：<br>
一、使用DispatchServlet的registration来启用multipart请求<br>
二、配置multipart解析器（注：如果使用的是Part参数的形式接收文件的上传，那么就没必要配置解析
器了。只需要在该参数上加上@RequestPart参数即可）
##处理异常<br>
Spring提供了多种方式将异常转换为响应：<br>
一、特定的Spring异常将会自动映射为指定的HTTP状态码。<br>
二、异常上可以添加@ResponseStatus注解，从而将其映射为某一个HTTP状态码。<br>
三、在Controller层，使用带有@ExceptionHandler注解的方法来处理异常。（它能处理
同一个控制器中所有处理方法所抛出的异常）<br>
注：在带有@ControllerAdvice注解的类中，带有@ExceptionHandler，@InitBinder，
@ModelAttribute方法会运用到整个应用程序所有 _控制器_ 中带有@RequestMapping注解
的方法上。<br>
##跨重定向请求传输数据<br>
当控制器的结果是重定向的话，原始的请求就结束了，并且会发起新的 _get_ 请求。原始
请求中所带有的 _模型数据_ 也就随着请求一起消亡了。在新的请求属性中，没有任何的
模型数据。<br>
Spring有两种方法能够从发起重定向的方法传递数据给处理重定向的方法中：<br>
一、使用url模板以路径变量和/或查询参数的形式传递数据（它只能用来发送简单的值，如String
和数字的值）。<br>
二、通过flash属性发送数据（按照定义，flash属性会一直携带这些数据直到下一次请求，然后才会消失）。<br>
这里我们使用 _RedirectAttributes_ 类来代替 _Model_ 类，通过调用 _addFlashAttribute_ 添加flash属性<br>
原理：在重定向执行之前，所有的flash属性都会复制到会话中。在重定向后，存在会话中的flash属 性会被取出，
并从会话转移到模型之中。<br>
##缓存数据<br>
###ehcache 和 redis 比较<br>
ehcache直接在jvm虚拟机中缓存，速度快，效率高；但是缓存共享麻烦，集群分布式应用不方便。<br>

redis是通过socket访问到缓存服务，效率比ecache低，但比数据库要快很多， 
处理集群和分布式缓存方便，有成熟的方案。如果是单个应用或者对缓存访问要求很高的应用，
用ehcache。如果是大型系统，存在缓存共享、分布式部署、缓存内容很大的，建议用redis。<br>
###@Cacheable不起作用的原因<br>
1）缓存中的对象必须实现Serializable<br>
2）是否启用对注解的支持 即有没有使用@EnableCaching注解<br>
3）@Cacheable是基于Spring AOP代理类，内部方法调用是不走代理的 即如下代码是不会使用缓存<br>

      ``public void get(){
            selectByPrimaryKey(13);
        }
    
        @Cacheable
        @Override
        public User selectByPrimaryKey(int id){
            return userDao.selectByPrimaryKey(id);
        }``<br>
4)检查ehcache.xml中自定义缓存中设置对象的存活时间和死亡时间<br>
###@Cacheable注意事项<br>
因为@Cacheable 由AOP 实现，所以，如果该方法被其它注解切入，当缓存命中的时候，
则其它注解不能正常切入并执行，@Before 也不行，当缓存没有命中的时候，其它注解可以正常工作<br>
###@Cacheable和@CachePut的区别<br>
带有@Cacheable注解的方法，会在方法执行前会在缓存中查找条目，如果找到了匹配的条目，那么就不会
对方法进行调用了<br>
@CachePut并不会在缓存中检查匹配的值，目标方法总是会被调用，并将返回值添加到缓存之中<br>
###@Cacheable和@CachePut中的属性unless和condition的区别<br>
如果unless的spel表达式计算结果为true，那么缓存方法的返回值就不会放到缓存中；condition属性
的spel表达式的计算结果为false，那么对于这个方法缓存就会被禁用掉。<br>
_注意_：unless属性只能阻止将对象放进缓存，但在这个方法被调用的时候，依然会去缓存中进行查找，如果
找到了匹配的值，就会返回值。如果condition的表达式计算结果为false，那么在整个方法调用的过程中，
缓存是被禁用的。<br>
###@CacheEvict<br>
如果带有@CacheEvict注解的方法被调用的话，就会有一条或更多的条目会在缓存中移除<br>
###@Caching<br>
这是一个分组注解，能够同时应用多个其它缓存注解<br>
``@Caching(
              cacheable = {
                      @Cacheable(value = "empCache",key = "#lastName")
              },
              put =
                      {
                              @CachePut(value = "empCache",key = "#lastName")
                              @CachePut(value = "empCache",key = "#result.id")
                              @CachePut(value = "empCache",key = "#result.email")
                      }
      )
      public  Employee getEmpByLastName(String lastName)
      {
          return mapper.getEmpByLastName(lastName);
      }``
      
###CacheConfig<be>
假如一个功能下，每个缓存操作都要写cacheName,keygenerator那一定是太麻烦了。
@CacheConfig就是为了统一配置而生。只要在你service上统一配置，就一劳永逸了。

``@CacheConfig(cacheNames = "cacheNames")
  @Service
  public class EmployeeService {
        ...
  }``
###注意事项<br>
@CacheEvict能够应用在返回值为void的方法上，而@Cacheable和@CachePut需要在非void方法上使用<br>
##REST API(一种软件架构风格，不是标准)<br>
###如何设计RESTFUL风格的API<br>
在RESTful架构中，每个网址代表一种资源，所以网址中不能有动词，只能有名词，而且所用的名词往往与
数据库的表名对应，一般来说，数据库中的表都是同种记录的集合，所以API中的名词也应该用复数。对于
资源的具体操作类型，由HTTP动词表示（get post put delete patch）<br>
###HTTP信息转换器<br>
_注意：_ Spring自带了各种各样的转换器，大部分可以直接使用，由几个需要添加库到类路径下。如:
你想使用MappingJacksonHttpMessageConverter来实现json和Java对象的相互转换，那么需要将Jackson 
JSON Processor库添加到类路径中。类似的，如果你想使用Jaxb2RootElementHttpMessageConverter来
实现xml和java对象的互相转换，那么需要JAXB库。<br>
如何对 _返回的消息_ 使用消息转换器：在方法上使用@ResponseBody 注解即可。该注解会根据客户端的
_Accept_ 头部信息来判断客户端需要 _接收_ 什么类型的信息。如果头部信息表明它接受 “application/json”
且Jackson JSON 库位于应用的类路径下，那么将会选择MappingJacksonHttpMessageConverter
或MappingJackson2HttpMessageConverter（这取决于类路径下是哪个版本的 Jackson）。消息转换器
会将转换的json数据写到响应体中。（添加了@ResponseBody注解的方法不止能返回json还能返回xml等）<br>
如何对 _接受的消息_ 使用消息转换器：在参数上使用@RequestBody 注解即可。该注解会根据客户端的
_Content-Type_ 头部信息来判断客户端会 _发送_ 什么类型的信息。如果头部信息表明它接受 ”application/json“
*DispatchServlet*会查找能够将json转换为java对象的消息转换器。<br>
###如何添加资源以外的其它内容<br>
作为@ResponseBody的代替方案，控制器方法可以返回一个ResponseEntity<T>对象。ResponseEntity中可以
包含响应相关的元数据（如头部信息和状态码）以及要转换成资源表述的对象。<br>
###UriComponentsBuilder<br>
在处理器方法得到的UriComponentsBuilder中，会预先配置已知的信息如host、端口以及Servlet内容等。它
会从处理器方法所对应的请求中获取这些基础信息。<br>
###后端代码如何与RESTful API交互<br>
我们可以使用Apache HTTP Client来与RESTful API进行交互，但是Spring提供了一个更为便利的类
RestTemplate。<br>
####Get资源<br>
getForObject()返回所请求类型的对象，而getForEntity()方法会返回请求的对象及响应相关的额外信息。<br>
####Put资源<br>
put()<br>
####Delete资源<br>
delete()<br>
####Post资源<br>
postForObject() postForEntity(); postForLocation()调用该方法可以获取Location头信息的值（
返回的是一个url）<br>
####通过RestTemplate在请求中设置头信息<br>
exchange()方法中有个参数HttpEntity，通过HttpEntity我们可以在请求中设置头信息<br>

##异步消息<br>
###异步通信与同步通信的区别<br>
同步通信：当客户端向服务端发送消息时必须等待服务端的响应才能继续往下执行<br>
异步通信：客户端将消息发给消息中间件后无须等待响应即可继续往下执行<br>
###ActiveMQ<br>
为了发送异步消息，我们需要一个消息中间件。这里我们使用ActiveMq。我们可以去官网下载，下载
好后解压即可以使用。此外我们还需要导入activemq-all的依赖，版本与你下载的ActiveMq产品版
本一致。<br>
如何向ActiveMq发送消息(配置类：MessageServiceConfig)：<br>
1、设置activeMq连接工厂 指定连接地址<br>
2、申明一个消息目的地，可以是队列，也可以是主题。注意：不管是消息队列还是主题，必须设置
物理名（因为服务端会根据物理名来选中去哪个队列或主题中获取消息，物理名必须唯一）<br>
3、申明一个消息转换器，这里我们使用SimpleMessageConverter。因为我们传输的是json字符串，服务端
获取的是String类型的，不需要转换成对象。如果你想在json和对象之间相互转换就使用MappingJackson2MessageConverter，
前提是引入jackson依赖。<br>
4、创建JmsTemplate，调用convertAndSend()发送消息。使用receiveAndConvert()接收消息。注意：
调用receiveAndConvert()方法是同步的，如果队列中没有消息，它则会一直同步等待<br>

如何查看消息是否发往了ActiveMQ以及消息是否被消费：<br>
访问http://localhost:8161/admin/queues.jsp 登录控制面板 登录的用户名密码都是admin<br>

activemq 控制面板里的字段含义：<br>
1、Name：创建队列或主题时指定的物理名字<br>
2、Number Of Consumers：  消费者 这个是消费者端的消费者数量<br>
3、Number Of Pending Messages： 等待消费的消息 这个是当前未出队列的数量。可以理解为总接收数-总出队列数<br> 
4、Messages Enqueued 进入队列的消息 ： 进入队列的总数量,包括出队列的。 这个数量只增不减<br>
5、Messages Dequeued 出了队列的消息 ： 可以理解为是消费这消费掉的数量<br>
6、Messages Dequeued要分两种情况理解：在queues里它和进入队列的总数量相等(因为一个消息只会被成功消费一次),
如果暂时不等是因为消费者还没来得及消费。 
在 topics里 它因为多消费者从而导致数量会比入队列数高。<br>
简单的理解上面的意思就是 
当有一个消息进入这个队列时，等待消费的消息是1，进入队列的消息是1。 
当消息消费后，等待消费的消息是0，进入队列的消息是1，出队列的消息是1. 
在来一条消息时，等待消费的消息是1，进入队列的消息就是2.
没有消费者时  Pending Messages   和 入队列数量一样 
有消费者消费的时候 Pedding会减少 出队列会增加 
到最后 就是 入队列和出队列的数量一样多 <br>

注意：当以 _订阅模式_ 发布时，如果消费者后于生产者启动时，肯定是收不到生产者发布的消息的，
生产者发布的消息发布时，只在当时对在线的消费者推送一次，仅此而已。 <br>
由于JmsTemplate发送MQ消息时每次都要创建Connection和Session。因此引入Spring提供的
CachingConnectionFactory，起到类似于数据库连接池的效果<br>
注册JmsTemplate时，pubSubDomain这个属性的值要特别注意。默认值是false，
也就是说默认只是支持queue模式，不支持topic模式。但是，如果将它改为true，
则不支持queue模式。因此如果项目需要同时支持queue和topic模式，
那么需要注册2个JmsTemplate，同时监听容器（<jms:listener-container>）也需要注册2个<br>

###JMS如何异步接收消息<br>
首先我们要使用注解@EnableJms开启对JMS注解的支持，其次我们要配置JMS队列监听器容器工厂（
参考MessageServiceConfig.class中的配置）。最后我们只需要将对监听到的消息进行处理的
方法上使用@JmsListener注解即可（参考JmsServiceImpl中的getInfoAsync方法）<br>


##扩展知识<br>
一、lombok工具类可以用简单的注解形式来简化代码，提高开发效率。<br>
例：在实体类上使用@Data、@ToString()注解可以省去代码中大量的get()、 set()、 toString()等方法；
如果不想每次都写private  final Logger logger = LoggerFactory.getLogger(当前类名.class)可以用注解@Slf4j;<br>

二、@Repository和@Mapper注解的区别<br>
@Repository需要在Spring中配置扫描地址，然后生成Dao层的Bean才能被注入到Service层中。<br>
@Mapper不需要配置扫描地址，通过xml里面的namespace里面的接口地址，生成了Bean后注入到Service层中。<br>

三、HTTP报头的Accept与Content-Type的区别<br>
Accept属于请求头，Content-Type属于实体头。HTTP报头分为通用报头、请求报头、响应报头、实体报头
请求方的http报头结构：通用报头|请求报头|实体报头 
响应方的http报头结构：通用报头|响应报头|实体报头
Accept代表发送端希望接受的数据类型，如：Accept: text/xml 代表发送端希望接受xml类型的数据。
Content-Type代表发送端发送的实体数据的数据类型，如：Content-Type: text/html 代表发送端
发送的数据格式是html。<br>

##XML可视化代码详解（不止XML，也可以支持json）<br>
###相关的类<br>（赋值可以使用SPEL表达式，参考AidSystemUtils的generateSendFlowNo方法）
unmatched-common模块下的com.unmatched.common.messageTransform包下全部<br>

测试相关的类：unmatched-system-api模块下的com.unmatched.converter全部<br>

测试类：unmatched-system-api模块下的test文件夹com.unmatched包下的 TestBuildXml.class<br>

###看清xml报文的本质<br>
一份xml报文往往有许多节点构成。每个节点必定有节点名，可能还有节点值，节点属性及节点属性值。这个
节点可能是孤单的，也有可能这个节点下有许多的子节点。这个节点以及其下的子节点一起就构成了一个xml报文。
按照面向对象编程，我把它抽象成了XMLMessageNodeInfo类。<br>

###CoreAcctRecordConverterImpl类代码解析<br>
在接口组以往拼写报文的类中，代码往往与报文节点名耦合，这就导致如果节点需要变化，则代码也需要
改变。像这种两者直接关联引起的耦合，往往通过引入第三者的方式来进行解耦。所以在CoreAcctRecordConverterImpl类代码解析
中我们通过节点的Id来获取节点，为它赋值。<br>
这里我取消了XMLUtils，我们来思考一下为什么以前我们需要使用XMLUtils这个工具类。当我们拿到一份需求
文档时，我们很容易知道哪个节点赋什么值，哪个节点需要循环（由代码控制）。于是我们将这些值装到一个
List或者Map中，然后再丢到XMLUtils中，让他来替我们生成XML报文。因为我们的报文模板是一个字符型的
我们通过代码去循环去赋值显然不太可能。但现在我们把报文模板抽象成了一个类，因此我们可以对他做任何
的事情，在代码中直接给节点赋值，直接取出要循环的节点，然后循环生成它。正如我在CoreAcctRecordConverterImpl
中所做的。<br>

###约定<br>
约定我们存入数据库的根节点一定是类似于 /<?xml version="1.0" encoding="UTF-8"?/> 这种的XML申明<br>

###使用这个XML可视化工具的优点<br>
1、拼报文时不需要XMLUtils （XMLUtils如何拼报文往往与项目点的要求耦合，不能个个项目点通用）<br>
2、如果节点名不对不需要改代码，直接改数据库<br>
3、如果节点不需要了直接删除即可（前提代码里的做好判断）<br>
4、节点之间可以互换名字<br>
5、可以新增节点（前提是节点的值是常量，可以直接在数据库里赋值，如果需要从一个类里取值，那肯定
要改动代码了）<br>

###不足
1、未经足够的测试（我XML接触的比较少，基本都是json）<br>
2、写Converter类可能稍微复杂了点，且编写方式与以前不太相同了<br>
3、很慢，后续需要优化（主要慢在循环递归那块）<br>

