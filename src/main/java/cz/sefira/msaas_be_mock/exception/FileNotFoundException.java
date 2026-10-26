package cz.sefira.msaas_be_mock.exception;

import cz.sefira.msaas_be_mock.model.ErrorSource;
import cz.sefira.msaas_be_mock.model.ErrorType;

public class FileNotFoundException extends MsaasPostException {

  public FileNotFoundException() {
    super(302, ErrorType.VALIDATION, ErrorSource.FILE_URL, "Zdrojový soubor neexistuje");
  }
}
