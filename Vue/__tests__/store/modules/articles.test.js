import index from '@/store/index';
import articles from '@/store/modules/articles';
import {createLocalVue} from '@vue/test-utils';
import {cloneDeep} from 'lodash';
import axios from "axios";
import Vuex from 'vuex';
import {beforeEach, describe} from "@jest/globals";

const localVue = createLocalVue();
localVue.use(Vuex);

const replaceArticles = [
    {
        article: {
            articleId: 1,
            title: 'title1',
            // createdAt: Date.now(),
            // updatedAt: Date.now(),
            stateFlag: 1,
            feedbackCount: 1,
            registeredMyArticleCount: 2,
            qiitaRecommendPoint: 1
        },
        postedUser: {
            userId: 1,
            displayName: "user1",
            photoUrl: "photo1"
        },
        tags: {
            tagId: 1,
            tagName: 'java'
        }
    },
    {
        article: {
            articleId: 2,
            title: 'title2',
            // createdAt: Date.now(),
            // updatedAt: Date.now(),
            stateFlag: 2,
            feedbackCount: 0,
            registeredMyArticleCount: 1,
            qiitaRecommendPoint: 1
        },
        postedUser: {
            userId: 2,
            displayName: "user2",
            photoUrl: "photo2"
        },
        tags: {
            tagId: 2,
            tagName: 'go'
        }
    },
    {
        article: {
            articleId: 3,
            title: 'title3',
            // createdAt: Date.now(),
            // updatedAt: Date.now(),
            stateFlag: 1,
            feedbackCount: 3,
            registeredMyArticleCount: 1,
            qiitaRecommendPoint: 2
        },
        postedUser: {
            userId: 3,
            displayName: "user3",
            photoUrl: "photo3"
        },
        tags: {
            tagId: 3,
            tagName: 'ruby'
        }
    },
    {
        article: {
            articleId: 4,
            title: 'title4',
            // createdAt: Date.now(),
            // updatedAt: Date.now(),
            stateFlag: 1,
            feedbackCount: 2,
            registeredMyArticleCount: 2,
            qiitaRecommendPoint: 2
        },
        postedUser: {
            userId: 4,
            displayName: "user4",
            photoUrl: "photo4"
        },
        tags: {
            tagId: 4,
            tagName: 'python'
        }
    },
    {
        article: {
            articleId: 5,
            title: 'title5',
            // createdAt: Date.now(),
            // updatedAt: Date.now(),
            stateFlag: 2,
            feedbackCount: 2,
            registeredMyArticleCount: 1,
            qiitaRecommendPoint: 2
        },
        postedUser: {
            userId: 5,
            displayName: "user5",
            photoUrl: "photo5"
        },
        tags: {
            tagId: 5,
            tagName: 'html'
        }
    },
    {
        article: {
            articleId: 6,
            title: 'title6',
            // createdAt: Date.now(),
            // updatedAt: Date.now(),
            stateFlag: 2,
            feedbackCount: 1,
            registeredMyArticleCount: 1,
            qiitaRecommendPoint: 1
        },
        postedUser: {
            userId: 6,
            displayName: "user6",
            photoUrl: "photo6"
        },
        tags: {
            tagId: 1,
            tagName: 'java'
        }
    },
    {
        article: {
            articleId: 7,
            title: 'title7',
            // createdAt: Date.now(),
            // updatedAt: Date.now(),
            stateFlag: 2,
            feedbackCount: 3,
            registeredMyArticleCount: 2,
            qiitaRecommendPoint: 2
        },
        postedUser: {
            userId: 7,
            displayName: "user7",
            photoUrl: "photo7"
        },
        tags: {
            tagId: 3,
            tagName: 'ruby'
        }
    },
    {
        article: {
            articleId: 8,
            title: 'title8',
            // createdAt: Date.now(),
            // updatedAt: Date.now(),
            stateFlag: 1,
            feedbackCount: 1,
            registeredMyArticleCount: 2,
            qiitaRecommendPoint: 1
        },
        postedUser: {
            userId: 8,
            displayName: "user8",
            photoUrl: "photo8"
        },
        tags: {
            tagId: 5,
            tagName: 'html'
        }
    },
    {
        article: {
            articleId: 9,
            title: 'title9',
            // createdAt: Date.now(),
            // updatedAt: Date.now(),
            stateFlag: 1,
            feedbackCount: 5,
            registeredMyArticleCount: 7,
            qiitaRecommendPoint: 8
        },
        postedUser: {
            userId: 9,
            displayName: "user9",
            photoUrl: "photo9"
        },
        tags: {
            tagId: 4,
            tagName: 'python'
        }
    },
    {
        article: {
            articleId: 10,
            title: 'title10',
            // createdAt: Date.now(),
            // updatedAt: Date.now(),
            stateFlag: 1,
            feedbackCount: 3,
            registeredMyArticleCount: 4,
            qiitaRecommendPoint: 2
        },
        postedUser: {
            userId: 10,
            displayName: "user10",
            photoUrl: "photo10"
        },
        tags: {
            tagId: 1,
            tagName: 'java'
        }
    },
]

