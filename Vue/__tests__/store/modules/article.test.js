import index from '@/store/index';
import article from '@/store/modules/article';
import {createLocalVue} from '@vue/test-utils';
import {cloneDeep} from 'lodash';
import axios from "axios";
import Vuex from 'vuex';
import {beforeEach, describe} from "@jest/globals";
import QiitaAPI from "@/mixins/QiitaAPI";

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
    feedbacks: [],
    qiitaRecommendPoint: 2
}

const replaceInsertArticle = {
    postedUser: {
        userid: 1,
        uid: 'uid',
        password: 'password',
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
}

// actualな値
let url = ''
let apiToken = ''
let param = []
let requestBody = []
let mockHttpStatus = 200
let mockReturn = null
let mockErrorReturn = null
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
            param = _param

            if (mockError) {
                if (mockHttpStatus === 400) {
                    throw  Object.assign(new Error('BAD REQUEST'), {
                            name: 'axios error',
                            response: {status: 400}
                        }
                    );
                }
                if (mockHttpStatus === 404) {
                    throw  Object.assign(new Error('NOT FOUND'), {
                        name: 'axios error',
                        response: {status: 404}
                    });
                }
                if (mockHttpStatus === 409) {
                    throw  Object.assign(new Error('CONFLICT'), {
                        name: 'axios error',
                        response: {status: 409}
                    });
                }
                if (mockHttpStatus === 500) {
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
                    throw  Object.assign(new Error('BAD REQUEST'), {
                            name: 'axios error',
                            response: {
                                status: 400,
                                data: {message: mockErrorReturn}
                            }
                        }
                    );
                }
                if (mockHttpStatus === 401) {
                    throw  Object.assign(new Error('Unauthorized'), {
                            name: 'axios error',
                            response: {
                                status: 401,
                                data: {message: mockErrorReturn}
                            }
                        }
                    );
                }
                if (mockHttpStatus === 403) {
                    throw  Object.assign(new Error('Forbidden'), {
                            name: 'axios error',
                            response: {
                                status: 403,
                                data: {message: mockErrorReturn}
                            }
                        }
                    );
                }
                if (mockHttpStatus === 404) {
                    throw  Object.assign(new Error('NOT FOUND'), {
                        name: 'axios error',
                        response: {
                            status: 404,
                            data: {message: mockErrorReturn}
                        }
                    });
                }
                if (mockHttpStatus === 409) {
                    throw  Object.assign(new Error('CONFLICT'), {
                        name: 'axios error',
                        response: {
                            status: 409,
                            data: {message: mockErrorReturn}
                        }
                    });
                }
                if (mockHttpStatus === 500) {
                    throw   Object.assign(new Error('Internal Server Error'), {
                        name: 'axios error',
                        response: {
                            status: 500,
                            data: {message: mockErrorReturn}
                        }
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
                    throw  Object.assign(new Error('BAD REQUEST'), {
                            name: 'axios error',
                            response: {status: 400}
                        }
                    );
                }
                if (mockHttpStatus === 404) {
                    throw  Object.assign(new Error('NOT FOUND'), {
                        name: 'axios error',
                        response: {status: 404}
                    });
                }
                if (mockHttpStatus === 409) {
                    throw  Object.assign(new Error('CONFLICT'), {
                        name: 'axios error',
                        response: {status: 409}
                    });
                }
                if (mockHttpStatus === 500) {
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

    delete: jest.fn((_url, {
        headers: {
            'Authorization': _apiToken
        }
    }) => {
        return new Promise((resolve) => {
            url = _url;
            apiToken = _apiToken;

            if (mockError) {
                if (mockHttpStatus === 400) {
                    throw  Object.assign(new Error('BAD REQUEST'), {
                            name: 'axios error',
                            response: {status: 400}
                        }
                    );
                }
                if (mockHttpStatus === 404) {
                    throw  Object.assign(new Error('NOT FOUND'), {
                        name: 'axios error',
                        response: {status: 404}
                    });
                }
                if (mockHttpStatus === 409) {
                    throw  Object.assign(new Error('CONFLICT'), {
                        name: 'axios error',
                        response: {status: 409}
                    });
                }
                if (mockHttpStatus === 500) {
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


// まとめてテストを行うと失敗するが、個別でテストを行うと成功する
describe('store/articles.js', () => {

    //******************** actions ********************
    describe("actions", () => {

        let store
        let commit
        let dispatch
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
                            apiToken: 'token',
                            isLinkedToQiita: null
                        },
                        getters: {
                            apiToken: state => state.apiToken,
                            isLinkedToQiita: state => state.isLinkedToQiita
                        }
                    },
                }
            })
            rootState = store.state
            rootGetters = store.getters;
            store.registerModule('article', cloneDeep(article))
            commit = cloneDeep(jest).fn();
            dispatch = cloneDeep(jest).fn();
        })

        // alertのtestは未実装
        test('actions: postArticleToQiita(isLinkedToQiita=true)', async () => {
            const articleId = 1
            rootState.auth.isLinkedToQiita = 'qiitaToken'

            window.alert = () => {
                return 'Qiitaに記事を投稿しました'
            }

            await article.actions.postArticleToQiita({commit, rootGetters, rootState, dispatch}, articleId)
            await expect(url).toBe('http://localhost:8080/qiita_builder/qiita/save-article-to-qiita/1')
            await expect(apiToken).toBe('token')
            await expect(commit).toHaveBeenCalledTimes(1)
            await expect(commit).toHaveBeenCalledWith('updateStateFlag', 2)
        })

        test('actions: postArticleToQiita(isLinkedToQiita=true && error401)', async () => {
            const articleId = 1
            rootState.auth.isLinkedToQiita = 'qiitaToken'
            rootState.auth.apiToken = null
            mockErrorReturn = 'QiitaAPI'
            mockError = true
            mockHttpStatus = 401
            mockErrorReturn = 'QiitaAPI'

            window.confirm = () => {
                return true
            }

            try {
                await article.actions.postArticleToQiita({commit, rootGetters, rootState, dispatch}, articleId)
            } catch (error) {
                await expect(url).toBe('http://localhost:8080/qiita_builder/qiita/save-article-to-qiita/1')
                await expect(apiToken).toBe(null)
                await expect(error.name).toBe("axios error")
                await expect(error.response.status).toBe(401)
                window.location.assign = jest.fn()
                await expect(location.assign).toBeCalledWith('')
            }
        })

        test('actions: postArticleToQiita(isLinkedToQiita=true && error403)', async () => {
            const articleId = 1
            rootState.auth.isLinkedToQiita = 'qiitaToken'
            mockError = true
            mockHttpStatus = 403
            mockErrorReturn = 'QiitaAPI'

            window.alert = () => {
                return 'この記事を更新する権限はありません'
            }

            try {
                await article.actions.postArticleToQiita({commit, rootGetters, rootState, dispatch}, articleId)
            } catch (error) {
                await expect(url).toBe('http://localhost:8080/qiita_builder/qiita/save-article-to-qiita/1')
                await expect(apiToken).toBe('token')
                await expect(error.name).toBe("axios error")
                await expect(error.response.status).toBe(403)
            }
        })

        // 微妙（confirmの後のcommit,dispatchのテストが反応しない）
        test('actions: postArticleToQiita(isLinkedToQiita=true && error404 && confirm=true )', async () => {
            const articleId = 1
            rootState.auth.isLinkedToQiita = 'qiitaToken'
            mockError = true
            mockHttpStatus = 404
            mockErrorReturn = 'QiitaAPI'

            window.confirm = () => {
                return true
            }

            try {
                await article.actions.postArticleToQiita({commit, rootGetters, rootState, dispatch}, articleId)
            } catch (error) {
                await expect(url).toBe('http://localhost:8080/qiita_builder/qiita/save-article-to-qiita/1')
                await expect(apiToken).toBe('token')
                await expect(error.name).toBe("axios error")
                await expect(error.response.status).toBe(404)
                await expect(commit).toHaveBeenCalledTimes(1)
                await expect(commit).toHaveBeenCalledWith('updateStateFlag', 2)
                await expect(dispatch).toHaveBeenCalledTimes(1)
                await expect(dispatch).toHaveBeenCalledWith('postArticleToQiita',articleId)
            }
        })

        // 微妙（confirmの後のcommit,dispatchのテストが反応しない）
        test('actions: postArticleToQiita(isLinkedToQiita=true && error404 && confirm=false )', async () => {
            const articleId = 1
            rootState.auth.isLinkedToQiita = 'qiitaToken'
            mockError = true
            mockHttpStatus = 404
            mockErrorReturn = 'QiitaAPI'

            window.confirm = () => {
                return false
            }

            try {
                await article.actions.postArticleToQiita({commit, rootGetters, rootState, dispatch}, articleId)
            } catch (error) {
                await expect(url).toBe('http://localhost:8080/qiita_builder/qiita/save-article-to-qiita/1')
                await expect(apiToken).toBe('token')
                await expect(error.name).toBe("axios error")
                await expect(error.response.status).toBe(404)
                await expect(commit).toHaveBeenCalledTimes(1)
                await expect(commit).toHaveBeenCalledWith('updateStateFlag', 1)
            }
        })

        test('actions: postArticleToQiita(isLinkedToQiita=true && elseError)', async () => {
            const articleId = 1
            rootState.auth.isLinkedToQiita = 'qiitaToken'
            mockError = true
            mockHttpStatus = 500
            mockErrorReturn = 'QiitaAPI'

            window.confirm = () => {
                return true
            }

            try {
                await article.actions.postArticleToQiita({commit, rootGetters, rootState, dispatch}, articleId)
            } catch (error) {
                await expect(url).toBe('http://localhost:8080/qiita_builder/qiita/save-article-to-qiita/1')
                await expect(apiToken).toBe('token')
                await expect(error.name).toBe("axios error")
                await expect(error.response.status).toBe(500)
            }
        })

        // navigationのtest
        test('actions: postArticleToQiita', async () => {
            const articleId=1
            await article.actions.postArticleToQiita({commit, rootGetters, rootState, dispatch}, articleId)

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
            const articleId = 'error'
            mockError = true
            mockHttpStatus = 400
            try {
                await article.actions.fetchArticle({commit, rootGetters, rootState}, articleId)
            } catch (error) {
                await expect(url).toBe('http://localhost:8080/qiita_builder/article/error')
                await expect(apiToken).toBe('token')
                await expect(error.name).toBe("axios error")
                await expect(error.response.status).toBe(400)
            }
        })

        test('actions: fetchArticle(500error)', async () => {
            const articleId = 'error'
            mockError = true
            mockHttpStatus = 500
            try {
                await article.actions.fetchArticle({commit, rootGetters, rootState}, articleId)
            } catch (error) {
                await expect(url).toBe('http://localhost:8080/qiita_builder/article/error')
                await expect(apiToken).toBe('token')
                await expect(error.name).toBe("axios error")
                await expect(error.response.status).toBe(500)
            }
        })

        // 記事投稿用のrequestBody
        const insertRequestBody = {
            articleId: null,
            title: 'title',
            content: 'content',
            stateFlag: 1,
            tags: {
                tagId: 1,
                tagName: 'Java'
            },
        }

        test('actions: saveArticle(post)', async () => {
            mockReturn = replaceInsertArticle

            await article.actions.saveArticle({rootState, rootGetters}, insertRequestBody)
            await expect(url).toBe('http://localhost:8080/qiita_builder/article/')
            await expect(apiToken).toBe('token')
            await expect(requestBody).toStrictEqual(insertRequestBody)
        })

        test('actions: saveArticle(post,400error)', async () => {
                mockError = true
                mockHttpStatus = 400
                insertRequestBody.content = ''
                try {
                    await article.actions.saveArticle({rootState, rootGetters}, insertRequestBody)
                } catch (error) {
                    await expect(url).toBe('http://localhost:8080/qiita_builder/article/')
                    await expect(apiToken).toBe('token')
                    await expect(error.name).toBe("axios error")
                    await expect(error.response.status).toBe(400)
                }
            }
        )

        test('actions: saveArticle(post,500error)', async () => {
                mockError = true
                mockHttpStatus = 500
                insertRequestBody.content = ''
                try {
                    await article.actions.saveArticle({rootState, rootGetters}, insertRequestBody)
                } catch (error) {
                    await expect(url).toBe('http://localhost:8080/qiita_builder/article/')
                    await expect(apiToken).toBe('token')
                    await expect(error.name).toBe("axios error")
                    await expect(error.response.status).toBe(500)
                }
            }
        )

        test('actions: saveArticle(put)', async () => {
            mockReturn = replaceInsertArticle
            insertRequestBody.articleId = 1
            insertRequestBody.articleVersion = 1
            await article.actions.saveArticle({rootState, rootGetters}, insertRequestBody)
            await expect(url).toBe('http://localhost:8080/qiita_builder/article/')
            await expect(apiToken).toBe('token')
            await expect(requestBody).toStrictEqual(insertRequestBody)
        })

        test('actions: saveArticle(put,400error)', async () => {
                mockError = true
                insertRequestBody.articleId = 1
                insertRequestBody.articleVersion = 1
                mockHttpStatus = 400
                insertRequestBody.content = ''
                try {
                    await article.actions.saveArticle({rootState, rootGetters}, insertRequestBody)
                } catch (error) {
                    await expect(url).toBe('http://localhost:8080/qiita_builder/article/')
                    await expect(apiToken).toBe('token')
                    await expect(error.name).toBe("axios error")
                    await expect(error.response.status).toBe(400)
                }
            }
        )

        test('actions: saveArticle(put,500error)', async () => {
                mockError = true
                insertRequestBody.articleId = 1
                insertRequestBody.articleVersion = 1
                mockHttpStatus = 500
                insertRequestBody.content = ''
                try {
                    await article.actions.saveArticle({rootState, rootGetters}, insertRequestBody)
                } catch (error) {
                    await expect(url).toBe('http://localhost:8080/qiita_builder/article/')
                    await expect(apiToken).toBe('token')
                    await expect(error.name).toBe("axios error")
                    await expect(error.response.status).toBe(500)
                }
            }
        )

        test('actions: resetArticle', async () => {
            await article.actions.resetArticle({commit})
            await expect(commit).toHaveBeenCalledTimes(1)
            await expect(commit).toHaveBeenCalledWith('resetArticle')
        })

        test('actions: commitMarkDownText', async () => {
            const text = 'text'
            await article.actions.commitMarkDownText({commit}, text)
            await expect(commit).toHaveBeenCalledTimes(1)
            await expect(commit).toHaveBeenCalledWith('mutateMarkDownText', text)
        })

        //フィードバック投稿用のrequestBody
        const feedbackRequestBody = {
            feedbackId: null,
            articleId: 1,
            content: 'content',
            deleteFlag: 0
        }

        test('actions: postFeedback', async () => {
            await article.actions.postFeedback({commit, rootState, rootGetters}, feedbackRequestBody)
            await expect(url).toBe('http://localhost:8080/qiita_builder/feedback')
            await expect(apiToken).toBe('token')
            await expect(requestBody).toStrictEqual(feedbackRequestBody)
        })

        test('actions: postFeedback(400error)', async () => {
            mockError = true
            mockHttpStatus = 400
            feedbackRequestBody.content = ''
            try {
                await article.actions.postFeedback({commit, rootState, rootGetters}, feedbackRequestBody)
            } catch (error) {
                await expect(url).toBe('http://localhost:8080/qiita_builder/feedback')
                await expect(apiToken).toBe('token')
                await expect(error.name).toBe("axios error")
                await expect(error.response.status).toBe(400)
            }
        })

        test('actions: postFeedback(409error)', async () => {
            mockError = true
            mockHttpStatus = 409
            feedbackRequestBody.articleId = 2
            try {
                await article.actions.postFeedback({commit, rootState, rootGetters}, feedbackRequestBody)
            } catch (error) {
                await expect(url).toBe('http://localhost:8080/qiita_builder/feedback')
                await expect(apiToken).toBe('token')
                await expect(error.name).toBe("axios error")
                await expect(error.response.status).toBe(409)
            }
        })

        test('actions: postFeedback(500error)', async () => {
            mockError = true
            mockHttpStatus = 500
            feedbackRequestBody.articleId = 2
            try {
                await article.actions.postFeedback({commit, rootState, rootGetters}, feedbackRequestBody)
            } catch (error) {
                await expect(url).toBe('http://localhost:8080/qiita_builder/feedback')
                await expect(apiToken).toBe('token')
                await expect(error.name).toBe("axios error")
                await expect(error.response.status).toBe(500)
            }
        })

        test('actions: updateFeedback', async () => {
            feedbackRequestBody.feedbackId = 1
            feedbackRequestBody.content = 'updateContent'
            feedbackRequestBody.feedbackVersion = 1
            mockReturn = feedbackRequestBody

            await article.actions.updateFeedback({commit, rootState, rootGetters}, feedbackRequestBody)
            await expect(url).toBe('http://localhost:8080/qiita_builder/feedback')
            await expect(apiToken).toBe('token')
            await expect(commit).toHaveBeenCalledTimes(1)
            await expect(commit).toHaveBeenCalledWith('updateFeedback', feedbackRequestBody)
        })

        test('actions: updateFeedback(400error)', async () => {
            mockError = true
            mockHttpStatus = 400
            feedbackRequestBody.feedbackId = 1
            feedbackRequestBody.content = 'updateContent'
            feedbackRequestBody.feedbackVersion = 1
            try {
                await article.actions.updateFeedback({commit, rootState, rootGetters}, feedbackRequestBody)
            } catch (error) {
                await expect(url).toBe('http://localhost:8080/qiita_builder/feedback')
                await expect(apiToken).toBe('token')
                await expect(error.name).toBe("axios error")
                await expect(error.response.status).toBe(400)
            }
        })

        test('actions: updateFeedback(409error)', async () => {
            mockError = true
            mockHttpStatus = 409
            feedbackRequestBody.feedbackId = 2
            feedbackRequestBody.content = 'updateContent1'
            feedbackRequestBody.feedbackVersion = 1
            try {
                await article.actions.updateFeedback({commit, rootState, rootGetters}, feedbackRequestBody)
            } catch (error) {
                await expect(url).toBe('http://localhost:8080/qiita_builder/feedback')
                await expect(apiToken).toBe('token')
                await expect(error.name).toBe("axios error")
                await expect(error.response.status).toBe(409)
            }
        })

        test('actions: updateFeedback(500error)', async () => {
            mockError = true
            mockHttpStatus = 500
            feedbackRequestBody.feedbackId = 2
            feedbackRequestBody.content = 'updateContent1'
            feedbackRequestBody.feedbackVersion = 1
            try {
                await article.actions.updateFeedback({commit, rootState, rootGetters}, feedbackRequestBody)
            } catch (error) {
                await expect(url).toBe('http://localhost:8080/qiita_builder/feedback')
                await expect(apiToken).toBe('token')
                await expect(error.name).toBe("axios error")
                await expect(error.response.status).toBe(500)
            }
        })

        test('actions: deleteFeedback', async () => {
            feedbackRequestBody.feedbackId = 1
            feedbackRequestBody.content = 'updateContent'
            feedbackRequestBody.feedbackVersion = 2
            feedbackRequestBody.deleteFlag = 1
            await article.actions.deleteFeedback({commit, rootState, rootGetters}, feedbackRequestBody)
            await expect(url).toBe('http://localhost:8080/qiita_builder/feedback')
            await expect(apiToken).toBe('token')
            await expect(commit).toHaveBeenCalledTimes(1)
            await expect(commit).toHaveBeenCalledWith('removeFeedback', 1)
        })

        test('actions: deleteFeedback(400error)', async () => {
            mockError = true
            mockHttpStatus = 400
            feedbackRequestBody.feedbackId = 1
            feedbackRequestBody.content = ''
            feedbackRequestBody.feedbackVersion = 1
            try {
                await article.actions.deleteFeedback({commit, rootState, rootGetters}, feedbackRequestBody)
            } catch (error) {
                await expect(url).toBe('http://localhost:8080/qiita_builder/feedback')
                await expect(apiToken).toBe('token')
                await expect(error.name).toBe("axios error")
                await expect(error.response.status).toBe(400)
            }
        })

        test('actions: deleteFeedback(409error)', async () => {
            mockError = true
            mockHttpStatus = 409
            // 存在しないfeedbackIdを想定
            feedbackRequestBody.feedbackId = 2
            feedbackRequestBody.content = 'updateContent'
            feedbackRequestBody.feedbackVersion = 1
            try {
                await article.actions.deleteFeedback({commit, rootState, rootGetters}, feedbackRequestBody)
            } catch (error) {
                await expect(url).toBe('http://localhost:8080/qiita_builder/feedback')
                await expect(apiToken).toBe('token')
                await expect(error.name).toBe("axios error")
                await expect(error.response.status).toBe(409)
            }
        })

        test('actions: deleteFeedback(500error)', async () => {
            mockError = true
            mockHttpStatus = 500
            // 存在しないfeedbackIdを想定
            feedbackRequestBody.feedbackId = 2
            feedbackRequestBody.content = 'updateContent'
            feedbackRequestBody.feedbackVersion = 1
            try {
                await article.actions.deleteFeedback({commit, rootState, rootGetters}, feedbackRequestBody)
            } catch (error) {
                await expect(url).toBe('http://localhost:8080/qiita_builder/feedback')
                await expect(apiToken).toBe('token')
                await expect(error.name).toBe("axios error")
                await expect(error.response.status).toBe(500)
            }
        })

        const replaceMyArticle = {
            myArticleId: 1,
            articleId: 1,
            postedUserId: 1,
            registerUserId: 1
        }

        test('actions: fetchMyArticle', async () => {
            const articleId = 1
            mockReturn = replaceMyArticle
            await article.actions.fetchMyArticle({commit, rootState, rootGetters}, articleId)
            await expect(url).toBe('http://localhost:8080/qiita_builder/my-article?articleId=1')
            await expect(apiToken).toBe('token')
            await expect(commit).toHaveBeenCalledTimes(1)
            await expect(commit).toHaveBeenCalledWith('setMyArticleId', replaceMyArticle.myArticleId)
        })

        test('actions: fetchMyArticle(400error)', async () => {
            const articleId = 'error'
            mockError = true
            mockHttpStatus = 400
            try {
                await article.actions.fetchMyArticle({commit, rootState, rootGetters}, articleId)
            } catch (error) {
                await expect(url).toBe('http://localhost:8080/qiita_builder/my-article?articleId=error')
                await expect(apiToken).toBe('token')
                await expect(error.name).toBe("axios error")
                await expect(error.response.status).toBe(400)
            }
        })

        test('actions: fetchMyArticle(500error)', async () => {
            const articleId = 'error'
            mockError = true
            mockHttpStatus = 500
            try {
                await article.actions.fetchMyArticle({commit, rootState, rootGetters}, articleId)
            } catch (error) {
                await expect(url).toBe('http://localhost:8080/qiita_builder/my-article?articleId=error')
                await expect(apiToken).toBe('token')
                await expect(error.name).toBe("axios error")
                await expect(error.response.status).toBe(500)
            }
        })

        test('actions: registerMyArticle', async () => {
            const articleId = 1
            mockReturn = replaceMyArticle
            await article.actions.registerMyArticle({commit, rootState, rootGetters}, articleId)
            await expect(url).toBe('http://localhost:8080/qiita_builder/my-article')
            await expect(apiToken).toBe('token')
            await expect(requestBody).toStrictEqual(replaceMyArticle)
            await expect(commit).toHaveBeenCalledTimes(1)
            await expect(commit).toHaveBeenCalledWith('setMyArticleId', replaceMyArticle.myArticleId)
        })

        test('actions: registerMyArticle(400error)', async () => {
            const articleId = 'error'
            mockError = true
            mockHttpStatus = 400

            try {
                await article.actions.registerMyArticle({commit, rootState, rootGetters}, articleId)
            } catch (error) {
                await expect(url).toBe('http://localhost:8080/qiita_builder/my-article')
                await expect(apiToken).toBe('token')
                await expect(error.name).toBe("axios error")
                await expect(error.response.status).toBe(400)
            }
        })

        test('actions: registerMyArticle(500error)', async () => {
            const articleId = 'error'
            mockError = true
            mockHttpStatus = 500

            try {
                await article.actions.registerMyArticle({commit, rootState, rootGetters}, articleId)
            } catch (error) {
                await expect(url).toBe('http://localhost:8080/qiita_builder/my-article')
                await expect(apiToken).toBe('token')
                await expect(error.name).toBe("axios error")
                await expect(error.response.status).toBe(500)
            }
        })

        test('actions: deleteMyArticle', async () => {
            const myArticleId = 1
            mockReturn = null
            await article.actions.deleteMyArticle({commit, rootState, rootGetters}, myArticleId)
            await expect(url).toBe('http://localhost:8080/qiita_builder/my-article/1')
            await expect(apiToken).toBe('token')
            await expect(commit).toHaveBeenCalledTimes(1)
            await expect(commit).toHaveBeenCalledWith('setMyArticleId', null)
        })

        test('actions: deleteMyArticle(400error)', async () => {
            const myArticleId = 'error'
            mockError = true
            mockHttpStatus = 400

            try {
                await article.actions.deleteMyArticle({commit, rootState, rootGetters}, myArticleId)
            } catch (error) {
                await expect(url).toBe('http://localhost:8080/qiita_builder/my-article/error')
                await expect(apiToken).toBe('token')
                await expect(error.name).toBe("axios error")
                await expect(error.response.status).toBe(400)
            }
        })

        test('actions: deleteMyArticle(500error)', async () => {
            const myArticleId = 'error'
            mockError = true
            mockHttpStatus = 500

            try {
                await article.actions.deleteMyArticle({commit, rootState, rootGetters}, myArticleId)
            } catch (error) {
                await expect(url).toBe('http://localhost:8080/qiita_builder/my-article/error')
                await expect(apiToken).toBe('token')
                await expect(error.name).toBe("axios error")
                await expect(error.response.status).toBe(500)
            }
        })

        const replaceRecommend = {
            recommendId: 1,
            articleId: 1,
            postedUserId: 1,
            recommendUserId: 1
        }

        test('actions: fetchRecommend', async () => {
            const articleId = 1
            mockReturn = replaceRecommend
            await article.actions.fetchRecommend({commit, rootState, rootGetters}, articleId)
            await expect(url).toBe('http://localhost:8080/qiita_builder/recommend?articleId=1')
            await expect(apiToken).toBe('token')
            await expect(commit).toHaveBeenCalledTimes(1)
            await expect(commit).toHaveBeenCalledWith('setRecommendId', replaceRecommend.recommendId)
        })

        test('actions: fetchRecommend(400error)', async () => {
            const articleId = 'error'
            mockError = true
            mockHttpStatus = 400

            try {
                await article.actions.fetchRecommend({commit, rootState, rootGetters}, articleId)
            } catch (error) {
                await expect(url).toBe('http://localhost:8080/qiita_builder/recommend?articleId=error')
                await expect(apiToken).toBe('token')
                await expect(error.name).toBe("axios error")
                await expect(error.response.status).toBe(400)
            }
        })

        test('actions: fetchRecommend(500error)', async () => {
            const articleId = 'error'
            mockError = true
            mockHttpStatus = 500

            try {
                await article.actions.fetchRecommend({commit, rootState, rootGetters}, articleId)
            } catch (error) {
                await expect(url).toBe('http://localhost:8080/qiita_builder/recommend?articleId=error')
                await expect(apiToken).toBe('token')
                await expect(error.name).toBe("axios error")
                await expect(error.response.status).toBe(500)
            }
        })

        test('actions: registerRecommend', async () => {
            const articleId = 1
            mockReturn = replaceRecommend
            await article.actions.registerRecommend({commit, rootState, rootGetters}, articleId)
            await expect(url).toBe('http://localhost:8080/qiita_builder/recommend')
            await expect(apiToken).toBe('token')
            await expect(commit).toHaveBeenCalledTimes(2)
            await expect(commit).toHaveBeenCalledWith('setRecommendId', replaceRecommend.recommendId)
            await expect(commit).toHaveBeenCalledWith('incrementQiitaRecommendPoint')
        })

        test('actions: registerRecommend(400error)', async () => {
            const articleId = 'error'
            mockError = true
            mockHttpStatus = 400

            try {
                await article.actions.registerRecommend({commit, rootState, rootGetters}, articleId)
            } catch (error) {
                await expect(url).toBe('http://localhost:8080/qiita_builder/recommend')
                await expect(apiToken).toBe('token')
                await expect(error.name).toBe("axios error")
                await expect(error.response.status).toBe(400)
            }
        })

        test('actions: registerRecommend(500error)', async () => {
            const articleId = 'error'
            mockError = true
            mockHttpStatus = 500

            try {
                await article.actions.registerRecommend({commit, rootState, rootGetters}, articleId)
            } catch (error) {
                await expect(url).toBe('http://localhost:8080/qiita_builder/recommend')
                await expect(apiToken).toBe('token')
                await expect(error.name).toBe("axios error")
                await expect(error.response.status).toBe(500)
            }
        })

        test('actions: deleteRecommend', async () => {
            const articleId = 1
            mockReturn = null
            await article.actions.deleteRecommend({commit, rootState, rootGetters}, articleId)
            await expect(url).toBe('http://localhost:8080/qiita_builder/recommend/1')
            await expect(apiToken).toBe('token')
            await expect(commit).toHaveBeenCalledTimes(2)
            await expect(commit).toHaveBeenCalledWith('setRecommendId', null)
            await expect(commit).toHaveBeenCalledWith('decrementQiitaRecommendPoint')

        })

        test('actions: deleteRecommend(400error)', async () => {
            const articleId = 'error'
            mockError = true
            mockHttpStatus = 400

            try {
                await article.actions.deleteRecommend({commit, rootState, rootGetters}, articleId)
            } catch (error) {
                await expect(url).toBe('http://localhost:8080/qiita_builder/recommend/error')
                await expect(apiToken).toBe('token')
                await expect(error.name).toBe("axios error")
                await expect(error.response.status).toBe(400)
            }
        })

        test('actions: deleteRecommend(500error)', async () => {
            const articleId = 'error'
            mockError = true
            mockHttpStatus = 500

            try {
                await article.actions.deleteRecommend({commit, rootState, rootGetters}, articleId)
            } catch (error) {
                await expect(url).toBe('http://localhost:8080/qiita_builder/recommend/error')
                await expect(apiToken).toBe('token')
                await expect(error.name).toBe("axios error")
                await expect(error.response.status).toBe(500)
            }
        })

        // issueの修正後、引数が変更する可能性があるため、一旦保留
        test('actions: fetchArticleEdit', async () => {
            const params = {
                articleId: 1,
                userId: 1
            }
            article.state.stateFlag = 1
            const state = article.state
            await article.actions.fetchArticleEdit({dispatch, state, rootState, rootGetters}, params)
            await expect(url).toBe('http://localhost:8080/qiita_builder/article/isExist')
            await expect(apiToken).toBe('token')
            await expect(dispatch).toHaveBeenCalledTimes(4)
            await expect(dispatch).toHaveBeenCalledWith('resetArticle')
            await expect(dispatch).toHaveBeenCalledWith('articles/fetchTags', null, {root: true})
            await expect(dispatch).toHaveBeenCalledWith('fetchArticle', params.articleId)
        })

        test('actions: fetchArticleEdit(400error)', async () => {

        })

        test('actions: toggleProcessFailure', async () => {
            await article.actions.toggleProcessFailure({commit})
            expect(commit).toHaveBeenCalledTimes(1)
            expect(commit).toHaveBeenCalledWith('toggleProcessFailure')
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
            const editFeedback = replaceArticle.feedbacks
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


