package cz.sefira.msaas_be_mock.exception;

import cz.sefira.msaas_be_mock.model.ErrorSource;
import cz.sefira.msaas_be_mock.model.ErrorType;
import org.springframework.http.HttpStatus;

public class MsaasGetException extends MsaasException {

  public MsaasGetException(int code, ErrorType errorType, ErrorSource source, String message) {
    super(HttpStatus.NOT_FOUND, code, errorType, source, message);
  }
}
