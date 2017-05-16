#!/bin/bash

curl -o secrets.tar.aes ${SECRETS_URL}/secrets.tar.aes
md5sum secrets.tar.aes

openssl enc -aes-256-cbc -d -in secrets.tar.aes -out secrets.tar -k "${KEY}"
md5sum secrets.tar

tar -xf secrets.tar -C ~

mkdir -p ~/.gradle/


echo "
signing.keyId=${GPG_KEY_ID}
signing.password=${GPG_PASSWORD}
signing.secretKeyRingFile=/home/ubuntu/.gnupg/secring.gpg

ossrhUsername=${OSSRH_USERNAME}
ossrhPassword=${OSSRH_PASSWORD}
"> ~/.gradle/gradle.properties

