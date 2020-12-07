import index from '@/store/index';
import article from '@/store/modules/article';
import {createLocalVue} from '@vue/test-utils';
import {cloneDeep} from 'lodash';
import axios from "axios";
import Vuex from 'vuex';
import {beforeEach, describe} from "@jest/globals";

const localVue = createLocalVue();
localVue.use(Vuex);

const replaceArticle = {
    postedUser: {
        userid: 1,
        uid: 'uid',
        displayName: 'user1',
        photoUrl: 'url1'
    },
    articleId: 1,
    title: 'title',
    content: 'content',
    stateFlag: 1,
    articleVersion: 1,
    tags: {
        tagId: 1,
        tagName: 'Java'
    },
    feedbacks: [
        {
            feedbackId: 1,
            articleId: 1,
            // createdAt:
            // updatedAt:
            content: 'content',
            feedbackVersion: 1,
            deleteFlag: 0
        }],
    qiitaRecommendPoint: 2
}

// actualな値
let url = ''
let apiToken = ''
let param = []
let requestBody = []
let mockHttpStatus = 200
let mockReturn = null
let mockError = false
let httpStatus = ''

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
            param = _param

            if (mockError) {
                if (mockHttpStatus === 400) {
                    httpStatus = 400
                    throw  Object.assign(new Error('BAD REQUEST'), {
                            name: 'axios error',
                            response: {status: 400}
                        }
                    );
                }
                if (mockHttpStatus === 404) {
                    httpStatus = 404
                    throw  Object.assign(new Error('NOT FOUND'), {
                        name: 'axios error',
                        response: {status: 404}
                    });
                }
                if (mockHttpStatus === 409) {
                    httpStatus = 409
                    throw  Object.assign(new Error('NOT FOUND'), {
                        name: 'axios error',
                        response: {status: 404}
                    });
                }
                if (mockHttpStatus === 500) {
                    httpStatus = 500
                    throw   Object.assign(new Error('Internal Server Error'), {
                        name: 'axios error',
                        response: {status: 500}
                    });
                }
            }

            resolve({
                data: mockReturn
            })
        })
    }),

    post: jest.fn((_url, _requestBody, {
        headers: {
            'Authorization': _apiToken
        }
    }) => {
        return new Promise((resolve) => {
            url = _url;
            apiToken = _apiToken;
            requestBody = _requestBody;

            if (mockError) {
                if (mockHttpStatus === 400) {
                    httpStatus = 400
                    throw  Object.assign(new Error('BAD REQUEST'), {
                            name: 'axios error',
                            response: {status: 400}
                        }
                    );
                }
                if (mockHttpStatus === 404) {
                    httpStatus = 404
                    throw  Object.assign(new Error('NOT FOUND'), {
                        name: 'axios error',
                        response: {status: 404}
                    });
                }
                if (mockHttpStatus === 409) {
                    httpStatus = 409
                    throw  Object.assign(new Error('NOT FOUND'), {
                        name: 'axios error',
                        response: {status: 404}
                    });
                }
                if (mockHttpStatus === 500) {
                    httpStatus = 500
                    throw   Object.assign(new Error('Internal Server Error'), {
                        name: 'axios error',
                        response: {status: 500}
                    });
                }
            }
            // update予定の記事
            resolve({
                data: mockReturn
            })
        })
    }),

    put: jest.fn((_url, _requestBody, {
        headers: {
            'Authorization': _apiToken
        }
    }) => {
        return new Promise((resolve) => {
            url = _url;
            apiToken = _apiToken;
            requestBody = _requestBody;

            if (mockError) {
                if (mockHttpStatus === 400) {
                    httpStatus = 400
                    throw  Object.assign(new Error('BAD REQUEST'), {
                            name: 'axios error',
                            response: {status: 400}
                        }
                    );
                }
                if (mockHttpStatus === 404) {
                    httpStatus = 404
                    throw  Object.assign(new Error('NOT FOUND'), {
                        name: 'axios error',
                        response: {status: 404}
                    });
                }
                if (mockHttpStatus === 409) {
                    httpStatus = 409
                    throw  Object.assign(new Error('NOT FOUND'), {
                        name: 'axios error',
                        response: {status: 404}
                    });
                }
                if (mockHttpStatus === 500) {
                    httpStatus = 500
                    throw   Object.assign(new Error('Internal Server Error'), {
                        name: 'axios error',
                        response: {status: 500}
                    });
                }
            }
            // update予定の記事
            resolve({
                data: mockReturn
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
            store.registerModule('article', cloneDeep(article))
            commit = cloneDeep(jest).fn();
            // dispatch = cloneDeep(jest).fn();
        })

        test('actions: fetchArticle', async () => {
            const articleId = 1
            mockReturn = replaceArticle
            await article.actions.fetchArticle({commit, rootGetters, rootState}, articleId)
            await expect(url).toBe('http://localhost:8080/qiita_builder/article/1')
            await expect(apiToken).toBe('token')
            await expect(commit).toHaveBeenCalledTimes(1)
            await expect(commit).toHaveBeenCalledWith('setArticle', replaceArticle)
        })

        test('actions: fetchArticle(400error)', async () => {
            const articleId = 'a'
            mockError = true
            mockHttpStatus = 400
            try {
                await article.actions.fetchArticle({commit, rootGetters, rootState}, articleId)
            } catch (error) {
                await expect(url).toBe('http://localhost:8080/qiita_builder/article/a')
                await expect(apiToken).toBe('token')
                await expect(httpStatus).toHaveBeenCalledTimes(400)
            }
        })

        test('actions: saveArticle(post)', async () => {

        })

        test('actions: saveArticle(post,400error)', async () => {

        })

        test('actions: saveArticle(put)', async () => {

        })

        test('actions: saveArticle(put,400error)', async () => {

        })

        test('actions: resetArticle', async () => {

        })

        test('actions: commitMarkDownText', async () => {

        })

        test('actions: postFeedback', async () => {

        })

        test('actions: postFeedback(400error)', async () => {

        })

        test('actions: postFeedback(409error)', async () => {

        })

        test('actions: updateFeedback', async () => {

        })

        test('actions: updateFeedback(400error)', async () => {

        })

        test('actions: updateFeedback(409error)', async () => {

        })

        test('actions: deleteFeedback', async () => {

        })

        test('actions: deleteFeedback(400error)', async () => {

        })

        test('actions: deleteFeedback(409error)', async () => {

        })

        test('actions: fetchMyArticle', async () => {

        })

        test('actions: fetchMyArticle(400error)', async () => {

        })

        test('actions: registerMyArticle', async () => {

        })

        test('actions: registerMyArticle(400error)', async () => {

        })

        test('actions: deleteMyArticle', async () => {

        })

        test('actions: deleteMyArticle(400error)', async () => {

        })

        test('actions: fetchRecommend', async () => {

        })

        test('actions: fetchRecommend(400error)', async () => {

        })

        test('actions: registerRecommend', async () => {

        })

        test('actions: registerRecommend(400error)', async () => {

        })

        test('actions: deleteRecommend', async () => {

        })

        test('actions: deleteRecommend(400error)', async () => {

        })

        test('actions: fetchArticleEdit', async () => {

        })

        test('actions: fetchArticleEdit(400error)', async () => {

        })

        test('actions: toggleProcessFailure', async () => {

        })
    })

    //************ mutations ************
    describe('mutations', () => {
        const defaultArticle = {
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
        test('mutations: setArticle', () => {
            expect(state.article).toStrictEqual(defaultArticle)
            commit('setArticle', replaceArticle)
            expect(state.article).toStrictEqual(replaceArticle)
        })

        test('mutations: mutateMarkDownText', () => {
            expect(state.article.content).toBe(defaultArticle.content)
            commit('mutateMarkDownText', 'edit content')
            expect(state.article.content).toBe('edit content')
        })

        test('mutations: resetArticle', () => {
            state.article = replaceArticle
            expect(state.article).toStrictEqual(replaceArticle)
            commit('resetArticle')
            expect(state.article).toStrictEqual(defaultArticle)
        })

        test('mutations: addFeedback', () => {
            expect(state.article.feedbacks).toStrictEqual([])
            commit('addFeedback', replaceArticle.feedbacks)
            expect(state.article.feedbacks).toStrictEqual([replaceArticle.feedbacks])
        })

        test('mutations: updateFeedback', () => {
            state.article.feedbacks = replaceArticle.feedbacks
            expect(state.article.feedbacks[0]).toStrictEqual(replaceArticle.feedbacks[0])
            const editFeedback = replaceArticle.feedbacks[0]
            editFeedback.title = 'edit title'
            editFeedback.content = 'edit content'
            commit('updateFeedback', editFeedback)
            expect(state.article.feedbacks[0]).toStrictEqual(editFeedback)
        })

        test('mutation: removeFeedback', () => {
            state.article.feedbacks = replaceArticle.feedbacks
            expect(state.article.feedbacks).toStrictEqual(replaceArticle.feedbacks)
            commit('removeFeedback', 1)
            expect(state.article.feedbacks).toStrictEqual([])
        })

        test('mutation: setMyArticleId', () => {
            expect(state.myArticleId).toBe(null)
            commit('setMyArticleId', 1)
            expect(state.myArticleId).toBe(1)
        })

        test('mutation: setRecommendId', () => {
            expect(state.recommendId).toBe(null)
            commit('setRecommendId', 1)
            expect(state.recommendId).toBe(1)
        })

        test('mutation: incrementQiitaRecommendPoint', () => {
            state.article.qiitaRecommendPoint = 1
            expect(state.article.qiitaRecommendPoint).toBe(1)
            commit('incrementQiitaRecommendPoint')
            expect(state.article.qiitaRecommendPoint).toBe(2)
        })

        test('mutation: decrementQiitaRecommendPoint', () => {
            state.article.qiitaRecommendPoint = 1
            expect(state.article.qiitaRecommendPoint).toBe(1)
            commit('decrementQiitaRecommendPoint')
            expect(state.article.qiitaRecommendPoint).toBe(0)
        })

        test('mutation: updateStateFlag', () => {
            state.article.stateFlag = 1
            expect(state.article.stateFlag).toBe(1)
            commit('updateStateFlag', 2)
            expect(state.article.stateFlag).toBe(2)
        })

        test('mutation: toggleProcessFailure', () => {
            state.article.processFailure = false
            expect(state.processFailure).toBeFalsy()
            commit('toggleProcessFailure')
            expect(state.processFailure).toBeTruthy()
        })
    })
})


