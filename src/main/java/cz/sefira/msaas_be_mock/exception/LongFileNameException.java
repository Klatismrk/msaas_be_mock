package cz.sefira.msaas_be_mock.exception;

import cz.sefira.msaas_be_mock.model.ErrorSource;
import cz.sefira.msaas_be_mock.model.ErrorType;

public class LongFileNameException extends MsaasPostException {

  public LongFileNameException() {
    super(1, ErrorType.VALIDATION, ErrorSource.FILE_NAME, "Dlouhý název souboru");
  }
}
