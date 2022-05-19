
## [![Nabla](http://albandrieu.com/nabla/index/assets/nabla/nabla-4.png)](https://github.com/AlbanAndrieu) bodgeit-maven

[![License: APACHE](http://img.shields.io/:license-apache-blue.svg?style=flat-square)](http://www.apache.org/licenses/LICENSE-2.0.html)
[![License: MIT](https://img.shields.io/badge/license-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

<!--[![Jenkins Build Status](http://albandrieu.com/jenkins/job/bodgeit-maven-nightly-nightly/badge/icon)](http://albandrieu.com/jenkins/job/bodgeit-maven-nightly-nightly)-->

This is mavenised version of the bodgeitstore. (see https://github.com/psiinon/bodgeit for original code with Ant).
Goal is to test integration with https://github.com/javabeanz/zap-maven-plugin

#### build

```
$ mvn clean install -Dserver=jetty9x -Ddatabase=hsqldb
```

#### build + integration test

Jetty9x is the default server

```
$ mvn clean install -Prun-its -Dserver=jetty9x -Ddatabase=hsqldb
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
