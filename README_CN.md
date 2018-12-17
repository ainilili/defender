# Defender

[![Build Status](https://travis-ci.org/ainilili/defender.svg?branch=master)](https://travis-ci.org/ainilili/defender.svg?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.smallnico/defender/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.smallnico/defender/)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

``defender``是一款全面拥抱``spring-boot``的轻量级，高灵活，高可用的权限框架。如果日常中我们需要更加便捷的对服务增加权限管理，那么``defender``正合适！

![](https://github.com/ainilili/defender/blob/master/PROCESS.jpg)

它可以免除我们重复编写自定义注解和切面，只需要调用简单的API即可灵活的指定不同模式的防御网络。

## 快速开始
只需两步即可轻松部署``defender``，在使用之前，请确保您的服务使用的是``spring-Boot``技术栈，并且需要引入``spring-boot-starter-aop``和``spring-boot-starter-web``模块。
#### 依赖
```
<dependency>
	<groupId>com.smallnico</groupId>
	<artifactId>defender</artifactId>
	<version>${defender.version}</version>
</dependency>
```
#### 配置
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
## 进阶
 - [中文文档](https://github.com/ainilili/defender/blob/master/DOC_CN.md)
 - [English Document](https://github.com/ainilili/defender/blob/master/DOC_EN.md)
 
## 贡献
 - [如何贡献](https://github.com/ainilili/defender/blob/master/CONTRIBUTING.md)
