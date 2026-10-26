package cz.sefira.msaas_be_mock.exception;

import cz.sefira.msaas_be_mock.model.ErrorSource;
import cz.sefira.msaas_be_mock.model.ErrorType;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MsaasException extends RuntimeException {

  private final HttpStatus httpStatus;
  private final int code;
  private final ErrorType errorType;
  private final ErrorSource source;

  public MsaasException(HttpStatus httpStatus, int code, ErrorType errorType, ErrorSource source, String message) {
    super(message);
    this.httpStatus = httpStatus;
    this.code = code;
    this.errorType = errorType;
    this.source = source;
  }
}
