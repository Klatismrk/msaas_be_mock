package cz.sefira.msaas_be_mock.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Base64Payload extends ScanPayloadBase{

  public String data;
}
