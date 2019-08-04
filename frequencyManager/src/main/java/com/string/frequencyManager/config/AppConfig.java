package com.string.frequencyManager.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * Configurations
 * 
 * @author Kamna
 * @version 1.0
 * @see com.string.frequencyManager.config.AppConfig.java
 */

@Data
@Component
@PropertySource("classpath:application.properties")
public class AppConfig {

	@Value("${log.dir}")
    private String logFolder;
	
	@Value("${log.file.format}")
	private String logFileFormat;
	
	@Value("${log.file.extension}")
	private String logFileExt;
	
	@Value("${log.file.string.count}")
	private int count;
}
