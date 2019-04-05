package it.ifm.springinaction.organization.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.ifm.springinaction.organization.model.Organization;

import java.util.Optional;

@Repository
public interface OrganizationRepository extends CrudRepository<Organization,String>  {
	
    public Optional<Organization> findById(String organizationId);
}
