package cz.sefira.msaas_be_mock.service;

import cz.sefira.msaas_be_mock.exception.ScanNotFoundException;
import cz.sefira.msaas_be_mock.model.ResultResponse;
import cz.sefira.msaas_be_mock.model.ResultSet;
import cz.sefira.msaas_be_mock.model.Status;
import cz.sefira.msaas_be_mock.model.StatusResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.function.BiFunction;

@Service
@AllArgsConstructor
public class GetService implements CommunicateWithMsaas {

  @Getter
  private final ConfigurationService configurationService;
  private final ResultService resultService;

  public StatusResponse processStatus(String scanId) {
    checkMsaas500Errors();
    ResultSet resultSet = getResultSet(scanId);
    return getStatusResponse(resultSet);
  }

  public ResultResponse processResult(String scanId) {
    checkMsaas500Errors();
    ResultSet resultSet = getResultSet(scanId);
    return getResultResponse(resultSet);
  }

  private ResultSet getResultSet(String scanId) {
    ResultSet resultSet = resultService.get(scanId);
    if (resultSet == null) {
      throw new ScanNotFoundException();
    }
    return resultSet;
  }

  private StatusResponse getStatusResponse(ResultSet resultSet) {
    StatusResponse statusResponse = checkAndGetUncompleted(resultSet, StatusResponse::new);
    if (statusResponse != null) {
      return statusResponse;
    }
    return new StatusResponse(resultSet.scanId(), resultSet.finalStatus(), !configurationService.getValidationFailed());
  }

  private ResultResponse getResultResponse(ResultSet resultSet) {
    ResultResponse resultResponse = checkAndGetUncompleted(resultSet, ResultResponse::new);
    if (resultResponse != null) {
      return resultResponse;
    }
    return new ResultResponse(resultSet.scanId(), resultSet.finalStatus(), !configurationService.getValidationFailed(),
        new Date(resultSet.expectedCompletionTime()), configurationService.getProviderResults());
  }

  private <T> T checkAndGetUncompleted(ResultSet resultSet, BiFunction<String, Status, T> constructor) {
    String scanId = resultSet.scanId();
    if (!resultSet.isCompleted()) {
      return constructor.apply(scanId, Status.IN_PROGRESS);
    }
    Status finalStatus = resultSet.finalStatus();
    if (finalStatus != Status.DONE) {
      return constructor.apply(scanId, finalStatus);
    }
    return null;
  }
}
