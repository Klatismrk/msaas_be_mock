package cz.sefira.msaas_be_mock.service;

import cz.sefira.msaas_be_mock.exception.InternalServerErrorException;
import cz.sefira.msaas_be_mock.exception.ServiceUnavailableException;

public interface CommunicateWithMsaas {

  ConfigurationService getConfigurationService();

  default void checkMsaas500Errors() {
    if (getConfigurationService().getMsaasNotReachable()) {
      throw new ServiceUnavailableException("MSaaS is not reachable");
    }
    if (getConfigurationService().getMsaasInternalError()) {
      throw new InternalServerErrorException("MSaaS internal error");
    }
  }

}
