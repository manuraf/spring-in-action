package it.ifm.springinaction.licenses.config;

import java.security.Principal;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springboot.KeycloakBaseSpringBootConfiguration;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@KeycloakConfiguration
@EnableConfigurationProperties(KeycloakSpringBootProperties.class)
public class SecurityConfig extends KeycloakWebSecurityConfigurerAdapter {

	@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
  
        KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
        auth.authenticationProvider(keycloakAuthenticationProvider);
    }
 
    @Bean
    public KeycloakSpringBootConfigResolver KeycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }
 
    /* this defines that we want to use the Spring Boot properties file support instead of the default keycloak.json */
    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }
 
    @Override
	protected void configure(HttpSecurity http) throws Exception {
		super.configure(http);

		http //
				.csrf().disable() //
				.authorizeRequests() //
//				.requestMatchers(EndpointRequest.to( //
//						InfoEndpoint.class, //
//						HealthEndpoint.class //
//				))
//				.permitAll() //
//
//				.requestMatchers(EndpointRequest.toAnyEndpoint())
//				.hasRole("HEALTH")
				.antMatchers("/v1/organizations/**")
				.hasRole("USER") //
				.anyRequest().authenticated(); //
		;
	}
    
    
    
    /**
	 * Allows to inject requests scoped wrapper for {@link KeycloakSecurityContext}.
	 *
	 * Returns the {@link KeycloakSecurityContext} from the Spring
	 * {@link ServletRequestAttributes}'s {@link Principal}.
	 * <p>
	 * The principal must support retrieval of the KeycloakSecurityContext, so at
	 * this point, only {@link KeycloakPrincipal} values and
	 * {@link KeycloakAuthenticationToken} are supported.
	 *
	 * @return the current <code>KeycloakSecurityContext</code>
	 */
	@Bean
	@Scope(scopeName = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
	public KeycloakSecurityContext provideKeycloakSecurityContext() {

		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		Principal principal = attributes.getRequest().getUserPrincipal();
		if (principal == null) {
			return null;
		}

		if (principal instanceof KeycloakAuthenticationToken) {
			principal = Principal.class.cast(KeycloakAuthenticationToken.class.cast(principal).getPrincipal());
		}

		if (principal instanceof KeycloakPrincipal) {
			return KeycloakPrincipal.class.cast(principal).getKeycloakSecurityContext();
		}

		return null;
	}

	/**
	 * Ensures the correct registration of KeycloakSpringBootConfigResolver when Keycloaks AutoConfiguration
	 * is explicitly turned off in application.yml {@code keycloak.enabled: false}.
	 */
	@Configuration
	static class CustomKeycloakBaseSpringBootConfiguration extends KeycloakBaseSpringBootConfiguration {
	}
}
