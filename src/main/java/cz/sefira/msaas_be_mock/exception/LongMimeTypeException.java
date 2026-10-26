package cz.sefira.msaas_be_mock.exception;

import cz.sefira.msaas_be_mock.model.ErrorSource;
import cz.sefira.msaas_be_mock.model.ErrorType;

public class LongMimeTypeException extends MsaasPostException {

  public LongMimeTypeException() {
    super(2, ErrorType.VALIDATION, ErrorSource.REQUIRED_MIME_TYPE, "Dlouhý requiredMimeType");
  }
}
