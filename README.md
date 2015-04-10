bodgeit-maven
================

[![Jenkins Build Status](http://home.nabla.mobi:8380/jenkins/job/bodgeit-maven-nightly/badge/icon)](http://home.nabla.mobi:8380/jenkins/job/bodgeit-maven-nightly)

Dependency Status using [versioneye](https://www.versioneye.com/users/AlbanAndrieu)

[![Dependency Status](https://www.versioneye.com/user/projects/55279e252ced4ffffe0006a1/badge.svg?style=flat)](https://www.versioneye.com/user/projects/55279e252ced4ffffe0006a1/visual)

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
