# ECHOES-Tools [![Build Status](https://travis-ci.org/CSUC/ECHOES-Tools.svg?branch=develop)](https://travis-ci.org/CSUC/ECHOES-Tools) [![codecov](https://codecov.io/gh/CSUC/ECHOES-Tools/branch/develop/graph/badge.svg)](https://codecov.io/gh/CSUC/ECHOES-Tools)
Java mapping tool that generates EDM compliant items

## ABOUT ECHOES

ECHOES project aims at dissolving the current barriers to access to diverse collections of different nations and languages through an integrated platform. It will try to present and grant accesso to digital cultural assets in innovative ways, making them available for new and traditional audiences in a more efficient way. This contributes to solve the European challenge regarding the fragmentation of its history and cultural heritage contents into lots of different sources, and provides a solution to the needs of the users that require access to this kind of contents but seeing Europe as a whole.

ECHOES tries to provide a modular IT architecture, based on open source, to heritage collection holders, that functions as a digital ecosystem for a broad range of user communities, allowing them to take an active role and be able to enrich digital collections.

The partners of this project are Erfgoed Leiden en Omstreken (ELO, Project Leader), Tresoar (TRES), the Diputació de Barcelona (DIBA) and the Generalitat de Catalunya (GENCAT). After some conversations, it seems that the Consorci de Serveis Universitaris de Catalunya (CSUC) could be the technological partner of the project.

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
