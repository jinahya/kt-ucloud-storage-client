# kt-ucloud-storage-client
[![Maven Central](https://img.shields.io/maven-central/v/com.github.jinahya/kt-ucloud-storage-client.svg?style=flat-square)](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.github.jinahya%22%20AND%20a%3A%22kt-ucloud-storage-client%22)
[![Javadocs](http://www.javadoc.io/badge/com.github.jinahya/kt-ucloud-storage-client.svg?style=flat-square)](http://www.javadoc.io/doc/com.github.jinahya/kt-ucloud-storage-client)
[![Dependency Status](https://www.versioneye.com/user/projects/57a6194d0f64000041a9375e/badge.svg?style=flat-square)](https://www.versioneye.com/user/projects/57a6194d0f64000041a9375e)
[![Build Status](https://travis-ci.org/jinahya/kt-ucloud-storage-client.svg?style=flat-square&branch=develop)](https://travis-ci.org/jinahya/kt-ucloud-storage-client)
[![Sputnik](https://sputnik.ci/conf/badge)](https://sputnik.ci/app#/builds/jinahya/kt-ucloud-storage-client)

a set of clients for accessing [kt ucloud storage](https://ucloudbiz.olleh.com/portal/ktcloudportal.epc.productintro.ss.info.html).

## verify
```
$ mvn -P(jersey|cxf|resteasy) -DauthUrl="" -DauthUser="" -DauthPass="" verify
```

## methods
http  |java        |notes
------|------------|-----
HEAD  |peek...     |reads resources' information
GET   |read...     |reads resources
PUT   |update...   |creates or updates resources
POST  |configure...|creates, updates or removes metadata
DELETE|delete...   |deletes resources

## java.net
```java
final StorageClientNet client
    = new StorageClientNet(authUrl, authUser, authPass);
```
## java.net.http
N/A yet.

## javax.ws.rs
```java
final StorageClientWsRs client
    = new StorageClientWsRs(authUrl, authUser, authPass);
```
