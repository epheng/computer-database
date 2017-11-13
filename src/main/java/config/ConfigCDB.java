package config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages= {"dao", "dto", "mapper", "model", "service", "servlet", "tmp", "ui"})
public class ConfigCDB {
	
}
