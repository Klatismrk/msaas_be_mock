package cz.sefira.msaas_be_mock.service;

import cz.sefira.msaas_be_mock.exception.*;
import cz.sefira.msaas_be_mock.model.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PostService implements CommunicateWithMsaas {

  @Getter
  private final ConfigurationService configurationService;
  private final ResultService resultService;

  public void validate(Base64Payload payload) throws InputValidation {
    if (payload.getFileName() == null || payload.getData() == null) {
      throw new InputValidation("FileName or Data missing");
    }
  }

  public void validate(S3Payload payload) throws InputValidation {
    if (payload.getFileName() == null || payload.getUrl() == null) {
      throw new InputValidation("FileName or Url missing");
    }
  }

  public ValidPostResponse process(ScanPayloadBase payload, String mimeType, long fileSize) throws MsaasPostException {
    checkMsaas500Errors();
    if (payload instanceof Base64Payload base64Payload) {
      processBase64(base64Payload.getData(), fileSize);
    }
    internalValidation(payload.getFileName(), mimeType);
    if (payload instanceof S3Payload s3Payload) {
      processSSS(s3Payload.getUrl(), fileSize);
    }
    String scanId = internalSave();
    return new ValidPostResponse(scanId);
  }

  private void processBase64(String data, long fileSize) {
    final byte[] fileData;
    try {
      fileData = decodeBase64(data);
    } catch (IllegalArgumentException e) {
      throw new CanNotDecodeBase64Exception();
    }
    long maxSize = configurationService.getMaxFileSize();
    if ((fileSize != 0 && fileSize > maxSize) ||
        fileData.length > maxSize) {
      throw new FileTooLargeException(202, ErrorSource.DATA);
    }
  }

  private void processSSS(SSSURL url, long fileSize) {
    switch (url) {
      case UNREACHABLE -> throw new SSSUnreachableException();
      case NOT_FOUND -> throw new FileNotFoundException();
      case UNAUTHORIZED -> throw new FileUnauthorizeException();
      case VALID -> {
        if (fileSize > configurationService.getMaxFileSize()) {
          throw new FileTooLargeException(304, ErrorSource.FILE_URL);
        }
      }
      default -> throw new IllegalArgumentException("Not implemented");
    }
  }

  private void internalValidation(String fileName, String mimeType) throws MsaasPostException {
    if (fileName.length() > configurationService.getMaxFileNameSize()) {
      throw new LongFileNameException();
    }
    if (mimeType.length() > configurationService.getMaxMimeTypeSize()) {
      throw new LongMimeTypeException();
    }
  }

  private String internalSave() {
    if (configurationService.getInternalSaveScan500()) {
      throw new InternalServerErrorException("Error during internal-save-scan-request-input");
    }
    ResultSet resultSet = new ResultSet(generateUUID(), configurationService.getFinalStatus(), configurationService.getDuration(), System.currentTimeMillis());
    resultService.add(resultSet);
    return resultSet.scanId();
  }

  private byte[] decodeBase64(String base64) {
    return Base64.getDecoder().decode(base64);
  }

  private String generateUUID() {
    return UUID.randomUUID().toString();
  }

}
