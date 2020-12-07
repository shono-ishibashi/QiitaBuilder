import Vuex from 'vuex';
import index from '@/store/index';
import user from '@/store/modules/user';
import {createLocalVue} from '@vue/test-utils';
import {cloneDeep} from 'lodash';
import axios from "axios";

const localVue = createLocalVue()
localVue.use(Vuex)
let url = ''
let apiToken = ''
let mockError = false
let mockHttpStatus = 200
let mockReturn = null
const apiUrl = 'http://localhost:8080/qiita_builder/'

describe('/user/mutations', () => {
    let store
    beforeEach(() => {
        store = new Vuex.Store(cloneDeep(user))
    })
    test('mutations: setArticleCardDisplay', () => {
        let articleCardDisplay = "articleCard"
        expect(store.getters.articleCardDisplay).toBeNull()
        store.commit('setArticleCardDisplay', articleCardDisplay)
        expect(store.getters.articleCardDisplay).toBe("articleCard")
    })
    test('mutations: setUserDetail', () => {
        expect(store.state.userDetail).toEqual({
            userId: 0,
            displayName: "",
            photoUrl: "",
            usedTags: [],
            feedbackCount: 0,
            qiitaRecommendedAllCount: 0,
            postedArticleCount: 0,
            isLoginUser: false,
        });
        store.commit('setUserDetail', userDetail);
        expect(store.state.userDetail).toEqual(userDetail);
    })
    test('mutations: setPostedArticles', () => {
        expect(store.state.postedArticles).toEqual([])
        store.commit('setPostedArticles', articles)
        expect(store.state.postedArticles).toEqual(articles)
    })
    test('mutations: setMyArticles', () => {
        expect(store.state.myArticles).toEqual([])
        store.commit('setMyArticles', articles)
        expect(store.state.myArticles).toEqual(articles)
    })
    test('mutations: setFeedBackArticles', () => {
        expect(store.state.feedbackArticles).toEqual([])
        store.commit('setFeedbackArticles', articles)
        expect(store.state.feedbackArticles).toEqual(articles)
    })
    test('mutations: setDisplayArticles', () => {
        expect(store.state.displayArticles).toEqual([])
        store.commit('setDisplayArticles', articles)
        expect(store.state.displayArticles).toEqual(articles)
    })
    test('mutations: setUsedTags', () => {
        expect(store.state.usedTags).toEqual([])
        store.commit('setUsedTags', usedTags)
        expect(store.state.usedTags).toEqual(usedTags)
    })
    test('mutations: clearDisplayArticles', () => {
        store.replaceState({
            displayArticles: articles
        })
        expect(store.state.displayArticles).toEqual(articles)
        store.commit('clearDisplayArticles')
        expect(store.state.displayArticles).toEqual([])
    })
    test('mutations: clearUsedTag', () => {
        store.replaceState({
            usedTags: usedTags
        })
        expect(store.state.usedTags).toEqual(usedTags)
        store.commit('clearUsedTag')
        expect(store.state.usedTags).toEqual([])
    })
    test('mutations: setChartData', () => {
        expect(store.state.chartData).toBeNull()
        store.commit('setChartData', chartData)
        expect(store.state.chartData).toEqual(chartData)
    })
    test('mutations: setUserId', () => {
        expect(store.state.userDetail.userId).toBe(0)
        store.commit('setUserId', 1)
        expect(store.state.userDetail.userId).toBe(1)
    })
    test('mutations: clearState', () => {
        store.replaceState({
            userDetail: userDetail,
            postedArticles: articles,
            myArticles: articles,
            feedbackArticles: articles,
            displayArticles: articles,
            usedTags: usedTags,
            chartData: chartData,
        })

        expect(store.state.postedArticles).toEqual(articles)
        expect(store.state.displayArticles).toEqual(articles)
        expect(store.state.myArticles).toEqual(articles)
        expect(store.state.feedbackArticles).toEqual(articles)
        expect(store.state.userDetail).toEqual(userDetail)
        expect(store.state.chartData).toEqual(chartData)
        expect(store.state.usedTags).toEqual(usedTags)

        store.commit('clearState')

        expect(store.state.postedArticles).toEqual([])
        expect(store.state.displayArticles).toEqual([])
        expect(store.state.myArticles).toEqual([])
        expect(store.state.feedbackArticles).toEqual([])
        expect(store.state.userDetail).toEqual({
            userId: 0,
            displayName: "",
            photoUrl: "",
            usedTags: [],
            feedbackCount: 0,
            qiitaRecommendedAllCount: 0,
            postedArticleCount: 0,
            isLoginUser: false,
        })
        expect(store.state.chartData).toBeNull()
        expect(store.state.usedTags).toEqual([])
    })
})

