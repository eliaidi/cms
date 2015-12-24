package com.wk.cms.shiro.filters;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wk.cms.controller.vo.Message;

public class MyFormAuthenticationFilter extends FormAuthenticationFilter {

	private static final Logger log = LoggerFactory.getLogger(MyFormAuthenticationFilter.class);
	@Override
	protected void setFailureAttribute(ServletRequest request,
			AuthenticationException ae) {
		request.setAttribute(getFailureKeyAttribute(), ae);
	}
	
	@Override
	protected boolean onLoginSuccess(AuthenticationToken token,
			Subject subject, ServletRequest request, ServletResponse response)
			throws Exception {
		
		if(isAjax(request)){
			setAjaxResponse(response);
			writeJSONObject(response,new Message(true, "login success!", ""));
			
			return false;
		}
		return super.onLoginSuccess(token, subject, request, response);
	}
	
	@Override
	protected boolean onLoginFailure(AuthenticationToken token,
			AuthenticationException e, ServletRequest request,
			ServletResponse response) {
		if(isAjax(request)){
			setAjaxResponse(response);
			try {
				writeJSONObject(response,new Message(false, e.getMessage(), ""));
			} catch (Exception e1) {
				log.error("返回登陆失败JSON数据失败！", e1);
			}
			return false;
		}
		return super.onLoginFailure(token, e, request, response);
	}
	
	private void writeJSONObject(ServletResponse response,Object obj) throws Exception {
		HttpServletResponse res = (HttpServletResponse) response;
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(res.getOutputStream(), obj);
	}

	private void setAjaxResponse(ServletResponse response) {
		HttpServletResponse res = (HttpServletResponse) response;
		res.setContentType("application/json;charset=UTF-8");
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request,
			ServletResponse response) throws Exception {
		
		if (isLoginRequest(request, response)) {
            if (isLoginSubmission(request, response)) {
                if (log.isTraceEnabled()) {
                    log.trace("Login submission detected.  Attempting to execute login.");
                }
                
                return executeLogin(request, response);
            } else {
                if (log.isTraceEnabled()) {
                    log.trace("Login page view.");
                }
                //allow them to see the login page ;)
                return true;
            }
        } else {
            if (log.isTraceEnabled()) {
                log.trace("Attempting to access a path which requires authentication.  Forwarding to the " +
                        "Authentication url [" + getLoginUrl() + "]");
            }

            saveRequestAndRedirectToLogin(request, response);
            return false;
        }
	}

	protected boolean isAjax(ServletRequest request) {
		
		HttpServletRequest req = (HttpServletRequest) request;
		
		return "XMLHttpRequest".equalsIgnoreCase(req.getHeader("X-Requested-With"));
	}
	
}
