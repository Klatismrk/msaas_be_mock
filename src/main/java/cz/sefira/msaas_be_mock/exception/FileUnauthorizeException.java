package cz.sefira.msaas_be_mock.exception;

import cz.sefira.msaas_be_mock.model.ErrorSource;
import cz.sefira.msaas_be_mock.model.ErrorType;

public class FileUnauthorizeException extends MsaasPostException {

  public FileUnauthorizeException() {
    super(303, ErrorType.VALIDATION, ErrorSource.FILE_URL, "Zdrojový soubor není přístupný");
  }
}
