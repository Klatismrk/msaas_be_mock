package cz.sefira.msaas_be_mock.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class S3Payload extends ScanPayloadBase {

  SSSURL url;

}
