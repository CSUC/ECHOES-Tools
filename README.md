# ECHOES-Tools [![Build Status](https://travis-ci.org/CSUC/ECHOES-Tools.svg?branch=develop)](https://travis-ci.org/CSUC/ECHOES-Tools) [![codecov](https://codecov.io/gh/CSUC/ECHOES-Tools/branch/develop/graph/badge.svg)](https://codecov.io/gh/CSUC/ECHOES-Tools)
Java mapping tool that generates EDM compliant items

## Resources
* [Release Notes](../../releases)
* [Wiki](../../wiki/Home)

## Installing

Donwload submodules corelib and europeana-parent-pom

```
git submodule update --init --recursive
```
```
mvn clean generate-sources install -DskipTests
```
