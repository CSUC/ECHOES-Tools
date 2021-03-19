#!/bin/bash
sed -i '/default SPARQL endpoint/c\'"requestConfig:{endpoint:'$DEFAULT_SPARQL_ENDPOINT'} \/\/ default SPARQL endpoint" /usr/share/nginx/html/index.html

exec "$@"