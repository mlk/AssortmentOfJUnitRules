CONTRIBUTING
===========

CODE STYLE
----------
[Google Code Style](https://google.github.io/styleguide/javaguide.html)


RELEASING
---------
Every push to master is automatically released.

FORKING
-------
If you plan to fork and release and make use of Travis CI to perform continuous deployment you will
need to create a tar file containing the GPG key used to sign the release JAR, encrypt this file
with Open SSL (OpenSSL 1.0.x).

    tar -cvf secrets.tar .gnupg
    openssl enc -aes-256-cbc -in secrets.tar -out secrets.tar.aes -k "${KEY}"

The encrypted file then should be stored on an external web site.

Finally the following enviroment variables should be set:

 1. `GPG_KEY_ID` The GPG Key to sign the artifacts with.
 1. `GPG_PASSWORD` The password to unlock the GPG Key.
 1. `KEY` The key to decrypt the tar file.
 1. `OSSRH_USERNAME` Username for [Nexus](https://oss.sonatype.org/)
 1. `OSSRH_PASSWORD` Username for [Nexus](https://oss.sonatype.org/)
 1. `SECRETS_URL` The location of the secrets (GPG key)