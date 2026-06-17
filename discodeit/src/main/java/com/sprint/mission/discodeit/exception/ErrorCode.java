package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.exception.notification.NotificationNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
  USER_NOT_FOUND("유저를 찾을 수 없습니다"),
  DUPLICATE_EMAIL("이미 존재하는 이메일입니다."),
  DUPLICATE_NAME("이미 존재하는 이름입니다."),
  CHANNEL_NOT_FOUND("채널을 찾을 수 없습니다."),
  FILE_NOT_FOUND("파일을 찾을 수 없습니다"),
  NOT_USABLE_FILEPATH("경로에 파일이 이미 존재합니다."),
  FILE_WRITE("파일 저장 중 오류가 발생하였습니다."),
  FILE_READ("파일을 불러오는 중 오류가 발생하였습니다."),
  MESSAGE_NOT_FOUND("메시지를 찾을 수 없습니다."),
  PRIVATE_CHANNEL_UPDATE("private 채널은 수정할 수 없습니다."),
  READSTATUS_NOT_FOUND("읽음 상태를 찾을 수 없습니다."),
  READSTATUS_ALREADY_EXIST("읽음 상태가 이미 존재합니다."),
  USERSTATUS_NOT_FOUND("유저 상태를 찾을 수 없습니다."),
  USERSTATUS_ALREADY_EXIST("유저 상태가 이미 존재합니다."),
  NOTIFICATION_NOT_FOUND("알림을 찾을 수 없습니다.");
  private String message;
}
