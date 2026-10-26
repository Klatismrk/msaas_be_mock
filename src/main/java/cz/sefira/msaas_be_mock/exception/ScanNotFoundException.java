package cz.sefira.msaas_be_mock.exception;

import cz.sefira.msaas_be_mock.model.ErrorSource;
import cz.sefira.msaas_be_mock.model.ErrorType;

public class ScanNotFoundException extends MsaasGetException {

  public ScanNotFoundException() {
    super(3, ErrorType.VALIDATION, ErrorSource.SCAN_ID, "Scan nenalezen");
  }
}
