package config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Context {
	
	private static ApplicationContext context;
	
	public static ApplicationContext getInstance() {
		if (context == null) {
			context = new AnnotationConfigApplicationContext(ConfigCDB.class);
		}
		return context;
	}
	
}
