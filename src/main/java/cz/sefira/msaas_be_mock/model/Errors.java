package cz.sefira.msaas_be_mock.model;

public record Errors(int code, String detail, ErrorSource source, Meta meta) {

}
