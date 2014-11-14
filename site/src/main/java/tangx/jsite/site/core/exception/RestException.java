package tangx.jsite.site.core.exception;

import org.springframework.http.HttpStatus;

/**
 * 专用于Restful WebService的异常.
 * @author tangx
 *
 */
public class RestException extends RuntimeException {

	private static final long serialVersionUID = 3350459903903171413L;
	
	public HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

	public RestException() {
	}

	public RestException(HttpStatus status) {
		this.status = status;
	}

	public RestException(String message) {
		super(message);
	}

	public RestException(HttpStatus status, String message) {
		super(message);
		this.status = status;
	}
}
