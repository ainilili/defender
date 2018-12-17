# Defender

[![Build Status](https://travis-ci.org/ainilili/defender.svg?branch=master)](https://travis-ci.org/ainilili/defender.svg?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.smallnico/defender/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.smallnico/defender)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

Defender is a lightweight, flexible, and highly available permission framework that fully embraces spring-boot.If we need to make it easier to add permission management to the service on a daily basis, Defender is the Defender!

![](https://github.com/ainilili/defender/blob/master/PROCESS.jpg)

It eliminates the need to repeatedly write custom annotations and facets, and allows you to flexibly specify different patterns of defense networks by simply calling a simple API.

## Quick start
Defender is easy to deploy in two steps, make sure your service USES the spring-boot technology stack before using it, and that you introduce spring-boot-starter aop and spring-boot-starter web modules.
#### Dependency
```
<dependency>
	<groupId>com.smallnico</groupId>
	<artifactId>defender</artifactId>
	<version>${defender.version}</version>
</dependency>
```
#### Configuration
```
@Configuration
@EnableDefender("* org.nico.trap.controller..*.*(..)")
public class DefenderTestConfig {
	@Bean
	public Defender init(){
		return Defender.getInstance()
				.registry(Guarder.builder(GuarderType.URI)
						.pattern("POST /user")
						.preventer(caller -> {
							return caller.getRequest().getHeader("token") == null 
								? Result.pass() : Result.notpass("error");
						}))
				.ready();
	}
}
```
## Advance
 - [中文文档](https://github.com/ainilili/defender/blob/master/DOC_CN.md)
 - [English Document](https://github.com/ainilili/defender/blob/master/DOC_EN.md)

## Contributing
 - [How to Contribute](https://github.com/ainilili/defender/blob/master/CONTRIBUTING.md)