//actualな値
const replaceTags =[
    {
        tagId:1,
        tagName:'Java'
    },
    {
        tagId:2,
        tagName:'JavaScript'
    },
    {
        tagId:3,
        tagName:'Ruby'
    },
    {
        tagId:4,
        tagName:'Go'
    },
    {
        tagId:5,
        tagName:'Python'
    },
]

// actualな値
let articleUrl = ''
let pageUrl = ''
let tagUrl = ''
let articleApiToken = ''
let pageApiToken = ''
let tagApiToken = ''
let articleParam = []
let pageParam = []
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
            if (mockError) {
                throw Error("Mock Error")
            }
            switch (_url) {
                case 'http://localhost:8080/qiita_builder/article/':
                    articleUrl = _url;
                    articleApiToken = _apiToken;
                    articleParam = _param;
                    resolve({
                        data: replaceArticles
                    })
                    break
                case 'http://localhost:8080/qiita_builder/article/totalPage':
                    pageUrl = _url;
                    pageApiToken = _apiToken;
                    pageParam = _param;
                    resolve({
                        data: 1
                    })
                    break
                case 'http://localhost:8080/qiita_builder/tag/':
                    tagUrl = _url
                    tagApiToken = _apiToken
                    resolve({
                        data: replaceTags
                    })
            }
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
            store.registerModule('articles', cloneDeep(articles))
            commit = cloneDeep(jest).fn();
        })

        // eslint-disable-next-line no-undef
        test('actions: fetchArticles', async () => {
            const searchCriteria = {
                sortNum: 0,
                pageSize: 10,
                currentPage: 1,
                searchWord: '',
                toggleSearchWord: 0,
                searchTag: [],
                period: null
            }
            await articles.actions.fetchArticles({commit, rootState, rootGetters}, searchCriteria)
            await expect(articleUrl).toBe('http://localhost:8080/qiita_builder/article/')
            await expect(articleApiToken).toBe('token')
            await expect(articleParam).toStrictEqual(searchCriteria)
            await expect(pageUrl).toBe('http://localhost:8080/qiita_builder/article/totalPage')
            await expect(pageApiToken).toBe('token')
            await expect(pageParam).toStrictEqual(searchCriteria)
            await expect(commit).toHaveBeenCalledTimes(2)
            await expect(commit).toHaveBeenCalledWith('setArticles', replaceArticles)
            await expect(commit).toHaveBeenCalledWith('setTotalPage', 1)
        })

        test('action: fetchTags', async () => {
            await articles.actions.fetchTags({commit, rootState, rootGetters})
            await expect(tagUrl).toBe("http://localhost:8080/qiita_builder/tag/")
            await expect(tagApiToken).toBe('token')
            await expect(commit).toHaveBeenCalledTimes(1)
            await expect(commit).toHaveBeenCalledWith('setTags',replaceTags)
        })

        test('action: toggleErrorTransitionDialog',async()=>{
            await articles.actions.toggleErrorTransitionDialog({commit})
            await expect(commit).toHaveBeenCalledTimes(1)
            await expect(commit).toHaveBeenCalledWith('toggleErrorTransitionDialog')
        })
    })

    //************ mutations ************
    describe('mutations', () => {
        let store
        let state
        let commit
        beforeEach(() => {
            store = new Vuex.Store(cloneDeep(articles))
            state = store.state
            commit = store.commit
        })

        // eslint-disable-next-line no-undef
        test('mutations: setArticles', () => {
            // eslint-disable-next-line no-undef
            expect(state.articles).toStrictEqual([])
            commit('setArticles', replaceArticles)
            // eslint-disable-next-line no-undef
            expect(state.articles).toBe(replaceArticles)
        })

        // eslint-disable-next-line no-undef
        test('mutations: setTags', () => {
            let tags = "tags"
            // eslint-disable-next-line no-undef
            expect(state.tags).toStrictEqual([])
            commit('setTags', tags)
            // eslint-disable-next-line no-undef
            expect(state.tags).toStrictEqual(tags)
        })

        // eslint-disable-next-line no-undef
        test('mutations: setTotalPage', () => {
            const totalPage = 5
            // eslint-disable-next-line no-undef
            expect(state.totalPage).toBe(undefined)
            commit('setTotalPage', totalPage)
            // eslint-disable-next-line no-undef
            expect(state.totalPage).toBe(5)
        })

        // eslint-disable-next-line no-undef
        test('mutations: toggleErrorTransitionDialog', () => {
            // eslint-disable-next-line no-undef
            expect(state.errorTransistionDialog).toBeFalsy()
            commit('toggleErrorTransitionDialog')
            // eslint-disable-next-line no-undef
            expect(state.errorTransistionDialog).toBeTruthy()
        })

    })
})


