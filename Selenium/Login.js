const { Builder, By, Capabilities, Key, until } = require("selenium-webdriver");

exports.login = async (BASE_URL) => {
  const driver = await new Builder().forBrowser("chrome").build();
  try {
    // QiitaBuilderのトップページへアクセス
    await driver.get(BASE_URL + "/login");
    // パートナーズでログイン
    await driver
      .wait(until.elementLocated(By.id("rp_login_btn")), 5000)
      .click();
    // Googleリダイレクト
    driver.getCurrentUrl(function (current_url) {
      if (current_url !== target_url) {
        throw "redirection doesn't work correctly";
      }
    });
    // アドレスを入力してEnter
    await driver
      .wait(until.elementLocated(By.name("identifier")), 10000)
      .sendKeys("soshi.morita", Key.RETURN);
    // パスワードを入力してEnter
    // await driver
    //   .wait(until.elementLocated(By.name("password")), 10000)
    //   .sendKeys("eqwrtqwtq34t55345", Key.RETURN);
  } catch (e) {
    console.log(e);
  } finally {
    // Chromeを終了
    driver && (await driver.quit());
  }
};

exports.test2 = async (query) => {
  console.log(query);
};
