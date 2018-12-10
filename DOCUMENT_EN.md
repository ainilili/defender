## Defender & Guarder
As for defender's definition, we abstract it into a castle guard formation. If you want to guard the castle, you need the guarder's participation, and guarder is the so-called guarder.

There is only ONE defender in a service, and guarder closely related TO guarder will form a many-to-one relationship with defender. Each guarder can specify different modes and strategies TO resist or filter visitors and foreign enemies. For multiple guarder running at the same time, they will cooperate with each other and work hard for the security of the service!
#### Defender
Defender is a singleton, and we can get it this way
```
Defender.getInstance()
```
Defender is more like a container, which loads the guarder we define to help us manage permissions. We can register a guarder by calling the registry method
```
Defender.getInstance()
        .registry(guarder)
        .ready()
```
It can be seen that after registering guarder, we called the ready method, which is essential. This is the signal that we notify defender of all ready. Once this method is executed, defender will perform the initialization work of preparation together.
#### Guarder
Getting a Guarder is simple, its construction is privatized to make the initialization process more elegant, and the following is a complete instantiation of Guarder
```
Guarder.builder(GuarderType.URI)
		.pattern("POST /user")
		.order(1)
		.preventer(caller -> {
			return Result.pass();
		})
```
The order is the default, with which you can customize the order of multiple guarder defenses, the first of which can be called the death squads.When we call the builder method, we need to pass in an enumeration value to indicate the defensive pattern we're using, which we'll explore later.As you can see, getting a guarder object is just as easy.

## Defender's several defense modes
Defender offers three defender modes, with different styles for different defender modes
 - Annotation model
 - Expression Model
 - URI(Ant Style)

Although different, it can be used in conjunction with a project, and we'll take a closer look at each pattern below
#### Annotation model
Using the annotation pattern, we need to instantiate a corresponding guarder
```
Guarder.builder(GuarderType.ANNOTATION)
		.pattern("org.nico.trap.controller")
		.preventer(caller -> {
			return Result.pass();
		})
```
Next, we need to add @access annotations to the methods we need to defend
```
@Access(AuthConst.LOGIN)
@PostMapping("/")
public ResponseVo<GameFullVo> publishGame(@RequestBody GamePublishVo gameVo) throws TrapException{
	GameFullVo gameFullVo = gameService.publishGame(gameVo);
	return new ResponseVo<GameFullVo>(ResponseCode.SUCCESS, gameFullVo);
}
```
The value value passed in the @access annotation can be defined as a single constant based on the current system condition, defender did not make the strong definition!The format of pattern under each mode is different. For the annotation mode, the meaning of pattern is to represent the package name, and all the patterns with @access under this package will be protected.
#### Expression Model
The Expression pattern is more granular than the annotation pattern, but it is also more general. We can obtain the guarder of an Expression pattern by the following methods
```
Guarder.builder(GuarderType.EXPRESSION)
    	.pattern("* org.nico.trap.controller.UserController.*(..)")
    	.preventer(caller -> {
    		return Result.pass();
    	})
```
n contrast to the annotation pattern, the format and meaning of the pattern vary widely, and an execution expression is represented here.
#### URI(Ant Style)
URI pattern are more intuitive
```
Guarder.builder(GuarderType.URI)
		.pattern("POST /user")
		.preventer(caller -> {
			return Result.pass();
		})
```
The first paragraph is the request type, and the second paragraph is the address of the requested resource. ANT style is used to match. Expression mode is similar to Expression mode in general.
## Guarder explain in detail 
In the previous discussion, we know the connection and role of defender and guarder, as well as the various defense modes of guarder. Now we will enter into a deeper analysis of guarder, so as to make better and more flexible use of guarder.
#### Guarder's builder method
This method needs to pass in an enumerated value GuarderType to distinguish guarder's guard mode, and the return value will be a guarder object and the only instantiated entry!
#### Guarder's pattern method
The meaning of pattern varies with different defense patterns. For the same guarder, there can be multiple patterns, such as URI pattern
```
Guarder.builder(GuarderType.URI)
		.pattern("POST /user")
		.pattern("DELETE /user")
		.pattern("* /game")
		.preventer(caller -> {
			return Result.pass();
		})
```
#### Guarder's order method
The order method specifies an int value indicating the defense order of all guarder under defender. The smaller the order value is, the higher the priority is. The default value is 0.
#### Methods the Guarder preventer
he method is introduced into a AbstractPreventer interface object, we need to implement the interface internal detection method to custom rules
```
public interface AbstractPreventer {
	public Result detection(Caller caller);
}
```
Next, let's consider a scenario where we need to get token in the request header to determine whether the user is protected against login, and we use annotation mode to construct a guarder object
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
The above AbstractPreventer implementation class, we use the @autowired, this is the function of the defender must support, for the simple defense, we can use the lambda more concise implementation, such as the token request head exists
```
Guarder.builder(GuarderType.ANNOTATION)
		.pattern("org.nico.trap.controller")
		.preventer(caller -> {
			return caller.getRequest().getHeader("token") == null 
					? Result.notpass("Token not exist") 
					: Result.pass();
		})
```
As can be seen from the above example, defender's method returns two types: pass and notpass, and the corresponding instance is obtained through the static method of Result
 - ``Result.notpass(Object o)`` indicates that the defense is not breached. This request is illegal. The passed parameter o represents the prompt message returned to the requester.
 - ``Result.pass()`` represents a legitimate request


