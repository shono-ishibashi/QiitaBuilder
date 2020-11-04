package com.qiitabuilder.controller;

import com.qiitabuilder.domain.QiitaConfiguration;
import com.qiitabuilder.service.QiitaAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
@RequestMapping(value = "/qiita")
public class QiitaAPIController {

    @Autowired
    private QiitaAPIService qiitaAPIService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String test() {
        return "redirect:https://www.google.com/";
    }

    @RequestMapping(value = "/to-qiita-api-authentication", method = RequestMethod.GET)
    public String toQiitaAPIAuthentication() {


        String request = qiitaAPIService.generateQiitaAPIAuthenticationURL();

        return "redirect:" + request;
    }

    @RequestMapping(value = "/check-qiita-api-authentication", method = RequestMethod.GET)
    public String checkQiitaAPIAuthentication(QiitaConfiguration qiitaConfiguration) {


        if (qiitaAPIService.existsStateInDB(qiitaConfiguration)) {

            qiitaAPIService.updateCode(qiitaConfiguration);
            //マイページ画面へ遷移させて、完了メッセージを出力
            return "redirect:https://www.google.com/";
        }

        qiitaAPIService.deleteByUserId(qiitaConfiguration.getUserId());
        //マイページ画面へ遷移させて、エラーメッセージを取得
        return "redirect:https://www.google.com/";
    }


}
