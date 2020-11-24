const { Builder, By, Capabilities, Key, until } = require("selenium-webdriver");
// 使用するモジュール
const Login = require("./Login.js");
const Test = require("./Test.js");

// 共通で使う変数
const BASE_URL = "http://localhost:8083";

// 実行するメソッド
// Test.search("java")
Login.login(BASE_URL);
