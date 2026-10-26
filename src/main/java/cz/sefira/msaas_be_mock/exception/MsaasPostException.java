package cz.sefira.msaas_be_mock.exception;

import cz.sefira.msaas_be_mock.model.ErrorSource;
import cz.sefira.msaas_be_mock.model.ErrorType;
import org.springframework.http.HttpStatus;

public class MsaasPostException extends MsaasException {

  public MsaasPostException(int code, ErrorType errorType, ErrorSource source, String message) {
    super(HttpStatus.BAD_REQUEST, code, errorType, source, message);
  }
}
