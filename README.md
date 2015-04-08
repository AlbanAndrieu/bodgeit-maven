# bodgeit-maven

This is mavenised version of the bodgeitstore. (see https://github.com/psiinon/bodgeit for original code with Ant).
Goal is to test integration with https://github.com/javabeanz/zap-maven-plugin

### build

```
$ mvn clean install
```

#### build + integration test

Jetty9x is the default server

```
$ mvn clean install -Prun-its
$ mvn clean install -Prun-its -Dserver=tomcat8x
```
