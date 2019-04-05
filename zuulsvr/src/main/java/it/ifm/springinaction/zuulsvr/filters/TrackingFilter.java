package it.ifm.springinaction.zuulsvr.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

@Component
public class TrackingFilter extends ZuulFilter {
	
	private static final int FILTER_ORDER = 1;
	private static final boolean SHOULD_FILTER=true;
	
	private static final Logger logger = LoggerFactory.getLogger(TrackingFilter.class);
	
	@Autowired
	FilterUtils filterUtils;

	@Override
	public Object run() throws ZuulException {
		if (isCorrelationIdPresent()) {
			logger.debug("tmx-correlation-id found in tracking filter: {}.", filterUtils.getCorrelationId());
		}
		else {
			filterUtils.setCorrelationId(generateCorrelationId());
			logger.debug("tmx-correlation-id generated in tracking filter: {}.", filterUtils.getCorrelationId());
		}
		
		RequestContext ctx = RequestContext.getCurrentContext();
		logger.debug("Processing incoming request for {}.",
		ctx.getRequest().getRequestURI());
		return null;
	}
	
	private String generateCorrelationId(){
		return java.util.UUID.randomUUID().toString();
	}
	
	private boolean isCorrelationIdPresent(){
		if (filterUtils.getCorrelationId() !=null){
			return true;
		}
		return false;
	}
	
	private String getOrganizationId(){
		String result="";
		if (filterUtils.getAuthToken()!=null){
			String authToken = filterUtils.getAuthToken().replace("Bearer ","");
			try {
//				Claims claims =	Jwts.parser()
//					.setSigningKey(serviceConfig.getJwtSigningKey()			
//					.getBytes("UTF-8"))
//					.parseClaimsJws(authToken)
//					.getBody();
//				
//				result = (String) claims.get("organizationId");
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		
		return result;
	}

	@Override
	public boolean shouldFilter() {
		return SHOULD_FILTER;
	}

	@Override
	public int filterOrder() {
		return FILTER_ORDER;
	}

	@Override
	public String filterType() {
		return FilterUtils.PRE_FILTER_TYPE;
	}

}
