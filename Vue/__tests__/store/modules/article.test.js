import index from '@/store/index';
import article from '@/store/modules/article';
import {createLocalVue} from '@vue/test-utils';
import {cloneDeep} from 'lodash';
import axios from "axios";
import Vuex from 'vuex';
import {beforeEach, describe} from "@jest/globals";

const localVue = createLocalVue();
localVue.use(Vuex);

const replaceArticle={
    postedUser:{
        userid:1,
        uid:'uid',
        displayName:'user1',
        photoUrl:'url1'
    },
    articleId:1,
    title:'title',
    content:'content',
    stateFlag:1,
    articleVersion:1,
    tags:{
        tagId:1,
        tagName:'Java'
    },
    feedbacks:[
        {
        feedbackId:1,
        articleId:1,
        // createdAt:
        // updatedAt:
        content:'content',
        feedbackVersion:1,
        deleteFlag:0
    }],
    qiitaRecommendPoint:2
}

// actualな値
let url = ''
let apiToken = '';
let param=[]
let requestBody;
let mockError = false

// axiosのモック化
//jestのエラーを削除するとtestに失敗する
jest.mock('axios', () => ({
    get: jest.fn((_url, {
        params: _param,
        headers: {
            'Authorization': _apiToken
        }
    }) => {
        return new Promise((resolve) => {
            url = _url;
            apiToken = _apiToken;
            param=_param

            if (mockError) {
                throw Error("Mock Error")
            }

            resolve({
                data: null
            })
        })
    }),

    post: jest.fn((_url,_requestBody,{
        headers:{
            'Authorization': _apiToken
        }
    })=>{
        return new Promise((resolve) => {
            url = _url;
            apiToken = _apiToken;
            requestBody=_requestBody;

            if (mockError) {
                throw Error("Mock Error")
            }
            //insert予定の記事
            resolve({
                data: null
            })
        })
    })
}))

describe('store/articles.js', () => {

    //******************** actions ********************
    describe("actions", () => {

        let store;
        let commit
        let rootState
        let rootGetters
        beforeEach(() => {
            store = new Vuex.Store({
                state: {
                    API_URL: "http://localhost:8080/qiita_builder/"
                },
                getters: {
                    API_URL: state => state.API_URL
                },
                modules: {
                    auth: {
                        state: {
                            apiToken: 'token'
                        },
                        getters: {
                            apiToken: state => state.apiToken
                        }
                    }
                }
            })
            rootState = store.state
            rootGetters = store.getters;
            store.registerModule('articles', cloneDeep(article))
            commit = cloneDeep(jest).fn();
        })


    })

    //************ mutations ************
    describe('mutations', () => {
        const defaultArticle=  {
            postedUser: {},
            articleId: null,
            title: "",
            content: "",
            stateFlag: undefined,
            articleVersion: undefined,
            tags: undefined,
            feedbacks: [],
            qiitaRecommendPoint: null,
        }

        let store
        let state
        let commit
        beforeEach(() => {
            store = new Vuex.Store(cloneDeep(article))
            state = store.state
            commit = store.commit
        })

        // eslint-disable-next-line no-undef
        test('mutations: setArticle',()=>{
            expect(state.article).toStrictEqual(defaultArticle)
            commit('setArticle',replaceArticle)
            expect(state.article).toStrictEqual(replaceArticle)
        })

        test('mutations: mutateMarkDownText',()=>{
            expect(state.article.content).toBe(defaultArticle.content)
            commit('mutateMarkDownText','edit content')
            expect(state.article.content).toBe('edit content')
        })

        test('mutations: resetArticle',()=>{
            state.article=replaceArticle
            expect(state.article).toStrictEqual(replaceArticle)
            commit('resetArticle')
            expect(state.article).toStrictEqual(defaultArticle)
        })

        test('mutations: addFeedback',()=>{
            expect(state.article.feedbacks).toStrictEqual([])
            commit('addFeedback',replaceArticle.feedbacks)
            expect(state.article.feedbacks).toStrictEqual([replaceArticle.feedbacks])
        })

        test('mutations: updateFeedback',()=>{
            state.article.feedbacks=replaceArticle.feedbacks
            expect(state.article.feedbacks[0]).toStrictEqual(replaceArticle.feedbacks[0])
            const editFeedback=replaceArticle.feedbacks[0]
            editFeedback.title='edit title'
            editFeedback.content='edit content'
            commit('updateFeedback',editFeedback)
            expect(state.article.feedbacks[0]).toStrictEqual(editFeedback)
        })

        test('mutation: removeFeedback',()=>{
            state.article.feedbacks=replaceArticle.feedbacks
            expect(state.article.feedbacks).toStrictEqual(replaceArticle.feedbacks)
            commit('removeFeedback',1)
            expect(state.article.feedbacks).toStrictEqual([])
        })

        test('mutation: setMyArticleId',()=>{
            expect(state.myArticleId).toBe(null)
            commit('setMyArticleId',1)
            expect(state.myArticleId).toBe(1)
        })

        test('mutation: setRecommendId',()=>{
            expect(state.recommendId).toBe(null)
            commit('setRecommendId',1)
            expect(state.recommendId).toBe(1)
        })

        test('mutation: incrementQiitaRecommendPoint',()=>{
            state.article.qiitaRecommendPoint=1
            expect(state.article.qiitaRecommendPoint).toBe(1)
            commit('incrementQiitaRecommendPoint')
            expect(state.article.qiitaRecommendPoint).toBe(2)
        })

        test('mutation: decrementQiitaRecommendPoint',()=>{
            state.article.qiitaRecommendPoint=1
            expect(state.article.qiitaRecommendPoint).toBe(1)
            commit('decrementQiitaRecommendPoint')
            expect(state.article.qiitaRecommendPoint).toBe(0)
        })

        test('mutation: updateStateFlag',()=>{
            state.article.stateFlag=1
            expect(state.article.stateFlag).toBe(1)
            commit('updateStateFlag',2)
            expect(state.article.stateFlag).toBe(2)
        })

        test('mutation: toggleProcessFailure',()=>{
            state.article.processFailure=false
            expect(state.processFailure).toBeFalsy()
            commit('toggleProcessFailure')
            expect(state.processFailure).toBeTruthy()
        })
    })
})


