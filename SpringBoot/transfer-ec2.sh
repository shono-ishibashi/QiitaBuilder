#!/bin/sh

# jarファイルをビルド
mvn package -DskipTests=true
# ファイル転送
sftp -i ~/aws/my-key.pem ec2-user@api.qiita-builder.ga <<< $'put ./target/qiita-builder-0.0.1-SNAPSHOT.jar'

# インスタンスにssh接続
ssh -i ~/aws/my-key.pem ec2-user@api.qiita-builder.ga


