package cz.sefira.msaas_be_mock.exception;

public class InternalServerErrorException extends RuntimeException {

  public InternalServerErrorException(String message) {
    super(message);
  }
}
