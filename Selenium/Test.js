const { Builder, By, Capabilities, Key, until } = require("selenium-webdriver");

exports.search = async (query) => {
  const driver = await new Builder().forBrowser("chrome").build();

  try {
    // Qiitaのトップページへアクセス
    await driver.get("https://qiita.com/");

    // 検索欄にキーワードを入力してEnter
    await driver
      .wait(until.elementLocated(By.name("q")), 5000)
      .sendKeys(query, Key.RETURN);
    // 検索結果数のバッジの値を取得
    const result = await driver
      .wait(until.elementLocated(By.className("badge")), 10000)
      .getText();
    // キーワードと結果の値を出力
    console.log(`${query}: ${result}`);
  } catch (e) {
    console.log(e);
  } finally {
    // Chromeを終了
    driver && (await driver.quit());
  }
};
