package cz.sefira.msaas_be_mock.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ScanPayloadBase {

  public String fileName;
  public List<String> archivePwd;

}
