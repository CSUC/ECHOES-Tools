# ECHOES

ECHOES project aims at dissolving the current barriers to access to diverse collections of different nations and languages through an integrated platform. It will try to present and grant accesso to digital cultural assets in innovative ways, making them available for new and traditional audiences in a more efficient way. This contributes to solve the European challenge regarding the fragmentation of its history and cultural heritage contents into lots of different sources, and provides a solution to the needs of the users that require access to this kind of contents but seeing Europe as a whole.

ECHOES tries to provide a modular IT architecture, based on open source, to heritage collection holders, that functions as a digital ecosystem for a broad range of user communities, allowing them to take an active role and be able to enrich digital collections.

The partners of this project are Erfgoed Leiden en Omstreken (ELO), Tresoar (TRES), the Diputació de Barcelona (DIBA), the Generalitat de Catalunya (GENCAT); and as a technological partner the Consorci de Serveis Universitaris de Catalunya (CSUC).

## Tools [![Build Status](https://travis-ci.org/CSUC/ECHOES-Tools.svg?branch=develop)](https://travis-ci.org/CSUC/ECHOES-Tools) [![codecov](https://codecov.io/gh/CSUC/ECHOES-Tools/branch/develop/graph/badge.svg)](https://codecov.io/gh/CSUC/ECHOES-Tools)
Java Tools to generate EDM compliant items from diferent metadata schema sources (standars as Dublin Core, A2A, etc. or custom schemas).
Mapped items can be loaded in a DataLake (based on [Blazegraph](https://github.com/blazegraph)) to exploit them from SPARQL End Point (base on [YASGUI](https://github.com/OpenTriply/YASGUI)) or using the DataLake API.

## Resources
* [Release Notes](../../releases)
* [Wiki](../../wiki/Home)

## Installing

Donwload submodules corelib and europeana-parent-pom

```
git submodule update --init --recursive
```
### Tools
```
mvn clean install -DskipTests -Ptools
```
### Database
```
mvn clean install -DskipTests -Pdatabase
```
