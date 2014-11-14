package tangx.jsite.site.core.exception;

/**
 * Service层公用的Exception.
 * 抛出时会触发事务回滚(需配置Spring事务)
 * @author tangx
 *
 */
public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = 3583566093089790852L;

	public ServiceException() {
		super();
	}

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(Throwable cause) {
		super(cause);
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