describe('/user/getters', () => {
    let store
    beforeEach(() => {
        store = new Vuex.Store(cloneDeep(user))
    })
    test('getters: userId', () => {
        store.replaceState({
            userDetail: userDetail,
        })
        expect(store.getters.userId).toBe(1)
    })
    test('getters: postedArticles(login user)', () => {
        store.replaceState({
            userDetail: userDetail,
            postedArticles: user1PostedArticles,
        })
        expect(store.getters.postedArticles).toEqual(user1PostedArticles)
    })
    test('getters: postedArticles(not login user)', () => {
        store.replaceState({
            userDetail: notLoginUserDetail,
            postedArticles: user2PostedArticles,
        })
        expect(store.getters.postedArticles).toEqual(
            [{
                articleId: 2,
                title: 'title2',
                createdAt: "00:00:02",
                updatedAt: "00:00:22",
                stateFlag: 2,
                feedbackCount: 2,
                registeredMyArticleCount: 1,
                qiitaRecommendPoint: 1,
                postedUser: {
                    userId: 2,
                    displayName: "user2",
                    photoUrl: "photo2"
                },
                tags: [{
                    tagId: 2,
                    tagName: 'go',
                }, {
                    tagId: 3,
                    tagName: "Javascript"
                }],
            }]
        )
    })
    test('getters: notDraftArticles', () => {
        store.replaceState({
            postedArticles: user2PostedArticles,
        })
        expect(store.getters.notDraftArticles).toEqual(
            [{
                articleId: 2,
                title: 'title2',
                createdAt: "00:00:02",
                updatedAt: "00:00:22",
                stateFlag: 2,
                feedbackCount: 2,
                registeredMyArticleCount: 1,
                qiitaRecommendPoint: 1,
                postedUser: {
                    userId: 2,
                    displayName: "user2",
                    photoUrl: "photo2"
                },
                tags: [{
                    tagId: 2,
                    tagName: 'go',
                }, {
                    tagId: 3,
                    tagName: "Javascript"
                }],
            }]
        )
    })
    test('getters: draftArticles(login user)', () => {
        store.replaceState({
            userDetail: userDetail,
            postedArticles: user1PostedArticles,
        })
        expect(store.getters.draftArticles).toEqual([{
                articleId: 3,
                title: 'title3',
                createdAt: "00:00:03",
                updatedAt: "00:00:33",
                stateFlag: 0,
                feedbackCount: 2,
                registeredMyArticleCount: 1,
                qiitaRecommendPoint: 1,
                postedUser: {
                    userId: 1,
                    displayName: "user1",
                    photoUrl: "photo1"
                },
                tags: [{
                    tagId: 2,
                    tagName: 'go',
                }, {
                    tagId: 3,
                    tagName: "Javascript"
                }]
            }]
        )
    })
    test('getters: draftArticles(not login user)', () => {
        store.replaceState({
            userDetail: notLoginUserDetail,
            postedArticles: user2PostedArticles,
        })
        expect(store.getters.draftArticles).toEqual([])
    })
    test('getters: feedbackArticles(my page)', () => {
        store.replaceState({
            userDetail: userDetail,
            feedbackArticles: articles,
        })
        expect(store.getters.feedbackArticles).toEqual([
            {
                articleId: 1,
                title: 'title1',
                createdAt: "00:00:01",
                updatedAt: "00:00:11",
                stateFlag: 1,
                feedbackCount: 1,
                registeredMyArticleCount: 2,
                qiitaRecommendPoint: 1,
                postedUser: {
                    userId: 1,
                    displayName: "user1",
                    photoUrl: "photo1"
                },
                tags: [{
                    tagId: 1,
                    tagName: 'Java',
                }, {
                    tagId: 3,
                    tagName: "Javascript"
                }]
            },
            {
                articleId: 2,
                title: 'title2',
                createdAt: "00:00:02",
                updatedAt: "00:00:22",
                stateFlag: 2,
                feedbackCount: 2,
                registeredMyArticleCount: 1,
                qiitaRecommendPoint: 1,
                postedUser: {
                    userId: 2,
                    displayName: "user2",
                    photoUrl: "photo2"
                },
                tags: [{
                    tagId: 2,
                    tagName: 'go',
                }, {
                    tagId: 3,
                    tagName: "Javascript"
                }]
            },
            {
                articleId: 3,
                title: 'title3',
                createdAt: "00:00:03",
                updatedAt: "00:00:33",
                stateFlag: 0,
                feedbackCount: 2,
                registeredMyArticleCount: 1,
                qiitaRecommendPoint: 1,
                postedUser: {
                    userId: 1,
                    displayName: "user1",
                    photoUrl: "photo1"
                },
                tags: [{
                    tagId: 2,
                    tagName: 'go',
                }, {
                    tagId: 3,
                    tagName: "Javascript"
                }]
            },
        ])
    })
    test('getters: feedbackArticles(not my page)', () => {
        store.replaceState({
            userDetail: notLoginUserDetail,
            feedbackArticles: articles,
        })
        expect(store.getters.feedbackArticles).toEqual([
            {
                articleId: 1,
                title: 'title1',
                createdAt: "00:00:01",
                updatedAt: "00:00:11",
                stateFlag: 1,
                feedbackCount: 1,
                registeredMyArticleCount: 2,
                qiitaRecommendPoint: 1,
                postedUser: {
                    userId: 1,
                    displayName: "user1",
                    photoUrl: "photo1"
                },
                tags: [{
                    tagId: 1,
                    tagName: 'Java',
                }, {
                    tagId: 3,
                    tagName: "Javascript"
                }]
            },
            {
                articleId: 2,
                title: 'title2',
                createdAt: "00:00:02",
                updatedAt: "00:00:22",
                stateFlag: 2,
                feedbackCount: 2,
                registeredMyArticleCount: 1,
                qiitaRecommendPoint: 1,
                postedUser: {
                    userId: 2,
                    displayName: "user2",
                    photoUrl: "photo2"
                },
                tags: [{
                    tagId: 2,
                    tagName: 'go',
                }, {
                    tagId: 3,
                    tagName: "Javascript"
                }]
            },
        ])
    })
    test('getters: myArticles(not my page)', () => {
        store.replaceState({
            userDetail: notLoginUserDetail,
            myArticles: articles
        })
        expect(store.getters.myArticles).toEqual([
            {
                articleId: 1,
                title: 'title1',
                createdAt: "00:00:01",
                updatedAt: "00:00:11",
                stateFlag: 1,
                feedbackCount: 1,
                registeredMyArticleCount: 2,
                qiitaRecommendPoint: 1,
                postedUser: {
                    userId: 1,
                    displayName: "user1",
                    photoUrl: "photo1"
                },
                tags: [{
                    tagId: 1,
                    tagName: 'Java',
                }, {
                    tagId: 3,
                    tagName: "Javascript"
                }]
            },
            {
                articleId: 2,
                title: 'title2',
                createdAt: "00:00:02",
                updatedAt: "00:00:22",
                stateFlag: 2,
                feedbackCount: 2,
                registeredMyArticleCount: 1,
                qiitaRecommendPoint: 1,
                postedUser: {
                    userId: 2,
                    displayName: "user2",
                    photoUrl: "photo2"
                },
                tags: [{
                    tagId: 2,
                    tagName: 'go',
                }, {
                    tagId: 3,
                    tagName: "Javascript"
                }]
            },
        ])
    })
    test('getters: myArticles(my page)', () => {
        store.replaceState({
            userDetail: userDetail,
            myArticles: articles,
        })
        expect(store.getters.myArticles).toEqual([
            {
                articleId: 1,
                title: 'title1',
                createdAt: "00:00:01",
                updatedAt: "00:00:11",
                stateFlag: 1,
                feedbackCount: 1,
                registeredMyArticleCount: 2,
                qiitaRecommendPoint: 1,
                postedUser: {
                    userId: 1,
                    displayName: "user1",
                    photoUrl: "photo1"
                },
                tags: [{
                    tagId: 1,
                    tagName: 'Java',
                }, {
                    tagId: 3,
                    tagName: "Javascript"
                }]
            },
            {
                articleId: 2,
                title: 'title2',
                createdAt: "00:00:02",
                updatedAt: "00:00:22",
                stateFlag: 2,
                feedbackCount: 2,
                registeredMyArticleCount: 1,
                qiitaRecommendPoint: 1,
                postedUser: {
                    userId: 2,
                    displayName: "user2",
                    photoUrl: "photo2"
                },
                tags: [{
                    tagId: 2,
                    tagName: 'go',
                }, {
                    tagId: 3,
                    tagName: "Javascript"
                }]
            },
            {
                articleId: 3,
                title: 'title3',
                createdAt: "00:00:03",
                updatedAt: "00:00:33",
                stateFlag: 0,
                feedbackCount: 2,
                registeredMyArticleCount: 1,
                qiitaRecommendPoint: 1,
                postedUser: {
                    userId: 1,
                    displayName: "user1",
                    photoUrl: "photo1"
                },
                tags: [{
                    tagId: 2,
                    tagName: 'go',
                }, {
                    tagId: 3,
                    tagName: "Javascript"
                }]
            },
        ])
    })
    test('getters: displayArticles', () => {
        store.replaceState({
            displayArticles: user1PostedArticles
        })
        expect(store.getters.displayArticles).toEqual(user1PostedArticles)
    })
    test('getters: usedTags', () => {
        store.replaceState({
            usedTags: usedTags
        })
        expect(store.getters.usedTags).toEqual(usedTags)
    })
    test('getters: chartData', () => {
        store.replaceState({chartData: chartData})
        expect(store.getters.chartData).toEqual(chartData)
    })
})

