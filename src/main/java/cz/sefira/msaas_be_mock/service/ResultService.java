package cz.sefira.msaas_be_mock.service;

import cz.sefira.msaas_be_mock.model.ResultSet;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ResultService {

  private final Map<String, ResultSet> resultMap = new HashMap<>();

  public void clear() {
    resultMap.clear();
  }

  public void add(ResultSet resultSet) {
    resultMap.put(resultSet.scanId(), resultSet);
  }

  public ResultSet get(String scanId) {
    return resultMap.get(scanId);
  }

}
