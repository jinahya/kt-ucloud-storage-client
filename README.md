# kt-ucloud-storage-client-jax-rs
[![Maven Central](https://img.shields.io/maven-central/v/com.github.jinahya/kt-ucloud-storage-client-jax-rs.svg?maxAge=2592000)](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.github.jinahya%22%20AND%20a%3A%22kt-ucloud-storage-client-jax-rs%22)
[![Javadocs](http://www.javadoc.io/badge/com.github.jinahya/kt-ucloud-storage-client-jax-rs.svg)](http://www.javadoc.io/doc/com.github.jinahya/kt-ucloud-storage-client-jax-rs)

a simple client for [kt ucloud storage](https://ucloudbiz.olleh.com/portal/ktcloudportal.epc.productintro.ss.info.html).

## basic usage
```java
final String authUrl; // authentication url
final String authUser; // access key ID
final String authPass; // secret key
final RsStorageClient client = new RsStorageClient(authUrl, authUser, authPass);
```
### authenticate user
```java
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
```java
client.refreshToken(System.currenTimeMillis() + 600000L);
final Entity<?> entity = getSome();
final int status = client.updateObject(
    "containerName", "objectName", entity, r -> r.getStatusCode());
```
### read
```java
client.refreshToken(System.currenTimeMillis() + 600000L);
client.readObject(
    "containerName", "objectName", r -> {
        // mess with the Response(r) here
        return null;
    });
```
### delete
```java
client.refreshToken(System.currenTimeMillis() + 600000L);
client.deleteObject(
    "containerName", "objectName", r -> {
        // mess with the Response(r) here
        return null;
    });
```
