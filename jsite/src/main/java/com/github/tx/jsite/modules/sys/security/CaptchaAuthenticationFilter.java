package com.github.tx.jsite.modules.sys.security;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Component;

/**
 * 验证码登录filter
 * @author tangx
 *
 */
@Component
public class CaptchaAuthenticationFilter extends FormAuthenticationFilter{
	
	//验证码参数名称
	public static final String DEFAULT_CAPTCHA_PARAM = "captcha";
	//登录次数超出allowLoginNum时，是否展示验证码的参数名称
	public static final String DEFAULT_SHOW_CAPTCHA_PARAM = "showCaptcha";
	//登录次数参数名称
	private static final String DEFAULT_LOGIN_NUM_PARAM = "loginNum";
    //允许登录次数，当登录次数大于该数值时，会在页面中显示验证码
    private static final Integer DEFAULT_ALLOW_LOGIN_NUM = 2;
    
    /**
     * 重写父类方法，在shiro执行登录时先对比验证码，正确后再执行原先的登录逻辑，否则直接登录失败
     */
	@Override
	protected boolean executeLogin(ServletRequest request,ServletResponse response) throws Exception {
		
		Session session = getSubject(request, response).getSession();
		//获取登录次数
		Integer number = (Integer) session.getAttribute(DEFAULT_LOGIN_NUM_PARAM);
		
		//首次登录，将该数量记录在session中，否则+1
		if (number == null) {
			number = new Integer(1);
			session.setAttribute(DEFAULT_LOGIN_NUM_PARAM, number);
		}else{
			number++;
			session.setAttribute(DEFAULT_LOGIN_NUM_PARAM, number);
		}
		
		//如果登录次数大于ALLOW_LOGIN_NUM，需要判断验证码是否一致
		if (number > DEFAULT_ALLOW_LOGIN_NUM) {
			//获取当前验证码
			String currentCaptcha = (String) session.getAttribute(DEFAULT_CAPTCHA_PARAM);
			//获取用户输入的验证码
			String submitCaptcha = getCaptcha(request);
			//如果验证码不匹配，登录失败
			if (StringUtils.isEmpty(submitCaptcha) || !StringUtils.equals(currentCaptcha,submitCaptcha.toLowerCase())) {
				return onLoginFailure(this.createToken(request, response), new AccountException("验证码不正确"), request, response);
			}
		
		}
		
		return super.executeLogin(request, response);
	}
	

	/**
	 * 获取用户输入的验证码
	 * 
	 * @param request ServletRequest
	 * 
	 * @return String
	 */
	public String getCaptcha(ServletRequest request) {
		return WebUtils.getCleanParam(request, DEFAULT_CAPTCHA_PARAM);
	}
	
	
	/**
	 * 重写父类方法，当登录失败将异常信息设置到request的attribute中
	 */
	@Override
	protected void setFailureAttribute(ServletRequest request,AuthenticationException ae) {
		if (ae instanceof IncorrectCredentialsException) {
			request.setAttribute(getFailureKeyAttribute(), "密码不正确");
		} else if (ae instanceof AccountException || ae instanceof UnknownAccountException) {
			request.setAttribute(getFailureKeyAttribute(), ae.getMessage());
		} else {
			request.setAttribute(getFailureKeyAttribute(), "服务异常");
		}
	}
	
	/**
	 * 重写父类方法，当登录失败次数大等于ALLOW_LOGIN_NUM时，将显示验证码
	 */
	@Override
	protected boolean onLoginFailure(AuthenticationToken token,AuthenticationException e, ServletRequest request,ServletResponse response) {
		Session session = getSubject(request, response).getSession(false);
		
		Integer number = (Integer) session.getAttribute(DEFAULT_LOGIN_NUM_PARAM);
		
		if (number >= DEFAULT_ALLOW_LOGIN_NUM) {
			session.setAttribute(DEFAULT_SHOW_CAPTCHA_PARAM, true);
		}
		return super.onLoginFailure(token, e, request, response);
	}
	
	/**
	 * 重写父类方法，当登录成功后，重置下一次登录的状态
	 */
	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
		Session session = subject.getSession(false);
		
		session.removeAttribute(DEFAULT_LOGIN_NUM_PARAM);
		session.removeAttribute(DEFAULT_SHOW_CAPTCHA_PARAM);
		
		return super.onLoginSuccess(token, subject, request, response);
	}
	
	/**
	 * 重写父类方法，创建一个自定义的{@link UsernamePasswordTokenExtend}
	 */
	@Override
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
		
		String username = getUsername(request);
	    String password = getPassword(request);
        String host = getHost(request);

	    boolean rememberMe = false;
	    String rememberMeValue = request.getParameter(getRememberMeParam());
	    Integer rememberMeCookieValue = null;
	    //如果提交的rememberMe参数存在值,将rememberMe设置成true
	    if(StringUtils.isNotEmpty(rememberMeValue)) {
	    	rememberMe = true;
	    	rememberMeCookieValue = NumberUtils.toInt(rememberMeValue);
	    }
	    
		return new UsernamePasswordTokenExtend(username, password, rememberMe, host,rememberMeCookieValue);
	}
	
	/**
	 * UsernamePasswordToken扩展，添加一个rememberMeCookieValue字段
	 * 根据该值去设置Cookie的有效时间。
	 * 
	 *
	 */
	@SuppressWarnings("serial")
	protected class UsernamePasswordTokenExtend extends UsernamePasswordToken {
		
		//rememberMe cookie的有效时间
		private Integer rememberMeCookieValue;
		
		public UsernamePasswordTokenExtend() {
			
		}
		
		public UsernamePasswordTokenExtend(String username,String password,boolean rememberMe, String host,Integer rememberMeCookieValue) {
			super(username, password, rememberMe, host);
			this.rememberMeCookieValue = rememberMeCookieValue;
		}

		/**
		 * 获取rememberMe cookie的有效时间
		 * 
		 * @return Integer
		 */
		public Integer getRememberMeCookieValue() {
			return rememberMeCookieValue;
		}

		/**
		 * 设置rememberMe cookie的有效时间
		 * 
		 * @param rememberMeCookieValue cookie的有效时间
		 */
		public void setRememberMeCookieValue(Integer rememberMeCookieValue) {
			this.rememberMeCookieValue = rememberMeCookieValue;
		}
		
		
	}
}