# mock
Сервис для мокирования интеграций в среде тестирования

### Проект использует:
in memory h2 DB и консоль по адресу http://localhost:9091/h2-console

### Пример использования:
Приложение должно отвечать сохраненными /path заглушек например:
```
curl --location --request POST 'http://localhost:9091/mock/rest/save' \
--header 'Content-Type: application/json' \
--data-raw '{
    "uri" : "/test",
    "code": 200,
    "body" : "{\"code\":1}"
}'
```
запрос зашлушки
```
curl --location --request POST 'http://localhost:9091/test'

HTTP/1.1 200 OK
{"code":1}
```

PS: Порт прописан 9091, можем скорректировать

## Запуск
#### Maven:
`mvn spring-boot:run`

#### JAR-файла:
`java -jar -pl :mock-rest-1.0.0.jar`
