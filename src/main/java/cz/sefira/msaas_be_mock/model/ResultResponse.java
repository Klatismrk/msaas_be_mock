package cz.sefira.msaas_be_mock.model;


import lombok.Getter;

import java.util.*;

@Getter
public class ResultResponse extends StatusResponse {

  private final Date scannedAt;
  private final List<ProviderResult> results;

  public ResultResponse(String scanId, Status status, Boolean valid, Date scannedAt, List<ProviderResult> results) {
    super(scanId, status, valid);
    this.scannedAt = scannedAt;
    this.results = results;
  }

  public ResultResponse(String scanId, Status status) {
    this(scanId, status, null, null, Collections.emptyList());
  }
}
