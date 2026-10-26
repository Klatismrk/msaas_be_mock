package cz.sefira.msaas_be_mock.exception;

public class InputValidation extends RuntimeException {

  public InputValidation(String message) {
    super(message);
  }
}
