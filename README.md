# ECHOES-Parser [![Build Status](https://travis-ci.org/CSUC/ECHOES-Parser.svg?branch=develop)](https://travis-ci.org/CSUC/ECHOES-Parser) [![codecov](https://codecov.io/gh/CSUC/ECHOES-Parser/branch/develop/graph/badge.svg)](https://codecov.io/gh/CSUC/ECHOES-Parser)
Java mapping tool that generates EDM compliant items

Donwload submodules corelib and europeana-parent-pom

```
git submodule update --init --recursive
```
```
mvn clean generate-sources install -DskipTests
```
## Echoes Examples (module ECHOES-Parser-Core)

### Download files OAI

```
java -jar Parser-Core-${project.version}-jar-with-dependencies.jar OAIDownload --host [host] --xslt
```
```
java -jar Parser-Core-${project.version}-jar-with-dependencies.jar OAIDownload --host https://webservices.picturae.com/a2a/20a181d4-c896-489f-9d16-20a3b7306b15 --xslt EDM_A2A.xslt
```

### Parser XML OAI

```
java -jar Parser-Core-${project.version}-jar-with-dependencies.jar OAIParser --host [host] --verb [verb] --metadataPrefix [metadataPrefix] --set [set]
```
```
java -jar Parser-Core-${project.version}-jar-with-dependencies.jar OAIParser --host [host] --verb [verb] --resumptionToken [resumptionToken]
```

```
java -jar Parser-Core-0.0.1-SNAPSHOT-jar-with-dependencies.jar OAIParser --host https://webservices.picturae.com/a2a/20a181d4-c896-489f-9d16-20a3b7306b15 --verb ListRecords --metadataPrefix a2a --set bs_e
```
Result:

