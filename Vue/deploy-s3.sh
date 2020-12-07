#!/bin/sh

# バケットとdistディレクトリを同期
aws s3 sync ./dist s3://qiita-builder.ga/ --delete
