bodgeit-maven
================

[![Jenkins Build Status](http://home.nabla.mobi:8381/job/bodgeit-maven-nightly/badge/icon)](http://home.nabla.mobi:8381/job/bodgeit-maven-nightly)

This is mavenised version of the bodgeitstore. (see https://github.com/psiinon/bodgeit for original code with Ant).
Goal is to test integration with https://github.com/javabeanz/zap-maven-plugin

#### build

```
$ mvn clean install
```

#### build + integration test

Jetty9x is the default server

```
$ mvn clean install -Prun-its
$ mvn clean install -Prun-its -Dserver=tomcat8x
```

#### eclipse

First run the webapp

```
$ mvn clean install jetty:run-war
```

Now you can run your Junit test (selenium is using chrome)
If you want to use firefox, you must uncomment int the test and create a selenium profile using

```
$ firefox -P
$ #create profile selenium
```
