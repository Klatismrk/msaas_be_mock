package cz.sefira.msaas_be_mock.service;

import cz.sefira.msaas_be_mock.model.Configuration;
import cz.sefira.msaas_be_mock.model.ProviderResult;
import cz.sefira.msaas_be_mock.model.Status;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
@Service
public class ConfigurationService {

  Configuration configuration = new Configuration();

  public void setConfiguration(Configuration changes) {
    if (changes == null) return;
    if (changes.getFinalStatus() != null) configuration.setFinalStatus(changes.getFinalStatus());
    if (changes.getDuration() != null) configuration.setDuration(changes.getDuration());
    if (changes.getMaxFileSize() != null) configuration.setMaxFileSize(changes.getMaxFileSize());
    if (changes.getValidationFailed() != null) configuration.setValidationFailed(changes.getValidationFailed());
    if (changes.getMsaasNotReachable() != null) configuration.setMsaasNotReachable(changes.getMsaasNotReachable());
    if (changes.getMsaasInternalError() != null) configuration.setMsaasInternalError(changes.getMsaasInternalError());
    if (changes.getInternalSaveScan500() != null) configuration.setInternalSaveScan500(changes.getInternalSaveScan500());
    if (changes.getProviderResults() != null && !changes.getProviderResults().isEmpty()) configuration.setProviderResults(changes.getProviderResults());
  }

  public Status getFinalStatus() {
    return configuration.getFinalStatus();
  }

  public Long getDuration() {
    return configuration.getDuration();
  }

  public Integer getMaxFileNameSize() {
    return configuration.getMaxFileNameSize();
  }

  public Integer getMaxMimeTypeSize() {
    return configuration.getMaxMimeTypeSize();
  }

  public Long getMaxFileSize() {
    return configuration.getMaxFileSize();
  }

  public Boolean getValidationFailed() {
    return configuration.getValidationFailed();
  }

  public Boolean getMsaasNotReachable() {
    return configuration.getMsaasNotReachable();
  }

  public Boolean getMsaasInternalError() {
    return configuration.getMsaasInternalError();
  }

  public Boolean getInternalSaveScan500() {
    return configuration.getInternalSaveScan500();
  }

  public List<ProviderResult> getProviderResults() {
    return configuration.getProviderResults();
  }

}
