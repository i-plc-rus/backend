##Оптимизация модуля обработки транзакций в банковской системе с использованием Spring Framework.
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
  * При обработке 1000 транзакций, с задержкой обработки каждой транзакции в 10ms, реализации V1 и V2 показали следующие результаты:
    * реализация V2 обрабатывает транзакции быстрее V1 примерно в ~12 раз
    * реализация V2 потребляет память в репозитории меньше чем V1 примерно в ~8 раз
~~~
2024-10-27T02:30:50.007+08:00 DEBUG 10428 --- [           main] .t.v.PerformanceTransactionProcessorTest : --------------------------------
2024-10-27T02:30:50.007+08:00 DEBUG 10428 --- [           main] .t.v.PerformanceTransactionProcessorTest : --- Test Performance Results ---
2024-10-27T02:30:50.007+08:00 DEBUG 10428 --- [           main] .t.v.PerformanceTransactionProcessorTest : --------------------------------
2024-10-27T02:30:50.007+08:00 DEBUG 10428 --- [           main] .t.v.PerformanceTransactionProcessorTest : Processing transactions count: 1000
2024-10-27T02:30:50.007+08:00 DEBUG 10428 --- [           main] .t.v.PerformanceTransactionProcessorTest : ForkJoin common pool parallelism: 15
2024-10-27T02:30:50.007+08:00 DEBUG 10428 --- [           main] .t.v.PerformanceTransactionProcessorTest : V1: duration=15660ms; repositorySize=335
2024-10-27T02:30:50.007+08:00 DEBUG 10428 --- [           main] .t.v.PerformanceTransactionProcessorTest : V2: duration=1263ms; repositorySize=40
2024-10-27T02:30:50.007+08:00 DEBUG 10428 --- [           main] .t.v.PerformanceTransactionProcessorTest : V1/V2 durationRatio=12.399049881235154
2024-10-27T02:30:50.007+08:00 DEBUG 10428 --- [           main] .t.v.PerformanceTransactionProcessorTest : V1/V2 repositorySizeRatio=8.375
~~~
