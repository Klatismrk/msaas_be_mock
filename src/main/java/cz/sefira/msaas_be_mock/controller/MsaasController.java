package cz.sefira.msaas_be_mock.controller;

import cz.sefira.msaas_be_mock.model.*;
import cz.sefira.msaas_be_mock.service.GetService;
import cz.sefira.msaas_be_mock.service.PostService;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class MsaasController {

  private static final String X_MIME_TYPE = "X-Mime-Type";
  private static final String X_FILE_SIZE = "X-File-Size";
  private static final String APPLICATION_PDF = "application/pdf";

  GetService getService;
  PostService postService;

  @GetMapping("/files/{scanId}/status")
  public ResponseEntity<StatusResponse> getStatus(@PathVariable String scanId) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(getService.processStatus(scanId));
  }

  @GetMapping("/files/{scanId}/result")
  public ResponseEntity<ResultResponse> getResult(@PathVariable String scanId) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(getService.processResult(scanId));
  }

  @PostMapping("/files/scan-base64")
  public ResponseEntity<ValidPostResponse> scanBase64(@RequestBody Base64Payload payload,
                                                      @RequestHeader(value = X_MIME_TYPE, defaultValue = APPLICATION_PDF) String mimeType,
                                                      @RequestHeader(value = X_FILE_SIZE, defaultValue = "0") int fileSize) {
    postService.validate(payload);
    return processPost(payload, mimeType, fileSize);
  }

  @PostMapping("/files/scan-s3")
  public ResponseEntity<ValidPostResponse> scanBase64(@RequestBody S3Payload payload,
                           @RequestHeader(value = X_MIME_TYPE, defaultValue = APPLICATION_PDF) String mimeType,
                           @RequestHeader(value = X_FILE_SIZE, defaultValue = "0") int fileSize) {
    postService.validate(payload);
    return processPost(payload, mimeType, fileSize);
  }

  private @NonNull ResponseEntity<ValidPostResponse> processPost(ScanPayloadBase payload, String mimeType, int fileSize) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(postService.process(payload, mimeType, fileSize));
  }
}
