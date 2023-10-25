# publish-broker-object

CURL para la invocación sincronica:

curl --location 'http://localhost:8090/publish/sync?subject=sync.get.credentials&url=nats%3A%2F%2Flocalhost%3A4222' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "aaa bbb",
    "dni": "34324234230",
    "email": "asdasd@asdas.kk",
    "password": "asdfasfdf",
    "pin": "da22",
    "refreshToken": "kldslkfjsdlklñklsd",
    "createdAt": "01-01-1900",
    "updatedAt": "01-01-1900"
}'


CURL para la invocación asincronica:

curl --location 'http://localhost:8090/publish/async?subject=async.create.user&url=nats%3A%2F%2Flocalhost%3A4222' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "aaa bbb",
    "dni": "34324234230",
    "email": "asdasd@asdas.kk",
    "password": "asdfasfdf",
    "pin": "da22",
    "refreshToken": "kldslkfjsdlklñklsd",
    "createdAt": "01-01-1900",
    "updatedAt": "01-01-1900"
}'
