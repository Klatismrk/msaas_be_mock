package cz.sefira.msaas_be_mock.model;

import lombok.Getter;

@Getter
public enum ErrorSource {

  FILE_NAME("request.body.fileName"),
  FILE_URL("request.body.fileUrl"),
  REQUIRED_MIME_TYPE("request.body.requiredMimeType"),
  SCAN_ID("request.path.scanId"),
  DATA("request.body.data");

  private final String value;

  ErrorSource(String value) {
    this.value = value;
  }


  @Override
  public String toString() {
    return value;
  }
}
