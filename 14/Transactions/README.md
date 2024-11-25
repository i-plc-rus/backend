## Оптимизация модуля обработки транзакций

### Build
```bash
./gradlew clean build -x test
```

### Tests
This task runs tests without LoadTest:
```bash
./gradlew clean test
```
 To run LoadTest use dedicated task. This test take a while (up to 1min depending on machine you are running on):
```bash
./gradlew clean loadTest
```