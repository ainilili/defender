package org.nico.defender;

import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class DefenderInitialized implements CommandLineRunner, ApplicationContextAware{

	private static ApplicationContext applicationContext;

	@Override
	public void run(String... args) throws Exception {
		Defender.getInstance().initialize();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if(DefenderInitialized.applicationContext == null) {
			DefenderInitialized.applicationContext = applicationContext;
		}
	}

	public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Object getBean(String name){
        return getApplicationContext().getBean(name);
    }

    public static <T> T getBean(Class<T> clazz){
        return getApplicationContext().getBean(clazz);
    }

    public static <T> T getBean(String name,Class<T> clazz){
        return getApplicationContext().getBean(name, clazz);
    }
}
