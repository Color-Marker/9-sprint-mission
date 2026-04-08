## 요구사항

### 기본

- 전부 완료

### 심화

- 전부 완료

## 진행 중 특이사항

- GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO 유저이름; 실행을 추가로 해서 권한을 주고 나서야 CRUD 작동 확인 됌.

- 기본 제공 env에서 JVM_OPTS="-Xmx384m -Xms256m -XX:MaxMetaspaceSize=64m -XX:+UseSerialGC" 작동 문제 있음.
    - 큰 따옴표를 DockerFile에서 명령 실행할 때 이상하게 인식함. 따라서 큰 따옴표들 제거함.
    - maxMetaspaceSize가 너무 작아 스프링이 실행되다가 메모리 크기 문제로 다운되는 걸 확인함.
    - 192m으로 최대 크기를 늘려 실행하여 해결.

## 멘토에게
