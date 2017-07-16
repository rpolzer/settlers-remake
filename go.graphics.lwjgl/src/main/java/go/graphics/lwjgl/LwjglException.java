package go.graphics.lwjgl;

public class LwjglException extends RuntimeException {

	private static final long serialVersionUID = 4676069041703259070L;

	public LwjglException() {
	}

	public LwjglException(String message) {
		super(message);
	}

	public LwjglException(Throwable cause) {
		super(cause);
	}

	public LwjglException(String message, Throwable cause) {
		super(message, cause);
	}

	public LwjglException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
