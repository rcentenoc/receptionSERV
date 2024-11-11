FROM openjdk:8-jdk-alpine

RUN apk update && \
  apk add --no-cache openssl && \
  rm -rf /var/cache/apk/*

#ENV SSLPASS $SSLPASS

ADD app.jar app.jar
#ADD tomcat/ca_tomcat.crt ca_tomcat.crt
#ADD tomcat/tomcat.crt tomcat.crt
#ADD tomcat/tomcat.key tomcat.key

#CMD echo "$SSLPASS"; openssl pkcs12 -export -in tomcat.crt -inkey tomcat.key -out cert_and_key.p12 -name tomcat -CAfile ca_tomcat.crt -caname root -password pass:$SSLPASS ; \
#     keytool -importkeystore -deststorepass $SSLPASS -destkeypass $SSLPASS -destkeystore keystore.jks -srckeystore cert_and_key.p12 -srcstoretype PKCS12 -srcstorepass $SSLPASS -alias tomcat; \
#     keytool -import -noprompt -trustcacerts -alias root -file /ca_tomcat.crt -keystore keystore.jks -storepass $SSLPASS ; \
#     java -jar app.jar


CMD java -jar app.jar
