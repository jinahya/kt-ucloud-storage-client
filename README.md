# kt-ucloud-storage-client-jax-rs
[![Maven Central](https://img.shields.io/maven-central/v/com.github.jinahya/kt-ucloud-storage-client-jax-rs.svg?style=flat-square)](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.github.jinahya%22%20AND%20a%3A%22kt-ucloud-storage-client-jax-rs%22)
[![Javadocs](http://www.javadoc.io/badge/com.github.jinahya/kt-ucloud-storage-client-jax-rs.svg?style=flat-square)](http://www.javadoc.io/doc/com.github.jinahya/kt-ucloud-storage-client-jax-rs)
[![Dependency Status](https://www.versioneye.com/user/projects/57a6194d0f64000041a9375e/badge.svg?style=flat-square)](https://www.versioneye.com/user/projects/57a6194d0f64000041a9375e)
[![Build Status](https://travis-ci.org/jinahya/kt-ucloud-storage-client-jax-rs.svg?style=flat-square&branch=develop)](https://travis-ci.org/jinahya/kt-ucloud-storage-client-jax-rs)
[![Sputnik](https://sputnik.ci/conf/badge)](https://sputnik.ci/app#/builds/jinahya/kt-ucloud-storage-client-jax-rs)

a simple client for [kt ucloud storage](https://ucloudbiz.olleh.com/portal/ktcloudportal.epc.productintro.ss.info.html).

## verify
```
$ mvn -P(jersey|cxf|resteasy) -DauthUrl="" -DauthUser="" -DauthPass="" clean verify
```
## methods mapping
http  |java        |notes
------|------------|-----
HEAD  |peek...     |reads resources' information
GET   |read...     |reads resources
PUT   |update...   |creates or updates resources
POST  |configure...|creates, updates or removes metadata
DELETE|delete...   |deletes resources
## java methods
There are, at most, four method types for each operations.
```java
// invoke and apply the function with the server response
// and return the value the function results
<T> T ...(..., Function<Response, T> function)
```
```java
// invoke and apply the function with the server response and the client itself
// and return the value the function results
<T> T ...(..., BiFunction<Response, StorageClient, T> function)
```
```java
// invoke and accept the consumer with the server response
// and return the client itself.
StorageClient ...(..., Consumer<Response> consumer)
```
```java
// invoke and accept the consumer with the server response and the client itself
// and return the client itself.
StorageClient ...(..., BiConsumer<Response, StorageClient> consumer)
```
## basic usage
### create a new instance
```java
final String url; // authentication url
final String user; // access key ID
final String pass; // secret key
final StorageClient client = new StorageClient(url, user, pass);
```
### authenticate user
```java
final int statusCode = client.authenticateUser(
    r -> {
        return r.getStatus();
    }
);
assert statusCode == 200;
```
## account
### `peekAccount`
### `readAccount`
#### `readAccountContainerNames`
## container
### create/update
```java
final Family family = client.createContainer(
    "containerName",
    null, // query paramters; MultivaluedMap<String, Object>
    null, // request heaers; MultivaluedMap<String, Object>
    (r, c) -> r.getStatusInfo().getFamily());
assert family == Family.SUCCESSFUL;
```
### read
```java
final Family family = client.readContainer(
    "containerName",
    null, // query paramters; MultivaluedMap<String, Object>
    null, // request heaers; MultivaluedMap<String, Object>
    (r, c) -> r.getStatusInfo().getFamily());
assert family == Family.SUCCESSFUL;
```
### delete
```java
client.deleteContainer(
    "containerName",
    null, // query parameters
    null, // request headers
    (r, c) -> {
        return null;
    }
);
```
## object
### create/update
```java
final Entity<?> entity = getSome();
final int status = client.createObject(
    "containerName",
    "objectName",
    null,
    null,
    entity,
    (r, c) -> {
        // mess with the Response(r) here
        return r.getStatus();
    }
);
```
### read
```java
client.readObject(
    "containerName",
    "objectName",
    null,
    null,
    (r, c) -> {
        // mess with the Response(r) here
        return null;
    }
);
```
### delete
```java
client.deleteObject(
    "containerName",
    "objectName",
    null,
    null,
    (r, c) -> {
        // mess with the Response(r) here
        return null;
    }
);
```
