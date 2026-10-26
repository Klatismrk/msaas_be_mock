package cz.sefira.msaas_be_mock.exception;

import cz.sefira.msaas_be_mock.model.ErrorSource;
import cz.sefira.msaas_be_mock.model.ErrorType;

public class CanNotDecodeBase64Exception extends MsaasPostException {

  public CanNotDecodeBase64Exception() {
    super(201, ErrorType.VALIDATION, ErrorSource.DATA,"Nelze dekódovat obsah souboru");
  }
}
