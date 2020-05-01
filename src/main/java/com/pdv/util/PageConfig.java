package com.pdv.util;

import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class PageConfig implements WebMvcConfigurer {
	   @Override
	    public void addViewControllers(ViewControllerRegistry registry) {
	        registry.addViewController("/home").setViewName("home");
	        registry.addViewController("/").setViewName("home");
	    }

	    @Bean
	    public RestTemplate restTemplate() {
	        return new RestTemplate();
	    }

	    // cache
	    @Bean
	    public CacheManager cacheManager() {
	        return new ConcurrentMapCacheManager();
	    }


}
