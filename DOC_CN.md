## Defender和Guarder
对于``defender``的定义，我们将之抽象为一个城堡守卫阵法，如果要守护城堡，并不可少需要守护者的参与，而``guarder``就是所谓的守护者。

一个服务之中只会有一个``defender``，与之息息相关的``guarder``会和``defender``形成``MANY-TO-ONE``的关系，每个``guarder``都可以指定不同的模式和策略去抵御或者说是过滤访客和外敌，对于多个``guarder``同时运作，他们之间将会相互合作，为服务的安全性一起努力工作！
#### Defender
``defender``为一个单例对象，我们可以通过以下方式获取它
```
Defender.getInstance()
```
``defender``更像是一个容器，它装载着我们定义的各种``guarder``来帮助我们进行权限管理，我们可以通过调用``registry``方法去注册一个``guarder``
```
Defender.getInstance()
        .registry(guarder)
        .ready()
```
可以看出，在注册完``guarder``之后，我们调用了``ready``方法，这是必不可少的，这是我们通知``defender``一切准备就绪信号，一旦执行此方法，``defender``将会进行一起准备的初始化工作，此方法执行完之后，``defender``的一切准备工作告一段落！
#### Guarder
获取一个Guarder很简单，它的构造被私有化，以来使初始化的过程更加优雅，下面是一个完整的``guarder``实例化过程
```
Guarder.builder(GuarderType.URI)
		.pattern("POST /user")
		.order(1)
		.preventer(caller -> {
			return Result.pass();
		})
```
其中的``order``是可以缺省的，有了它你可以自定义多个``guarder``防御的顺序，最前面的可以称之为敢死队。在调用``builder``方法的时候，我们需要传入一个枚举值来表示采用的防御模式，这个之后我们将会详解。从此可以看出，获取一个``guarder``对象也是如此的简单。

## Defender的几种防御模式
``defender``提供三种防御模式，对于不同的防御模式配置风格不尽相同
 - 注解模式
 - Expression模式
 - URI(Ant风格)

虽然与众不同，但是可以项目配合着使用，以下我们将会进一步深入了解每种模式
#### 注解模式
使用注解模式，我们需要实例化一个对应的``guarder``
```
Guarder.builder(GuarderType.ANNOTATION)
		.pattern("org.nico.trap.controller")
		.preventer(caller -> {
			return Result.pass();
		})
```
紧接着，我们还需要在需要防御的方法上添加``@Access``注解
```
@Access(AuthConst.LOGIN)
@PostMapping("/")
public ResponseVo<GameFullVo> publishGame(@RequestBody GamePublishVo gameVo) throws TrapException{
	GameFullVo gameFullVo = gameService.publishGame(gameVo);
	return new ResponseVo<GameFullVo>(ResponseCode.SUCCESS, gameFullVo);
}
```
``@Access``注解内传入的value值可以根据当前系统的情况单独定义一个常量，``defender``并没有做强定义！对于每种模式下的``pattern``的格式不同，对于注解模式，``pattern``的意义就是代表着包名，对于该包下的所有带有``@Access``都将会进行防御。
#### Expression模式
表达式模式相比注解模式，粒度变得更大，但是通用性也会变大，我们可以通过一下方式获得一个Expression模式的``guarder``
```
Guarder.builder(GuarderType.EXPRESSION)
    	.pattern("* org.nico.trap.controller.UserController.*(..)")
    	.preventer(caller -> {
    		return Result.pass();
    	})
```
与注解模式不同的是，``pattern``的格式和所代表的的含义变化很大，这里表示一个``execution表达式``。
#### URI(Ant风格)
URI模式则更为直观
```
Guarder.builder(GuarderType.URI)
		.pattern("POST /user")
		.preventer(caller -> {
			return Result.pass();
		})
```
``pattern``中分两段，第一段为请求类型，后一段为请求资源地址，匹配方式使用``ANT``风格，整体与Expression模式类似，不同的是，Expression模式拦截的是方法，URI模式拦截的是接口。
## Guarder详解
之前的讲述中，我们知道了``defender``和``guarder``的联系和作用，也知道了``guarder``的各种防御模式，下面我们将会对``guarder``进入更深层次的剖析，这样才能更好更灵活的使用``guarder``。
#### Guarder的builder方法
该方法需要传入一个枚举值``GuarderType``去区分``guarder``的防御模式，返回值将会是一个``guarder``对象，也是唯一实例化的入口！
#### Guarder的pattern方法
``pattern``的含义随着防御模式的不同而变化着，对于同一个``guarder``，可以有多个``pattern``，如在URI模式下可以写成
```
Guarder.builder(GuarderType.URI)
		.pattern("POST /user")
		.pattern("DELETE /user")
		.pattern("* /game")
		.preventer(caller -> {
			return Result.pass();
		})
```
#### Guarder的order方法
``order``方法指定一个``int``值表示``defender``下所有``guarder``的防御顺序，``order``值越小优先级越高，可缺省，默认值为0。
#### Guarder的preventer方法
该方法传入一个``AbstractPreventer``接口对象，我们需要实现该接口内部的``detection``方法去自定义防御规则
```
public interface AbstractPreventer {
	public Result detection(Caller caller);
}
```
接下来我们拟定一个场景：需要获取请求头里的``token``来判断用户是否是在登录状态下的防御，这里我们使用注解模式进行防御来构造一个``guarder``对象
```
Guarder.builder(GuarderType.ANNOTATION)
		.pattern("org.nico.trap.controller")
		.preventer(new AbstractPreventer() {
			
			@Autowired
			private AuthComponent authComponent;
			
			@Override
			public Result detection(Caller caller) {
				String identity = caller.getAccess().value();
				if(! identity.equals(AuthConst.VISITOR)) {
					UserBo user = authComponent.getUser();
					if(user != null) {
						if(identity.equals(AuthConst.ADMIN)){
							if(user.getRuleType() == null) {
								return Result.notpass(new ResponseVo<>(ResponseCode.ERROR_ON_USER_IDENTITY_MISMATCH));
							}
						}
					}else {
						return Result.notpass(new ResponseVo<>(ResponseCode.ERROR_ON_LOGIN_INVALID));
					}
				}
				return Result.pass();
			}
		})
```
上述的``AbstractPreventer``实现类中，我们用到了``@Autowired``，这是``defender``必须支持的功能，而对于简单的防御，我们可以使用``lambda``更加简洁的实现，如判断请求头``token``是否存在
```
Guarder.builder(GuarderType.ANNOTATION)
		.pattern("org.nico.trap.controller")
		.preventer(caller -> {
			return caller.getRequest().getHeader("token") == null 
					? Result.notpass("Token not exist") 
					: Result.pass();
		})
```
从上述例子可以看出，``defender``方法的返回值分两种类型：``pass``和``notpass``，通过``Result``的静态方法获取对应的实例
 - ``Result.notpass(Object o)`` 表示未穿破防御，这个请求时非法的，传入的参数``o``代表返回给请求者的提示信息。
 - ``Result.pass()`` 表示合法请求


