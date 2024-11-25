##Оптимизация модуля обработки транзакций в банковской системе с использованием Spring Framework.
* История переработки исходного кода доступна для изучения в локальном репозитории git 
* Модуль обработки транзакций реализован в виде spring-boot стартера [transaction-starter](pom.xml), что позволит без проблем подключать его к необходимым сервисам
* Модуль состоит из нескольких пакетов:
  * Пакет [com.bank.transactions](src/main/java/com/bank/transactions/package-info.java) содержит интерфейсы [Transaction.java](src/main/java/com/bank/transactions/Transaction.java) и [TransactionProcessor.java](src/main/java/com/bank/transactions/TransactionProcessor.java), которые реализованы в пакетах [V1](src/main/java/com/bank/transactions/v1/package-info.java) и [V2](src/main/java/com/bank/transactions/v2/package-info.java)
  * Пакет [V1](src/main/java/com/bank/transactions/v1/package-info.java) содержит старый легаси-код обработки транзакций, он используется в тестах производительности
  * Пакет [V2](src/main/java/com/bank/transactions/v2/package-info.java) содержит новый переработанный код, который обрабатывает транзакции намного быстрее и при этом потребляет меньше ресурсов в репозитории
  * Пакет [com.bank.common](src/main/java/com/bank/common/package-info.java) содержит полезные классы для работы с многопоточностью, исключениями, файлами. В будущем планируется вынести этот пакет в самостоятельный модуль или библиотеку 
* Новый переработанный код обработки транзакций обеспечивает качественную бесперебойную работу благодаря следующим подходам:
  * Быстрая обработка большого количества транзакций обеспечивается за счет использования многопоточности, реализованной в классе [TransactionRecursiveAction.java](src/main/java/com/bank/transactions/v2/TransactionRecursiveAction.java)
  * Надежный перехват ошибок и их обработка выполняется с применением аспекта [ExceptionHandler.java](src/main/java/com/bank/transactions/v2/ExceptionHandler.java)
  * Ход обработки транзакций и ошибки логируются просто и удобно с использованием [Logger.java](src/main/java/com/bank/transactions/v2/Logger.java)
  * Параметры обработчика транзакции можно легко отредактировать в [application.properties](src/main/resources/application.properties)
  * После запуска, в логе отображается красивый баннер, который при необходимости можно отключить в [application.properties](src/main/resources/application.properties)
  * Контроль качества обеспечивается всесторонними тестами [TransactionProcessorTest.java](src/test/java/com/bank/transactions/v2/TransactionProcessorTest.java), [ExceptionHandlerTest.java](src/test/java/com/bank/transactions/v2/ExceptionHandlerTest.java), [LoggerTest.java](src/test/java/com/bank/transactions/v2/LoggerTest.java), [BannerTest.java](src/test/java/com/bank/transactions/v2/BannerTest.java)
* Эффективность работы модуля подтверждается сравнительным тестом производительности [PerformanceTransactionProcessorTest.java](src/test/java/com/bank/transactions/v2/PerformanceTransactionProcessorTest.java)
  * При обработке 10000 транзакций на 16 ядерном процесоре с задержкой обновления репозитория на 1ms 
    * реализация V2 обрабатывает транзакции быстрее V1 примерно в ~9 раз
    * реализация V2 потребляет память в репозитории эффективнее чем V1 примерно в ~5 раз
~~~
2024-10-27T02:30:50.007+08:00 DEBUG 10428 --- [           main] .t.v.PerformanceTransactionProcessorTest : --------------------------------
2024-10-27T02:30:50.007+08:00 DEBUG 10428 --- [           main] .t.v.PerformanceTransactionProcessorTest : --- Test Performance Results ---
2024-10-27T02:30:50.007+08:00 DEBUG 10428 --- [           main] .t.v.PerformanceTransactionProcessorTest : --------------------------------
2024-10-27T02:30:50.007+08:00 DEBUG 10428 --- [           main] .t.v.PerformanceTransactionProcessorTest : Processing transactions count: 10000
2024-10-27T02:30:50.007+08:00 DEBUG 10428 --- [           main] .t.v.PerformanceTransactionProcessorTest : ForkJoin common pool parallelism: 15
2024-10-27T02:30:50.007+08:00 DEBUG 10428 --- [           main] .t.v.PerformanceTransactionProcessorTest : V1: duration=6261ms; repositorySize=3296
2024-10-27T02:30:50.007+08:00 DEBUG 10428 --- [           main] .t.v.PerformanceTransactionProcessorTest : V2: duration=700ms; repositorySize=617
2024-10-27T02:30:50.007+08:00 DEBUG 10428 --- [           main] .t.v.PerformanceTransactionProcessorTest : V1/V2 durationRatio=8.944285714285714
2024-10-27T02:30:50.007+08:00 DEBUG 10428 --- [           main] .t.v.PerformanceTransactionProcessorTest : V1/V2 repositorySizeRatio=5.341977309562399
~~~
