package cz.sefira.msaas_be_mock.exception;

import cz.sefira.msaas_be_mock.model.ErrorSource;
import cz.sefira.msaas_be_mock.model.ErrorType;

public class SSSUnreachableException extends MsaasPostException {

  public SSSUnreachableException() {
    super(301, ErrorType.VALIDATION, ErrorSource.FILE_URL, "Zdrojové s3 úložiště není dostupné");
  }
}
