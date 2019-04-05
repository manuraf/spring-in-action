package it.ifm.springinaction.licenses.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import it.ifm.springinaction.licenses.model.License;
import it.ifm.springinaction.licenses.services.LicenseService;


@RestController
@RequestMapping(value = "v1/organizations/{organizationId}/licenses")
public class LicenseServiceController {
	
	@Autowired
	private LicenseService licenseService;
	
	@RequestMapping(value="/",method = RequestMethod.GET)
    public List<License> getLicenses( @PathVariable("organizationId") String organizationId) {
        return licenseService.getLicensesByOrg(organizationId);
    }
	
	@RequestMapping(value = "/{licenseId}", method = RequestMethod.GET)
	public License getLicense(@PathVariable("organizationId") String organizationId,
			@PathVariable("licenseId") String licenseId){
		return licenseService.getLicense(organizationId, licenseId, "rest");
	}
	
	@RequestMapping(value="/{licenseId}/{clientType}",
			method = RequestMethod.GET)
	public License getLicensesWithClient(
			@PathVariable("organizationId") String organizationId,
			@PathVariable("licenseId") String licenseId,
			@PathVariable("clientType") String clientType) {
		
		return licenseService.getLicense(organizationId, licenseId, clientType);
	}

}
