package it.ifm.springinaction.organization.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.ifm.springinaction.organization.model.Organization;
import it.ifm.springinaction.organization.repository.OrganizationRepository;

import java.util.UUID;

@Service
public class OrganizationService {
	
    @Autowired
    private OrganizationRepository orgRepository;

    public Organization getOrg(String organizationId) {
        return orgRepository.findById(organizationId)
        		.orElse(null);
    }

    public void saveOrg(Organization org){
        org.setId( UUID.randomUUID().toString());

        orgRepository.save(org);

    }

    public void updateOrg(Organization org){
        orgRepository.save(org);
    }

    public void deleteOrg(Organization org){
        orgRepository.delete(org);
    }
}
