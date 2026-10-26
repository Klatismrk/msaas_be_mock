package cz.sefira.msaas_be_mock.controller;

import cz.sefira.msaas_be_mock.service.ResultService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResultController {

  private final ResultService resultService;

  public ResultController(ResultService resultService) {
    this.resultService = resultService;
  }

  @GetMapping("/clear-map")
  public String clearMap() {
    resultService.clear();
    return "Map cleared";
  }

}
