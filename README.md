# ECHOES-Parser [![Build Status](https://travis-ci.org/CSUC/ECHOES-Parser.svg?branch=develop)](https://travis-ci.org/CSUC/ECHOES-Parser) [![codecov](https://codecov.io/gh/CSUC/ECHOES-Parser/branch/develop/graph/badge.svg)](https://codecov.io/gh/CSUC/ECHOES-Parser)
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
