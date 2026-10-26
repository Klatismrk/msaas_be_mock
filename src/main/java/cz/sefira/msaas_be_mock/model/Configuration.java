package cz.sefira.msaas_be_mock.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Configuration {

  private Status finalStatus = Status.DONE;
  private Long duration = 5000L;
  private Long maxFileSize = 1000000L;
  private Boolean validationFailed = false;
  private Boolean msaasNotReachable = false;
  private Boolean msaasInternalError = false;
  private Boolean internalSaveScan500 = false;
  private List<ProviderResult> providerResults = List.of(
      new ProviderResult("AV1", "Antivirus 1", true),
      new ProviderResult("AV2", "Antivirus 2", null),
      new ProviderResult("AV3", "Antivirus 3", true)
  );

  private final Integer maxFileNameSize = 255;
  private final Integer maxMimeTypeSize = 50;

}
