package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.BinaryContentStatus;
import com.sprint.mission.discodeit.event.BinaryContentCreatedEvent;
import com.sprint.mission.discodeit.event.S3UploadFailedEvent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.NotificationService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.InputStream;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Slf4j
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
@Component
public class S3BinaryContentStorage implements BinaryContentStorage {

  private final BinaryContentRepository binaryContentRepository;

  private final String accessKey;
  private final String secretKey;
  private final String region;
  private final String bucket;

  private final S3Client s3Client;
  private final S3Presigner s3Presigner;
  private final Duration expiration;

  private final ApplicationEventPublisher eventPublisher;


  public S3BinaryContentStorage(
      BinaryContentRepository binaryContentRepository,
      @Value("${discodeit.storage.s3.access-key}") String accessKey,
      @Value("${discodeit.storage.s3.secret-key}") String secretKey,
      @Value("${discodeit.storage.s3.region}") String region,
      @Value("${discodeit.storage.s3.bucket}") String bucket,
      @Value("${discodeit.storage.s3.presigned-url-expiration}") Duration expiration,
      ApplicationEventPublisher eventPublisher) {
    this.binaryContentRepository = binaryContentRepository;
    this.eventPublisher = eventPublisher;
    this.accessKey = accessKey;
    this.secretKey = secretKey;
    this.region = region;
    this.bucket = bucket;
    this.expiration = expiration;

    StaticCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(
        AwsBasicCredentials.create(accessKey, secretKey)
    );
    Region awsRegion = Region.of(region);

    this.s3Client = S3Client.builder()
        .region(awsRegion)
        .credentialsProvider(credentialsProvider)
        .build();

    this.s3Presigner = S3Presigner.builder()
        .region(awsRegion)
        .credentialsProvider(credentialsProvider)
        .build();
  }

  @Retryable(
      retryFor = RuntimeException.class,
      maxAttempts = 3,
      backoff = @Backoff(delay = 1000)
  )
  @Override
  public UUID put(UUID binaryContentId, byte[] bytes) {
    try {
      String key = binaryContentId.toString();
      BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId)
          .orElseThrow(() -> new RuntimeException("바이너리 파일 저장 중 문제 발생"));
      PutObjectRequest putReq = PutObjectRequest.builder()
          .bucket(bucket)
          .key(key)
          .contentType(binaryContent.getContentType())
          .build();
      s3Client.putObject(putReq, RequestBody.fromBytes(bytes));
      log.debug("S3 팡리 업로드 완료 - key: {}", key);
      return binaryContentId;
    } catch (Exception e) {
      throw new RuntimeException("S3 업로드 실패", e);
    }
  }

  @Recover
  public UUID recover(RuntimeException ex, UUID contentId, byte[] bytes) {
    Throwable cause = ex;
    while (cause.getCause() != null) {
      cause = cause.getCause();
    }
    String requestId = MDC.get("requestId");
    eventPublisher.publishEvent(
        new S3UploadFailedEvent(requestId, contentId, cause.getMessage())
    );
    return contentId;
  }

  @Override
  public InputStream get(UUID binaryContentId) {
    try {
      String key = binaryContentId.toString();
      GetObjectRequest request = GetObjectRequest.builder()
          .bucket(bucket)
          .key(key)
          .build();
      log.debug("S3 파일 조회 - key: {}", key);
      return s3Client.getObject(request);
    } catch (Exception e) {
      throw new RuntimeException("S3 파일 조회 실패", e);
    }
  }

  @Override
  public ResponseEntity<Void> download(BinaryContentDto metaData) {
    String url = generatePresignedUrl(metaData.id().toString(), metaData.contentType());
    log.info("S3 파일 다운로드 리다이렉트 - ID: {}, 파일명: {}", metaData.id(), metaData.fileName());
    return ResponseEntity.status(HttpStatus.FOUND)
        .header(HttpHeaders.LOCATION, url)
        .build();
  }

  public S3Client getS3Client() {
    return s3Client;
  }

  public String generatePresignedUrl(String key, String contentType) {
    GetObjectRequest objectRequest = GetObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .responseContentType(contentType)
        .responseContentDisposition("attachment")
        .build();

    GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
        .getObjectRequest(objectRequest)
        .signatureDuration(expiration)
        .build();

    PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
    log.debug("Presigned URL 생성 - key: {}", key);
    return presignedRequest.url().toString();
  }
}
