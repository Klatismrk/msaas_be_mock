package cz.sefira.msaas_be_mock.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class StatusResponse {

  private final String scanId;
  private final Status status;
  private final Boolean valid;

  public StatusResponse(String scanId, Status status) {
    this(scanId, status, null);
    if (status == Status.DONE) {
      throw new IllegalArgumentException("When status id DONE, there should be field valid as well");
    }
  }
}