// axiosのモック化
jest.mock('axios', () => ({
    get: jest.fn((_url, {
        headers: {
            'Authorization': _apiToken
        }
    }) => {
        return new Promise((resolve) => {

            url = _url;
            apiToken = _apiToken;

            if (mockError) {
                if (mockHttpStatus === 400) throw  Object.assign(new Error('BAD REQUEST'), {
                    name: 'axios error',
                    response: {status: 400}
                });
                if (mockHttpStatus === 500) throw   Object.assign(new Error('Internal Server Error'), {
                    name: 'axios error',
                    response: {status: 500}
                });
            }
            resolve({
                data: mockReturn
            })
        })
    })
}))
describe('/user/actions', () => {
    let store;
    let commit;
    let dispatch;
    let rootState;
    let rootGetters;

    beforeEach(() => {
        store = new Vuex.Store({
            state: {
                API_URL: apiUrl
            },
            getters: {
                API_URL: state => state.API_URL
            },
            modules: {
                auth: {
                    state: {
                        apiToken: 'token'
                    }
                }
            }
        })
        store.registerModule('user', cloneDeep(user))

        commit = cloneDeep(jest).fn();
        dispatch = cloneDeep(jest).fn();
        rootState = store.state;
        rootGetters = store.getters;

        url = ''
        apiToken = ''
        mockError = false
        mockHttpStatus = 200
        mockReturn = null
    })
    test('actions: clearState', async () => {
        await user.actions.clearState({dispatch, commit, rootState, rootGetters})
        await expect(commit).toHaveBeenCalledTimes(1)
        await expect(commit).toHaveBeenCalledWith('clearState')
    })
    test('actions: setArticlesAndTags', async () => {
        await user.actions.setArticlesAndTags({dispatch, commit, rootState, rootGetters}, user1PostedArticles)
        await expect(commit).toHaveBeenCalledTimes(2 + 1 + user1PostedArticles.length)
        await expect(commit).toHaveBeenCalledWith('clearDisplayArticles')
        await expect(commit).toHaveBeenCalledWith('clearUsedTag')
        await expect(commit).toHaveBeenCalledWith('setDisplayArticles', user1PostedArticles)
        await expect(commit).toHaveBeenCalledWith('setUsedTags', user1PostedArticles[0].tags)
        await expect(commit).toHaveBeenCalledWith('setUsedTags', user1PostedArticles[1].tags)
    })
    test('actions: setArticles', async () => {
        await user.actions.setArticles({dispatch, commit, rootState, rootGetters}, user1PostedArticles)
        await expect(commit).toHaveBeenCalledTimes(2)
        await expect(commit).toHaveBeenCalledWith('clearDisplayArticles')
        await expect(commit).toHaveBeenCalledWith('setDisplayArticles', user1PostedArticles)
    })
    test('actions: setChartData', async () => {
        await user.actions.setChartData({dispatch, commit, rootState, rootGetters}, chartData)
        await expect(commit).toHaveBeenCalledTimes(1)
        await expect(commit).toHaveBeenCalledWith('setChartData', chartData)
    })
    test('actions: fetchUserDetail', async () => {
        const userId = 1;
        mockReturn = userDetail
        await user.actions.fetchUserDetail({dispatch, commit, rootState, rootGetters}, userId);
        await expect(url).toBe("http://localhost:8080/qiita_builder/user/detail/")
        await expect(apiToken).toBe('token')
        await expect(commit).toHaveBeenCalledTimes(1)
        await expect(commit).toHaveBeenCalledWith('setUserDetail', userDetail)
    })
    test('actions: fetchUserDetail(400error)', async () => {
        const userId = 1;
        mockError = true;
        mockHttpStatus = 400;
        try {
            await user.actions.fetchUserDetail({dispatch, commit, rootState, rootGetters}, userId);
        } catch (error) {//try内のmethodでreject()でerrorを返しているのでcatchしたらテスト実行
            await expect(url).toBe("http://localhost:8080/qiita_builder/user/detail/")
            await expect(apiToken).toBe('token')
            await expect(dispatch).toHaveBeenCalledTimes(1)
            await expect(dispatch).toHaveBeenCalledWith('window/setNotFound', true, {root: true})
        }
    })
    test('actions: fetchUserDetail(500error)', async () => {
        const userId = 1;
        mockError = true;
        mockHttpStatus = 500;
        try {
            await user.actions.fetchUserDetail({dispatch, commit, rootState, rootGetters}, userId);
        } catch (error) {
            await expect(url).toBe("http://localhost:8080/qiita_builder/user/detail/")
            await expect(apiToken).toBe('token')
            await expect(dispatch).toHaveBeenCalledTimes(1)
            await expect(dispatch).toHaveBeenCalledWith('window/setInternalServerError', true, {root: true})
        }
    })
    test('actions: fetchPostedArticles', async () => {
        const userId = 1;
        mockReturn = user1PostedArticles;
        await user.actions.fetchPostedArticles({dispatch, commit, rootState, rootGetters}, userId);
        await expect(url).toBe("http://localhost:8080/qiita_builder/article/posted")
        await expect(apiToken).toBe('token')
        await expect(commit).toHaveBeenCalledTimes(1)
        await expect(commit).toHaveBeenCalledWith('setPostedArticles', user1PostedArticles)
    })
    test('actions: fetchPostedArticles(400error)', async () => {
        const userId = 1;
        mockError = true;
        mockHttpStatus = 400;
        await user.actions.fetchPostedArticles({dispatch, commit, rootState, rootGetters}, userId);
        await expect(url).toBe("http://localhost:8080/qiita_builder/article/posted")
        await expect(apiToken).toBe('token')
        await expect(dispatch).toHaveBeenCalledTimes(1)
        await expect(dispatch).toHaveBeenCalledWith('window/setNotFound', true, {root: true})
    })
    test('actions: fetchPostedArticles(500error)', async () => {
        const userId = 1;
        mockError = true;
        mockHttpStatus = 500;
        await user.actions.fetchPostedArticles({dispatch, commit, rootState, rootGetters}, userId);
        await expect(url).toBe("http://localhost:8080/qiita_builder/article/posted")
        await expect(apiToken).toBe('token')
        await expect(dispatch).toHaveBeenCalledTimes(1)
        await expect(dispatch).toHaveBeenCalledWith('window/setInternalServerError', true, {root: true})
    })
    test('actions: fetchFeedbackArticles', async () => {
        const userId = 1;
        mockReturn = articles;
        await user.actions.fetchFeedbackArticles({dispatch, commit, rootState, rootGetters}, userId);
        await expect(url).toBe("http://localhost:8080/qiita_builder/article/feedbacked")
        await expect(apiToken).toBe('token')
        await expect(commit).toHaveBeenCalledTimes(1)
        await expect(commit).toHaveBeenCalledWith('setFeedbackArticles', articles)
    })
    test('actions: fetchFeedbackArticles(400error)', async () => {
        const userId = 1;
        mockError = true;
        mockHttpStatus = 400;
        await user.actions.fetchFeedbackArticles({dispatch, commit, rootState, rootGetters}, userId);
        await expect(url).toBe("http://localhost:8080/qiita_builder/article/feedbacked")
        await expect(apiToken).toBe('token')
        await expect(dispatch).toHaveBeenCalledTimes(1)
        await expect(dispatch).toHaveBeenCalledWith('window/setNotFound', true, {root: true})
    })
    test('actions: fetchFeedbackArticles(500error)', async () => {
        const userId = 1;
        mockError = true;
        mockHttpStatus = 500;
        await user.actions.fetchFeedbackArticles({dispatch, commit, rootState, rootGetters}, userId);
        await expect(url).toBe("http://localhost:8080/qiita_builder/article/feedbacked")
        await expect(apiToken).toBe('token')
        await expect(dispatch).toHaveBeenCalledTimes(1)
        await expect(dispatch).toHaveBeenCalledWith('window/setInternalServerError', true, {root: true})
    })
    test('actions: fetchMyArticles', async () => {
        const userId = 1;
        mockReturn = articles;
        await user.actions.fetchMyArticles({dispatch, commit, rootState, rootGetters}, userId);
        await expect(url).toBe("http://localhost:8080/qiita_builder/article/my-articles")
        await expect(apiToken).toBe('token')
        await expect(commit).toHaveBeenCalledTimes(1)
        await expect(commit).toHaveBeenCalledWith('setMyArticles', articles)
    })
    test('actions: fetchMyArticles(400error)', async () => {
        const userId = 1;
        mockError = true;
        mockHttpStatus = 400;
        await user.actions.fetchMyArticles({dispatch, commit, rootState, rootGetters}, userId);
        await expect(url).toBe("http://localhost:8080/qiita_builder/article/my-articles")
        await expect(apiToken).toBe('token')
        await expect(dispatch).toHaveBeenCalledTimes(1)
        await expect(dispatch).toHaveBeenCalledWith('window/setNotFound', true, {root: true})
    })
    test('actions: fetchMyArticles(500error)', async () => {
        const userId = 1;
        mockError = true;
        mockHttpStatus = 500;
        await user.actions.fetchMyArticles({dispatch, commit, rootState, rootGetters}, userId);
        await expect(url).toBe("http://localhost:8080/qiita_builder/article/my-articles")
        await expect(apiToken).toBe('token')
        await expect(dispatch).toHaveBeenCalledTimes(1)
        await expect(dispatch).toHaveBeenCalledWith('window/setInternalServerError', true, {root: true})
    })
    test('actions: findUserIdByUid', async () => {
        const userId = 1;
        const uId = 'a'
        mockReturn = userId
        await user.actions.findUserIdByUid({dispatch, commit, rootState, rootGetters}, uId);
        await expect(url).toBe("http://localhost:8080/qiita_builder/userId")
        await expect(apiToken).toBe('token')
        await expect(commit).toHaveBeenCalledTimes(1)
        await expect(commit).toHaveBeenCalledWith('setUserId', userId)
    })
    test('actions: findUserIdByUid(400error)', async () => {
        const uId = 'a'
        mockError = true;
        mockHttpStatus = 400;
        try {
            await user.actions.findUserIdByUid({dispatch, commit, rootState, rootGetters}, uId);
        } catch (error) {//try内のmethodでreject()でerrorを返しているのでcatchしたらテスト実行
            await expect(url).toBe("http://localhost:8080/qiita_builder/userId")
            await expect(apiToken).toBe('token')
            await expect(dispatch).toHaveBeenCalledTimes(1)
            await expect(dispatch).toHaveBeenCalledWith('window/setNotFound', true, {root: true})
        }
    })
    test('actions: fetchUserIdByUid(500error)', async () => {
        const uId = 'a'
        mockError = true;
        mockHttpStatus = 500;
        try {
            await user.actions.findUserIdByUid({dispatch, commit, rootState, rootGetters}, uId);
        } catch (error) {
            await expect(url).toBe("http://localhost:8080/qiita_builder/userId")
            await expect(apiToken).toBe('token')
            await expect(dispatch).toHaveBeenCalledTimes(1)
            await expect(dispatch).toHaveBeenCalledWith('window/setInternalServerError', true, {root: true})
        }
    })
})

