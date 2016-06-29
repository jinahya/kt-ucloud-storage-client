# kt-ucloud-storage-client
[![Maven Central](https://img.shields.io/maven-central/v/com.github.jinahya/kt-ucloud-storage-client-jax-rs.svg?maxAge=2592000)](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.github.jinahya%22%20AND%20a%3A%22kt-ucloud-storage-client-jax-rs%22)
[![Javadocs](http://www.javadoc.io/badge/com.github.jinahya/kt-ucloud-storage-client-jax-rs.svg)](http://www.javadoc.io/doc/com.github.jinahya/kt-ucloud-storage-client-jax-rs)

a simple client for [kt ucloud storage](https://ucloudbiz.olleh.com/portal/ktcloudportal.epc.productintro.ss.info.html).

## basic usage
```java
final String url; // authentication url
final String user; // access key ID
final String pass; // secret key
final RsStorageClient client = new RsStorageClient(url, user, pass);
// get the token for the first time.
final int statusCode = client.authenticateUser(r -> r.getStatusCode());
assert statusCode == 200;
```
### refresh token
```java
// make it sure the token is valid in next 10 minutes
client.refreshToken(System.currentTimeMillis() + 600000L);
```

## container
### create/update
```
client.refreshToken(System.currentTimeMillis() + 600000L);
final int status = client.updateContainer(
    "containerName",
    r -> r.getStatusCode());
assert status == 201 || status == 202;
```
### delete
```
client.refreshToken(System.currentTimeMillis() + 600000L);
final int status = client.deleteContainer(
    "containerName",
    r -> r.getStatusCode());
assert status == 204;
```
## object

### create/update
### read
### delete
