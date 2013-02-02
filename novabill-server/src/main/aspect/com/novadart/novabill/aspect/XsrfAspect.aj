package com.novadart.novabill.aspect;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.novadart.novabill.annotation.Xsrf;
import com.novadart.novabill.service.XsrfTokenService;

public aspect XsrfAspect {
	
	@Autowired
	private XsrfTokenService xsrfTokenService;
	
	public void setXsrfTokenService(XsrfTokenService xsrfTokenService){
		this.xsrfTokenService = xsrfTokenService;
	}
	
	pointcut protectedMethod(Xsrf xsrfAnnotation):
		execution(@Xsrf public * *(..)) && @annotation(xsrfAnnotation);
	
	private boolean canExecute(Xsrf xsrfAnnotation){
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
	    HttpSession session = attr.getRequest().getSession(false);
	    Object token = attr.getRequest().getParameter(xsrfAnnotation.tokenRequestParam());
		return token != null && xsrfTokenService.verifyAndRemoveToken((String)token, session, xsrfAnnotation.tokensSessionField());
	}
	
	Object around(Xsrf xsrfAnnotation): protectedMethod(xsrfAnnotation){
		if(canExecute(xsrfAnnotation))
			return proceed(xsrfAnnotation);
		return null;
	}

}
