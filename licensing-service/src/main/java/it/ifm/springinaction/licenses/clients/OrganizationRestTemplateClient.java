package it.ifm.springinaction.licenses.clients;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import it.ifm.springinaction.licenses.model.Organization;

@Component
public class OrganizationRestTemplateClient {
//    @Autowired
//    RestTemplate restTemplate;
	
    @Autowired
    private DiscoveryClient discoveryClient;
	
	@Autowired
	OAuth2RestTemplate restTemplate;

    public Organization getOrganization(String organizationId){
    	List<ServiceInstance> instances = discoveryClient.getInstances("organizationservice");

        if (instances.size()==0) return null;
        String serviceUri = String.format("%s/v1/organizations/%s",instances.get(0).getUri().toString(), organizationId);
    
        ResponseEntity<Organization> restExchange =
                restTemplate.exchange(
                		serviceUri,
                        HttpMethod.GET,
                        null, Organization.class, organizationId);

        return restExchange.getBody();
    }
}
