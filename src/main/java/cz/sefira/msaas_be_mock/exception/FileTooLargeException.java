package cz.sefira.msaas_be_mock.exception;

import cz.sefira.msaas_be_mock.model.ErrorSource;
import cz.sefira.msaas_be_mock.model.ErrorType;

public class FileTooLargeException extends MsaasPostException {

  public FileTooLargeException(int code, ErrorSource source) {
    super(code, ErrorType.VALIDATION, source,"Překročena maximální velikost souboru");
  }
}
