FROM nginx:stable
MAINTAINER Albert Martínez <albert.martinez@csuc.cat>

ENV DEFAULT_SPARQL_ENDPOINT http://localhost/sparql

COPY index.html /usr/share/nginx/html/
COPY startup.sh /
RUN chmod +x /startup.sh

ENTRYPOINT ["/startup.sh"]

CMD ["nginx", "-g", "daemon off;"]