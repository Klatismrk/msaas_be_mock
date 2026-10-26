package cz.sefira.msaas_be_mock.controller;

import cz.sefira.msaas_be_mock.exception.*;
import cz.sefira.msaas_be_mock.model.ErrorResponse;
import cz.sefira.msaas_be_mock.model.Errors;
import cz.sefira.msaas_be_mock.model.Meta;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MsaasException.class)
  public ResponseEntity<ErrorResponse> handleErrors(MsaasException exception) {
    ErrorResponse body = new ErrorResponse(List.of(
        new Errors(exception.getCode(), exception.getMessage(), exception.getSource(),
            new Meta(exception.getErrorType()))));
    return ResponseEntity
        .status(exception.getHttpStatus())
        .body(body);
  }

  @ExceptionHandler(InternalServerErrorException.class)
  public ResponseEntity<String> handleInternalServerError(InternalServerErrorException exception) {
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(exception.getMessage());
  }

  @ExceptionHandler(ServiceUnavailableException.class)
  public ResponseEntity<String> handleServiceUnavailable(ServiceUnavailableException exception) {
    return ResponseEntity
        .status(HttpStatus.SERVICE_UNAVAILABLE)
        .body(exception.getMessage());
  }

  @ExceptionHandler(InputValidation.class)
  public ResponseEntity<String> handleInputValidation(InputValidation exception) {
    return ResponseEntity
        .status(HttpStatus.NOT_ACCEPTABLE)
        .body(exception.getMessage());
  }
}
