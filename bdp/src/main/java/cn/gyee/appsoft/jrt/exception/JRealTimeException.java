package cn.gyee.appsoft.jrt.exception;

public class JRealTimeException extends Exception
{
  private static final long serialVersionUID = 8219280445126564147L;

  public JRealTimeException()
  {
  }

  public JRealTimeException(String message, Throwable cause)
  {
    super(message, cause);
  }

  public JRealTimeException(String message) {
    super(message);
  }

  public JRealTimeException(Throwable cause) {
    super(cause);
  }
}