| tag  | total | xpath |
| ------------- | ------------- | ------------- |
|a2a:Value|1179|/ns1:OAI-PMH/ns1:ListRecords/ns1:record/ns1:metadata/a2a:A2A[@Version]/a2a:Person[@pid]/a2a:PersonRemark[@Key]/a2a:Value|
|a2a:From|1103|/ns1:OAI-PMH/ns1:ListRecords/ns1:record/ns1:metadata/a2a:A2A[@Version]/a2a:Source/a2a:SourceIndexDate/a2a:From|
|a2a:Collection|1103|/ns1:OAI-PMH/ns1:ListRecords/ns1:record/ns1:metadata/a2a:A2A[@Version]/a2a:Source/a2a:SourceReference/a2a:Collection|
|a2a:Year|2192|/ns1:OAI-PMH/ns1:ListRecords/ns1:record/ns1:metadata/a2a:A2A[@Version]/a2a:Source/a2a:SourceDate/a2a:Year|
|a2a:EventType|1103|/ns1:OAI-PMH/ns1:ListRecords/ns1:record/ns1:metadata/a2a:A2A[@Version]/a2a:Event[@eid]/a2a:EventType|
|ns1:identifier|1103|/ns1:OAI-PMH/ns1:ListRecords/ns1:record/ns1:header/ns1:identifier|
|a2a:Gender|2186|/ns1:OAI-PMH/ns1:ListRecords/ns1:record/ns1:metadata/a2a:A2A[@Version]/a2a:Person[@pid]/a2a:Gender|
|a2a:Month|2190|/ns1:OAI-PMH/ns1:ListRecords/ns1:record/ns1:metadata/a2a:A2A[@Version]/a2a:Person[@pid]/a2a:BirthDate/a2a:Month|
|a2a:SourceLastChangeDate|1103|/ns1:OAI-PMH/ns1:ListRecords/ns1:record/ns1:metadata/a2a:A2A[@Version]/a2a:Source/a2a:SourceLastChangeDate|
|a2a:PersonNameLastName|2184|/ns1:OAI-PMH/ns1:ListRecords/ns1:record/ns1:metadata/a2a:A2A[@Version]/a2a:Person[@pid]/a2a:PersonName/a2a:PersonNameLastName|
|a2a:Month|2190|/ns1:OAI-PMH/ns1:ListRecords/ns1:record/ns1:metadata/a2a:A2A[@Version]/a2a:Event[@eid]/a2a:EventDate/a2a:Month|
|ns1:datestamp|1103|/ns1:OAI-PMH/ns1:ListRecords/ns1:record/ns1:header/ns1:datestamp|
|a2a:Place|3462|/ns1:OAI-PMH/ns1:ListRecords/ns1:record/ns1:metadata/a2a:A2A[@Version]/a2a:Person[@pid]/a2a:BirthPlace/a2a:Place|
|a2a:PersonNameFirstName|2186|/ns1:OAI-PMH/ns1:ListRecords/ns1:record/ns1:metadata/a2a:A2A[@Version]/a2a:Person[@pid]/a2a:PersonName/a2a:PersonNameFirstName|
|a2a:RegistryNumber|1103|/ns1:OAI-PMH/ns1:ListRecords/ns1:record/ns1:metadata/a2a:A2A[@Version]/a2a:Source/a2a:SourceReference/a2a:RegistryNumber|
|a2a:EventKeyRef|2186|/ns1:OAI-PMH/ns1:ListRecords/ns1:record/ns1:metadata/a2a:A2A[@Version]/a2a:RelationEP/a2a:EventKeyRef|
|a2a:Year|2192|/ns1:OAI-PMH/ns1:ListRecords/ns1:record/ns1:metadata/a2a:A2A[@Version]/a2a:Event[@eid]/a2a:EventDate/a2a:Year|
|a2a:Uri|1049|/ns1:OAI-PMH/ns1:ListRecords/ns1:record/ns1:metadata/a2a:A2A[@Version]/a2a:Source/a2a:SourceAvailableScans/a2a:Scan/a2a:Uri|
|a2a:Day|2190|/ns1:OAI-PMH/ns1:ListRecords/ns1:record/ns1:metadata/a2a:A2A[@Version]/a2a:Person[@pid]/a2a:BirthDate/a2a:Day|
|ns1:resumptionToken[@completeListSize and @cursor]|12|/ns1:OAI-PMH/ns1:ListRecords/ns1:resumptionToken[@completeListSize and @cursor]|
|ns1:request[@verb and @resumptionToken]|11|/ns1:OAI-PMH/ns1:request[@verb and @resumptionToken]|
|a2a:Day|2190|/ns1:OAI-PMH/ns1:ListRecords/ns1:record/ns1:metadata/a2a:A2A[@Version]/a2a:Source/a2a:SourceDate/a2a:Day|
|a2a:Day|2190|/ns1:OAI-PMH/ns1:ListRecords/ns1:record/ns1:metadata/a2a:A2A[@Version]/a2a:Event[@eid]/a2a:EventDate/a2a:Day|
|a2a:To|1103|/ns1:OAI-PMH/ns1:ListRecords/ns1:record/ns1:metadata/a2a:A2A[@Version]/a2a:Source/a2a:SourceIndexDate/a2a:To|
|a2a:SourceType|1103|/ns1:OAI-PMH/ns1:ListRecords/ns1:record/ns1:metadata/a2a:A2A[@Version]/a2a:Source/a2a:SourceType|
|a2a:Place|3462|/ns1:OAI-PMH/ns1:ListRecords/ns1:record/ns1:metadata/a2a:A2A[@Version]/a2a:Source/a2a:SourcePlace/a2a:Place|
|a2a:InstitutionName|1103|/ns1:OAI-PMH/ns1:ListRecords/ns1:record/ns1:metadata/a2a:A2A[@Version]/a2a:Source/a2a:SourceReference/a2a:InstitutionName|
|a2a:Profession|1817|/ns1:OAI-PMH/ns1:ListRecords/ns1:record/ns1:metadata/a2a:A2A[@Version]/a2a:Person[@pid]/a2a:Profession|
|ns1:request[@verb and @metadataPrefix and @set]|1|/ns1:OAI-PMH/ns1:request[@verb and @metadataPrefix and @set]|
|ns1:responseDate|12|/ns1:OAI-PMH/ns1:responseDate|
|a2a:UriViewer|1049|/ns1:OAI-PMH/ns1:ListRecords/ns1:record/ns1:metadata/a2a:A2A[@Version]/a2a:Source/a2a:SourceAvailableScans/a2a:Scan/a2a:UriViewer|
|a2a:UriPreview|1049|/ns1:OAI-PMH/ns1:ListRecords/ns1:record/ns1:metadata/a2a:A2A[@Version]/a2a:Source/a2a:SourceAvailableScans/a2a:Scan/a2a:UriPreview|
|a2a:Place|3462|/ns1:OAI-PMH/ns1:ListRecords/ns1:record/ns1:metadata/a2a:A2A[@Version]/a2a:Source/a2a:SourceReference/a2a:Place|
|a2a:Book|1103|/ns1:OAI-PMH/ns1:ListRecords/ns1:record/ns1:metadata/a2a:A2A[@Version]/a2a:Source/a2a:SourceReference/a2a:Book|
|a2a:Archive|1103|/ns1:OAI-PMH/ns1:ListRecords/ns1:record/ns1:metadata/a2a:A2A[@Version]/a2a:Source/a2a:SourceReference/a2a:Archive|
|a2a:RecordGUID|1103|/ns1:OAI-PMH/ns1:ListRecords/ns1:record/ns1:metadata/a2a:A2A[@Version]/a2a:Source/a2a:RecordGUID|
|a2a:PersonNamePrefixLastName|642|/ns1:OAI-PMH/ns1:ListRecords/ns1:record/ns1:metadata/a2a:A2A[@Version]/a2a:Person[@pid]/a2a:PersonName/a2a:PersonNamePrefixLastName|
|a2a:Place|3462|/ns1:OAI-PMH/ns1:ListRecords/ns1:record/ns1:metadata/a2a:A2A[@Version]/a2a:Event[@eid]/a2a:EventPlace/a2a:Place|
|a2a:PersonAgeLiteral|117|/ns1:OAI-PMH/ns1:ListRecords/ns1:record/ns1:metadata/a2a:A2A[@Version]/a2a:Person[@pid]/a2a:Age/a2a:PersonAgeLiteral|
|a2a:SourceDigitalOriginal|1103|/ns1:OAI-PMH/ns1:ListRecords/ns1:record/ns1:metadata/a2a:A2A[@Version]/a2a:Source/a2a:SourceDigitalOriginal|
|a2a:Year|2192|/ns1:OAI-PMH/ns1:ListRecords/ns1:record/ns1:metadata/a2a:A2A[@Version]/a2a:Person[@pid]/a2a:BirthDate/a2a:Year|
|a2a:Place|3462|/ns1:OAI-PMH/ns1:ListRecords/ns1:record/ns1:metadata/a2a:A2A[@Version]/a2a:Person[@pid]/a2a:Residence/a2a:Place|
|a2a:OrderSequenceNumber|1049|/ns1:OAI-PMH/ns1:ListRecords/ns1:record/ns1:metadata/a2a:A2A[@Version]/a2a:Source/a2a:SourceAvailableScans/a2a:Scan/a2a:OrderSequenceNumber|
|a2a:PersonKeyRef|2186|/ns1:OAI-PMH/ns1:ListRecords/ns1:record/ns1:metadata/a2a:A2A[@Version]/a2a:RelationEP/a2a:PersonKeyRef|
|a2a:Month|2190|/ns1:OAI-PMH/ns1:ListRecords/ns1:record/ns1:metadata/a2a:A2A[@Version]/a2a:Source/a2a:SourceDate/a2a:Month|
|a2a:DocumentNumber|1050|/ns1:OAI-PMH/ns1:ListRecords/ns1:record/ns1:metadata/a2a:A2A[@Version]/a2a:Source/a2a:SourceReference/a2a:DocumentNumber|
|a2a:RelationType|2186|/ns1:OAI-PMH/ns1:ListRecords/ns1:record/ns1:metadata/a2a:A2A[@Version]/a2a:RelationEP/a2a:RelationType|
|a2a:Value|1179|/ns1:OAI-PMH/ns1:ListRecords/ns1:record/ns1:metadata/a2a:A2A[@Version]/a2a:Source/a2a:SourceRemark[@Key]/a2a:Value|
