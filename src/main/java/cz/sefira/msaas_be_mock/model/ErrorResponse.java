package cz.sefira.msaas_be_mock.model;

import java.util.List;

public record ErrorResponse(List<Errors> errors) {

}
