package cz.sefira.msaas_be_mock.model;

public record ResultSet(String scanId, Status finalStatus, long duration, long created) {
  public long expectedCompletionTime() {
    return created + duration;
  }

  public boolean isCompleted() {
    return expectedCompletionTime() <= System.currentTimeMillis();
  }
}
