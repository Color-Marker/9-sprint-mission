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

## 오프셋 페이지네이션 vs. 커서 페이지네이션

일단 페이지네이션이라고 하면, 특정 정렬 기준과 필요 개수 조건에 맞춰 데이터를 가져오는 것이다.
기본 부분에서는 slice를 이용한 오프셋 페이지네이션을 이용하고,
심화 부분에서는 프론트에 cursor 관련 변수가 추가되어 커서 페이지네이션을 이용하게 리팩토링한다.

| 오프셋                                        | 커서                                                                              |
|:-------------------------------------------|:--------------------------------------------------------------------------------|
| 페이지 단위로 구분하여 요청/응답하게 구현하는 방식               | 클라이언트가 가져간 마지막 row의 순서상 다음 row들을 n개 요청/응답하게 구현                                  |
| 쉽게 말해 무조건 내부에서 미리 결정되어 있는 단위를 기준으로 잘라서 보내줌 | 보낸 데이터의 다음 데이터부터 보내주기 위한 구현이 되어있음. 그래서 어디서부터 보내줘야 하는지를 나타내는 지표로 cursor를 이용하는 방식 |
| 무조건 정해진 크기로 잘라서 보내줌                        | 여기 다음부터 몇 개 주세요가 가능 -> 무한 스크롤 기능 구현 가능!                                         |
| 정해진 크기로 보내면 되니 구현이 간단함                     | cursor가 어딘지 확인해서 그 다음부터 n개 보내줘야 해서 오프셋 방식보다는 복잡함                                |

## 주요 변경사항

- n+1 문제를 해결하기 위해 메시지 조회 시 entityGraph에 attachments도 원래 추가해두었으나, attachments가 리스트 형식이다 보니 쿼리 길이가 너무
  길어지는 상황이 발생함.
- 이에 이를 해결하기 위해 entityGraph에서 attachments는 제외하고 yaml에 batch 사이즈를 설정하는 방식으로 전환.
- batch 크기는 페이지네이션 사이즈인 50으로 같게 설정.

## 멘토에게

- n+1 문제가 전부 잘 해결된 것인지 궁금합니다.
- 또한 페이지네이션 방식 중 페이지 방식을 이용할 때 오프셋 형식을 이용하고, slice를 이용할 시 커서 방식을 이용하면 되는 건지 궁금합니다.