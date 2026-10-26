package cz.sefira.msaas_be_mock.controller;

import cz.sefira.msaas_be_mock.model.Configuration;
import cz.sefira.msaas_be_mock.service.ConfigurationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigurationController {

  private final ConfigurationService configurationService;

  public ConfigurationController(ConfigurationService configurationService) {
    this.configurationService = configurationService;
  }

  @GetMapping("/configuration")
  public Configuration getConfiguration() {
    return configurationService.getConfiguration();
  }

  @PostMapping("/configuration")
  public Configuration setConfiguration(@RequestBody Configuration payload) {
    configurationService.setConfiguration(payload);
    return configurationService.getConfiguration();
  }

}