const userDetail = {
    userId: 1,
    displayName: "a",
    photoUrl: "a",
    usedTags: [
        {
            tagId: 1,
            tagName: 'Java',
            usedTagCount: 1,
        },
        {
            tagId: 2,
            tagName: 'go',
            usedTagCount: 1
        },
        {
            tagId: 3,
            tagName: 'Javascript',
            usedTagCount: 2
        }
    ],
    feedbackCount: 3,
    qiitaRecommendedAllCount: 2,
    postedArticleCount: 0,
    isLoginUser: true,
}
const notLoginUserDetail = {
    userId: 2,
    displayName: "b",
    photoUrl: "b",
    usedTags: [
        {
            tagId: 1,
            tagName: 'Java',
            usedTagCount: 1,
        },
        {
            tagId: 2,
            tagName: 'go',
            usedTagCount: 1
        },
        {
            tagId: 3,
            tagName: 'Javascript',
            usedTagCount: 2
        }
    ],
    feedbackCount: 3,
    qiitaRecommendedAllCount: 2,
    postedArticleCount: 0,
    isLoginUser: false,
}
const articles = [
    {
        articleId: 1,
        title: 'title1',
        createdAt: "00:00:01",
        updatedAt: "00:00:11",
        stateFlag: 1,
        feedbackCount: 1,
        registeredMyArticleCount: 2,
        qiitaRecommendPoint: 1,
        postedUser: {
            userId: 1,
            displayName: "user1",
            photoUrl: "photo1"
        },
        tags: [{
            tagId: 1,
            tagName: 'Java',
        }, {
            tagId: 3,
            tagName: "Javascript"
        }]
    },
    {
        articleId: 2,
        title: 'title2',
        createdAt: "00:00:02",
        updatedAt: "00:00:22",
        stateFlag: 2,
        feedbackCount: 2,
        registeredMyArticleCount: 1,
        qiitaRecommendPoint: 1,
        postedUser: {
            userId: 2,
            displayName: "user2",
            photoUrl: "photo2"
        },
        tags: [{
            tagId: 2,
            tagName: 'go',
        }, {
            tagId: 3,
            tagName: "Javascript"
        }]
    },
    {
        articleId: 3,
        title: 'title3',
        createdAt: "00:00:03",
        updatedAt: "00:00:33",
        stateFlag: 0,
        feedbackCount: 2,
        registeredMyArticleCount: 1,
        qiitaRecommendPoint: 1,
        postedUser: {
            userId: 1,
            displayName: "user1",
            photoUrl: "photo1"
        },
        tags: [{
            tagId: 2,
            tagName: 'go',
        }, {
            tagId: 3,
            tagName: "Javascript"
        }]
    },
    {
        articleId: 4,
        title: 'title4',
        createdAt: "00:00:04",
        updatedAt: "00:00:44",
        stateFlag: 0,
        feedbackCount: 2,
        registeredMyArticleCount: 1,
        qiitaRecommendPoint: 1,
        postedUser: {
            userId: 2,
            displayName: "user2",
            photoUrl: "photo2"
        },
        tags: [{
            tagId: 3,
            tagName: "Javascript"
        }]
    },
]
const user1PostedArticles = [
    {
        articleId: 1,
        title: 'title1',
        createdAt: "00:00:01",
        updatedAt: "00:00:11",
        stateFlag: 1,
        feedbackCount: 1,
        registeredMyArticleCount: 2,
        qiitaRecommendPoint: 1,
        postedUser: {
            userId: 1,
            displayName: "user1",
            photoUrl: "photo1"
        },
        tags: [{
            tagId: 1,
            tagName: 'Java',
        }, {
            tagId: 3,
            tagName: "Javascript"
        }]
    },
    {
        articleId: 3,
        title: 'title3',
        createdAt: "00:00:03",
        updatedAt: "00:00:33",
        stateFlag: 0,
        feedbackCount: 2,
        registeredMyArticleCount: 1,
        qiitaRecommendPoint: 1,
        postedUser: {
            userId: 1,
            displayName: "user1",
            photoUrl: "photo1"
        },
        tags: [{
            tagId: 2,
            tagName: 'go',
        }, {
            tagId: 3,
            tagName: "Javascript"
        }]
    },
]
const user2PostedArticles = [
    {
        articleId: 2,
        title: 'title2',
        createdAt: "00:00:02",
        updatedAt: "00:00:22",
        stateFlag: 2,
        feedbackCount: 2,
        registeredMyArticleCount: 1,
        qiitaRecommendPoint: 1,
        postedUser: {
            userId: 2,
            displayName: "user2",
            photoUrl: "photo2"
        },
        tags: [{
            tagId: 2,
            tagName: 'go',
        }, {
            tagId: 3,
            tagName: "Javascript"
        }],
    },
    {
        articleId: 4,
        title: 'title4',
        createdAt: "00:00:04",
        updatedAt: "00:00:44",
        stateFlag: 0,
        feedbackCount: 2,
        registeredMyArticleCount: 1,
        qiitaRecommendPoint: 1,
        postedUser: {
            userId: 2,
            displayName: "user2",
            photoUrl: "photo2"
        },
        tags: [{
            tagId: 3,
            tagName: "Javascript"
        }]
    },
]
const usedTags = [
    {
        tagId: 1,
        tagName: 'Java',
    }, {
        tagId: 2,
        tagName: 'go'
    }, {
        tagId: 3,
        tagName: "Javascript"
    }
]
const chartData = [
    {
        labels: ['Java', 'go', 'Javascript'],
        datasets: [
            {
                data: [2, 1, 1],
                backgroundColor: ['#41ab5d', '#78c679', '#addd8e'],
            },
        ]
    }
]