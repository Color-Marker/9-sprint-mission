## 요구사항

### 기본

- 전부 완료

### 심화

- 전부 완료

## 관계 매핑 정보

| 엔티티 관계                     | 다중성 | 방향성                         | 부모-자식 관계                          | 연관관계의 주인   |
|:---------------------------|:----|:----------------------------|:----------------------------------|:-----------|
| User : BianaryContent      | 1:1 | User -> BinaryContent       | 부모: BinaryContent, 자식: User       | User       |
| User : UserStatus          | 1:1 | UserStatus -> User          | 부모: User, 자식: UserStatus          | UserStatus |
| Channel : ReadStatus       | 1:N | ReadStatus -> Channel       | 부모: Channel, 자식: ReadStatus       | ReadStatus |
| User : ReadStatus          | 1:N | ReadStatus -> User          | 부모: User, 자식: ReadStatus          | ReadStatus |
| Channel : Message          | 1:N | Message -> Channel          | 부모: Channel, 자식: Message          | Message    |
| User : Message             | 1:N | Message -> User             | 부모: User, 자식: Message             | Message    |
| BinaryContent : Attachment | 1:N | Attachment -> BinaryContent | 부모: BinaryContent, 자식: Attachment | Attachment |
| Message : Attachment       | 1:N | Attachment -> Message       | 부모: Message, 자식: Attachment       | Attachment |

## 주요 변경사항
-

## 스크린샷

![사진설명](사진링크)

## 멘토에게
- 

- 