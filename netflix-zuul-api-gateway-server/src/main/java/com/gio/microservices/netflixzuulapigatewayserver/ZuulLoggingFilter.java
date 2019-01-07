package com.gio.microservices.netflixzuulapigatewayserver;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ZuulLoggingFilter extends ZuulFilter {

	

	@Override
	public Object run() throws ZuulException {
		
		HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
		
		return null;
	}

	@Override
	public String filterType() {
		return "pre";
	}
	
	@Override
	public boolean shouldFilter() {
		return true;
	}
	
	@Override
	public int filterOrder() {
		return 1;
	}

}
