package com.github.tx.jsite.core.exception;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;



/**
 * 全局异常处理（只处理方法未处理的异常，如果方法try catch了所有异常则不会进入）
 * 
 * 一般来说，异常应在service或controller中进行处理。根据类型转换为ServiceException或RestException抛出
 * 
 * @author tangx
 * 
 */
@ControllerAdvice
public class GlobalExceptionHandler {
	
	private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(Exception.class)
	Object handleException(HttpServletRequest request, Exception ex) {
		logger.error("Requested URL:{},Exception:{}\n", request.getRequestURL(), ex.getMessage());
		// ajax异常处理
		if (request.getHeader("x-requested-with") != null
				&& "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"))) {
			return new ResponseEntity<String>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		// rest异常处理
		if (ex instanceof RestException) {
			return handleRestException((RestException)ex, request);
		}
		// 业务异常处理
		if (ex instanceof ServiceException) {
			return handleServiceException((ServiceException) ex);
		}
		// 默认处理
		return handleDefaultException(ex);
	}

	
	private ModelAndView handleServiceException(ServiceException ex) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("error/500");
		// modelAndView.addObject("name", ex.getClass().getSimpleName());
		// modelAndView.addObject("user", userDao.readUserName());
		return modelAndView;
	}
	
	private ResponseEntity<?> handleRestException(RestException ex, HttpServletRequest request) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		Map<String, Object> jsonException = new HashMap<String, Object>();
		jsonException.put("code", 1);
		jsonException.put("msg", ex.getMessage());
		return new ResponseEntity<Object>(jsonException, headers, ex.status);
	}
	
	private ModelAndView handleDefaultException(Exception ex) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("error/500");
		return modelAndView;
	}

}
