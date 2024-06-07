package com.credibanco.mstest.configurations;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Configuration("SwaggerProperties")
@PropertySource("classpath:application.properties")
public class SwaggerProperties implements Serializable {

	private static final long serialVersionUID = -1710430065264968271L;

	@Value("${api.version}")
	private String apiVersion;

	@Value("${swagger.enabled}")
	private String enabled = "false";

	@Value("${swagger.title}")
	private String title;

	@Value("${swagger.description}")
	private String description;

	@Value("${swagger.useDefaultResponseMessages}")
	private String useDefaultResponseMessages;

	@Value("${swagger.enableUrlTemplating}")
	private String enableUrlTemplating;

	@Value("${swagger.deepLinking}")
	private String deepLinking;

	@Value("${swagger.defaultModelsExpandDepth}")
	private String defaultModelsExpandDepth;

	@Value("${swagger.defaultModelExpandDepth}")
	private String defaultModelExpandDepth;

	@Value("${swagger.displayOperationId}")
	private String displayOperationId;

	@Value("${swagger.displayRequestDuration}")
	private String displayRequestDuration;

	@Value("${swagger.filter}")
	private String filter;

	@Value("${swagger.maxDisplayedTags}")
	private String maxDisplayedTags;

	@Value("${swagger.showExtensions}")
	private String showExtensions;

	@Override
	public String toString() {
		return "SwaggerConfigProperties [apiVersion=" + apiVersion + ", enabled=" + enabled + ", title=" + title
				+ ", description=" + description + ", useDefaultResponseMessages=" + useDefaultResponseMessages
				+ ", enableUrlTemplating=" + enableUrlTemplating + ", deepLinking=" + deepLinking
				+ ", defaultModelsExpandDepth=" + defaultModelsExpandDepth + ", defaultModelExpandDepth="
				+ defaultModelExpandDepth + ", displayOperationId=" + displayOperationId + ", displayRequestDuration="
				+ displayRequestDuration + ", filter=" + filter + ", maxDisplayedTags=" + maxDisplayedTags
				+ ", showExtensions=" + showExtensions + "]";
	}

	
}
