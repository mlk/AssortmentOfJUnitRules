#!/bin/bash
./de-secret.sh && ./gradlew uploadArchives closeAndReleaseRepository
