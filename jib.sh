#!/bin/bash
imageName=$1
imageTag=$2
profile=$3
echo "==== Jib 빌드 시작 ===="
echo "대상 이미지 이름: $imageName"
echo "대상 이미지 태그: $imageTag"
echo "대상 프로파일: $profile"

export IMAGE_TAG=$imageTag
export ACTIVE_PROFILE=$profile
export IMAGE_NAME=$imageName
./gradlew jib -DimageName="$imageName" -DimageTag="$imageTag" -Dspring.profiles.active="$profile"
