package com.sprint.mission.discodeit.storage.s3;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Properties;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

public class AWSS3Test {

  private static final String ENV_FILE = ".env";

  private final String accessKey;
  private final String secretKey;
  private final String region;
  private final String bucketName;

  private final S3Client s3Client;
  private final S3Presigner s3Presigner;

  public AWSS3Test() {
    Properties props = loadEnv();
    this.accessKey = props.getProperty("AWS_S3_ACCESS_KEY");
    this.secretKey = props.getProperty("AWS_S3_SECRET_KEY");
    this.region = props.getProperty("AWS_S3_REGION");
    this.bucketName = props.getProperty("AWS_S3_BUCKET");

    AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
    StaticCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(credentials);
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

  private Properties loadEnv() {
    Properties props = new Properties();
    Path envPath = Paths.get(ENV_FILE);

    if (!Files.exists(envPath)) {
      throw new IllegalStateException(".env 파일을 찾을 수 없습니다: " + envPath.toAbsolutePath());
    }

    try (InputStream is = Files.newInputStream(envPath)) {
      props.load(is);
    } catch (IOException e) {
      throw new RuntimeException(".env 파일 로드 실패", e);
    }

    System.out.println("[ENV] region=" + props.getProperty("AWS_REGION")
        + ", bucket=" + props.getProperty("AWS_S3_BUCKET"));
    return props;
  }

  public void testUpload(String localFilePath, String s3Key) {
    System.out.println("\n=== [Upload Test] ===");
    Path filePath = Paths.get(localFilePath);

    if (!Files.exists(filePath)) {
      System.err.println("업로드할 파일이 없습니다: " + filePath.toAbsolutePath());
      return;
    }

    try {
      String contentType = Files.probeContentType(filePath);
      if (contentType == null) {
        contentType = "application/octet-stream";
      }

      PutObjectRequest request = PutObjectRequest.builder()
          .bucket(bucketName)
          .key(s3Key)
          .contentType(contentType)
          .build();

      s3Client.putObject(request, RequestBody.fromFile(filePath));

      System.out.println("업로드 성공!");
      System.out.println("  Bucket : " + bucketName);
      System.out.println("  Key    : " + s3Key);
    } catch (Exception e) {
      System.err.println("업로드 실패: " + e.getMessage());
      e.printStackTrace();
    }
  }

  public void testDownload(String s3Key, String localOutputPath) {
    System.out.println("\n=== [Download Test] ===");

    GetObjectRequest request = GetObjectRequest.builder()
        .bucket(bucketName)
        .key(s3Key)
        .build();

    try (ResponseInputStream<GetObjectResponse> response = s3Client.getObject(request);
        OutputStream out = new FileOutputStream(localOutputPath)) {

      byte[] buffer = new byte[8192];
      int bytesRead;
      long totalBytes = 0;
      while ((bytesRead = response.read(buffer)) != -1) {
        out.write(buffer, 0, bytesRead);
        totalBytes += bytesRead;
      }

      System.out.println("다운로드 성공!");
      System.out.println("  S3 Key     : " + s3Key);
      System.out.println("  저장 경로  : " + Paths.get(localOutputPath).toAbsolutePath());
      System.out.println("  파일 크기  : " + totalBytes + " bytes");
    } catch (IOException e) {
      System.err.println("다운로드 실패: " + e.getMessage());
      e.printStackTrace();
    }
  }

  public String testPresignedUrl(String s3Key, long expirationMinutes) {
    System.out.println("\n=== [Presigned URL Test] ===");

    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucketName)
        .key(s3Key)
        .build();

    GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
        .signatureDuration(Duration.ofMinutes(expirationMinutes))
        .getObjectRequest(getObjectRequest)
        .build();

    PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
    URL presignedUrl = presignedRequest.url();

    System.out.println("Presigned URL 생성 성공!");
    System.out.println("  S3 Key    : " + s3Key);
    System.out.println("  유효 시간 : " + expirationMinutes + "분");
    System.out.println("  URL       : " + presignedUrl);

    return presignedUrl.toString();
  }

  public static void main(String[] args) {
    AWSS3Test test = new AWSS3Test();

    String localFile = "test-upload.txt";
    String s3Key = "test/test-upload.txt";
    String downloadTo = "test-download.txt";

    test.testUpload(localFile, s3Key);
    test.testDownload(s3Key, downloadTo);
    test.testPresignedUrl(s3Key, 10);
  }
}
