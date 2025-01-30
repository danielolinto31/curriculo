package br.com.curriculo.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:config.properties")
public class PropertySourceConfig {

	@Value("${app.sizeLimitToFileUpload}")
	private String sizeLimitToFileUpload;

	@Value("${app.logo}")
	private String appLogo;

	@Value("${app.name}")
	private String appName;

	@Value("${app.version.number}")
	private String appVersionNumber;

	@Value("${app.version.date}")
	private String appVersionDate;

	@Value("${app.url}")
	private String url;

	@Value("${app.owner}")
	private String appOwner;

	public Long getSizeLimitToFileUpload() {
		return Long.parseLong(sizeLimitToFileUpload);
	}

	public String getAppLogo() {
		return appLogo;
	}

	public String getAppName() {
		return appName;
	}

	public String getAppVersionNumber() {
		return appVersionNumber;
	}

	public String getAppVersionDate() {
		return appVersionDate;
	}

	public String getAppUrl() {
		return url;
	}

	public String getAppOwner() {
		return appOwner;
	}

}
