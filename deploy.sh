#!/bin/bash
echo "Running in..."
pwd
echo "dir listing:"
ls
echo "Decrypting magic"
./de-secret.sh
echo "Decrypted"
ls ~/.gradle/
ls ~/.gnupg/
echo "Gradling the gradles"
./gradlew uploadArchives closeAndReleaseRepository
echo "Sorted!"