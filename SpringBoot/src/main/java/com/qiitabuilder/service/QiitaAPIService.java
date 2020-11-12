package com.qiitabuilder.service;

import com.qiitabuilder.domain.*;
import com.qiitabuilder.mapper.ArticleMapper;
import com.qiitabuilder.mapper.QiitaConfigurationMapper;
import com.qiitabuilder.mapper.UserMapper;
import com.qiitabuilder.security.SimpleLoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static java.util.Objects.isNull;


/**
 * QiitaAPIと接続し、処理を行うクラス
 */

@Service
public class QiitaAPIService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QiitaConfigurationMapper qiitaConfigurationMapper;

    @Autowired
    private ArticleMapper articleMapper;

    final String CLIENT_ID = "59ba99a3357cf730bbcab40fde87bb06f3876124";
    final String CLIENT_SECRET = "6dde7331beda9a58b7665d9767c9dd6b6241198b";
    final String SCOPE = "scope=read_qiita+write_qiita";


    public void restTemplateTest() {
        final String URL = "https://qiita.com/api/v2/items";

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("body", "# テスト from StringBoot");
        requestBody.put("title", "テスト from StringBoot");
        requestBody.put("private", false);

        Map<String, Object> tag1 = new HashMap<>();
        tag1.put("name", "java");
        tag1.put("versions", new ArrayList<>());

        List<Map<String, Object>> tags = new ArrayList<>();
        tags.add(tag1);

        requestBody.put("tags", tags);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Bearer 037eefff6e3c7ad9142fe1134fe78ebc9d0603df");

        // build the request
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(URL, request, String.class);

        System.out.println(response);

    }

    /**
     * 正常なリクエストか判断するためのstateを作成し、Qiita APIの認証画面へ遷移させるURLを作成するメソッド
     *
     * @return QiitaAPIの認証画面へのURL
     */
    public String generateQiitaAPIAuthenticationURL() {


        final String BASE_URL = "https://qiita.com/api/v2/oauth/authorize";

        String state = UUID.randomUUID().toString();

        String request = BASE_URL + '?' +
                "client_id=" + CLIENT_ID +
                "&scope=" + SCOPE +
                "&state=" + state;

        QiitaConfiguration qiitaConfiguration = new QiitaConfiguration();
        //ログイン中のユーザ情報をセット
        SimpleLoginUser loginUser = (SimpleLoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        qiitaConfiguration.setUserId(loginUser.getUser().getUserId());

        qiitaConfiguration.setState(state);

        //DBに既に、stateが存在すれば、UPDATE、なければINSERT
        if (isNull(qiitaConfigurationMapper.getStateByUserId(loginUser.getUser().getUserId()))) {
            qiitaConfigurationMapper.insertQiitaConfiguration(qiitaConfiguration);
        } else {
            qiitaConfigurationMapper.updateState(qiitaConfiguration);
        }

        return request;
    }

    /**
     * Qiitaからリダイレクトされてきたstateがデータベースに存在するstateが同じかをを確認し、同じだったらcodeをデータベースに追加する
     *
     * @return 同じする:true 違う:false
     */

    public boolean isAuthenticated(QiitaConfiguration qiitaConfiguration) {
        //DB 内のstate
        String stateInDB = qiitaConfigurationMapper.getStateByUserId(qiitaConfiguration.getUserId());

        if (Objects.equals(qiitaConfiguration.getState(), stateInDB)) {
            qiitaConfigurationMapper.updateQiitaConfigurationCode(qiitaConfiguration);
            return true;
        } else {
            qiitaConfigurationMapper.deleteByUserId(qiitaConfiguration.getUserId());
            return false;
        }
    }

    /**
     *
     * QiitaAPIのトークンをインサートするメソッド
     *
     */
    public void saveToken() {
        SimpleLoginUser loginUser = (SimpleLoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String code = qiitaConfigurationMapper.getCodeByUserId(loginUser.getUser().getUserId());

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("client_id", CLIENT_ID);
        requestBody.put("client_secret", CLIENT_SECRET);
        requestBody.put("code", code);

        final String URL = "https://qiita.com/api/v2/access_tokens";

        Map<String, Object> response = restTemplate.postForObject(URL, requestBody, Map.class);

        qiitaConfigurationMapper.saveToken(String.valueOf(response.get("token")), loginUser.getUser().getUserId());

    }

    /**
     *
     * Qiitaに記事を投稿・更新するメソッド
     *
     * @param articleId 投稿する記事のID
     */
    public void saveArticleToQiita(Integer articleId) {

        SimpleLoginUser loginUser = (SimpleLoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        final String URL = "https://qiita.com/api/v2/items";

        //Qiitaに投稿する記事を取得
        Article article = articleMapper.load(articleId);

        article.setPostedUser(loginUser.getUser());

        //Qiitaに投稿するためにデータを整形
        //===============================================================================================================
        Map<String, Object> articleForQiita = new LinkedHashMap<>();

        articleForQiita.put("body", article.getContent());
        articleForQiita.put("title", article.getTitle());
        List<TagForQiita> articleForQiitaTags = new ArrayList<>();
        article.getTags().forEach(tag ->
                articleForQiitaTags.add(new TagForQiita(tag.getTagName(), new ArrayList<>()))
        );

        articleForQiita.put("tags", articleForQiitaTags);
        articleForQiita.put("private", false);


        //Httpリクエストの作成
        //===============================================================================================================
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Bearer " + qiitaConfigurationMapper.getTokenByUserId(loginUser.getUser().getUserId()));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(articleForQiita, headers);

        if (isNull(article.getQiitaArticleId())) {
            Map<String, Object> response = restTemplate.postForObject(URL, request, Map.class);
            article.setQiitaArticleId((String) response.get("id"));

            //Qiitaに投稿済みのフラッグ
            article.setStateFlag(2);
            articleMapper.updateArticle(article);
        } else {
            Map<String, Object> response = restTemplate.patchForObject(URL + '/' + article.getQiitaArticleId(), request, Map.class);
        }

    }
}
