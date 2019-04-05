package it.ifm.springinaction.licenses;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.web.client.RestTemplate;

import it.ifm.springinaction.licenses.services.LicenseService;
import it.ifm.springinaction.licenses.utils.UserContextInterceptor;

@SpringBootApplication
@RefreshScope
@EnableDiscoveryClient
@EnableFeignClients
@EnableEurekaClient
@EnableCircuitBreaker
public class LicensingServiceApplication {
	
	private static final Logger logger = LoggerFactory.getLogger(LicensingServiceApplication.class);
	
//	@LoadBalanced
//	@Bean
//	public RestTemplate getRestTemplate(){
//		RestTemplate template = new RestTemplate();
//		List interceptors = template.getInterceptors();
//		if (interceptors==null){
//			template.setInterceptors(
//					Collections.singletonList(
//							new UserContextInterceptor()));
//		}
//		else{
//			interceptors.add(new UserContextInterceptor());
//			template.setInterceptors(interceptors);
//		}
//		return template;
//	}
	
//    @Bean
//    @ConditionalOnProperty(prefix = "security.oauth2.client", value = "grant-type", havingValue = "password")
//    public OAuth2RestTemplate oauth2RestTemplate(OAuth2ClientContext oauth2ClientContext,
//                                                 OAuth2ProtectedResourceDetails details) {
//        return new OAuth2RestTemplate(details, oauth2ClientContext);
//    }
    
    @Bean
    public OAuth2RestTemplate oauth2RestTemplate(OAuth2ProtectedResourceDetails details) {
      OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(details);

      logger.debug("Begin OAuth2RestTemplate: getAccessToken");
      /* To validate if required configurations are in place during startup */
      oAuth2RestTemplate.getAccessToken();
      logger.debug("End OAuth2RestTemplate: getAccessToken");
      return oAuth2RestTemplate;
    }

	public static void main(String[] args) {
		SpringApplication.run(LicensingServiceApplication.class, args);
	}

}
