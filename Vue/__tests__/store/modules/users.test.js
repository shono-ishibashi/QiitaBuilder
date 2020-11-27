import index from '@/store/index';
import users from '@/store/modules/users';
import {createLocalVue} from '@vue/test-utils';
import {cloneDeep} from 'lodash';
import axios from "axios";
import Vuex from 'vuex';

const localVue = createLocalVue();
localVue.use(Vuex);

const replaceRankingUsers = [
    {
        user: {
            userId: 1,
            displayName: 'user1',
            photoUrl: "photo1"
        },
        relationArticle: {
            articleId: 1,
            title: 'title1'
        },
        rank: 1
    },
    {
        user: {
            userId: 2,
            displayName: 'user2',
            photoUrl: "photo2"
        },
        relationArticle: {
            articleId: 2,
            title: 'title2'
        },
        rank: 2
    },
    {
        user: {
            userId: 3,
            displayName: 'user3',
            photoUrl: "photo3"
        },
        relationArticle: {
            articleId: 3,
            title: 'title3'
        },
        rank: 3
    },
    {
        user: {
            userId: 4,
            displayName: 'user4',
            photoUrl: "photo4"
        },
        relationArticle: {
            articleId: 2,
            title: 'title2'
        },
        rank: 4
    },
    {
        user: {
            userId: 5,
            displayName: 'user5',
            photoUrl: "photo5"
        },
        relationArticle: {
            articleId: 5,
            title: 'title5'
        },
        rank: 5
    },];

let url = ''
let apiToken = '';
let mockError = false

// axiosのモック化
jest.mock('axios', () => ({
    get: jest.fn((_url, {
        headers: {
            'Authorization': _apiToken
        }
    }) => {
        return new Promise((resolve) => {
            if (mockError) {
                throw Error("Mock Error")
            }
            url = _url;
            apiToken = _apiToken;
            resolve({
                data: replaceRankingUsers
            })
        })
    })
}))

describe('store/users.js', () => {

    //************ actions ************
    describe("actions", () => {
        let store;
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
                        }
                    }
                }
            })
            store.registerModule('users', cloneDeep(users))
        })

        test('actions: fetchRankingUser/FBCount', async () => {
            let commit = cloneDeep(jest).fn();
            let rootState = store.state;
            let rootGetters = store.getters;
            const selectRankItemId = 1;

            await users.actions.fetchRankingUser({commit, rootState, rootGetters}, selectRankItemId);
            await expect(url).toBe("http://localhost:8080/qiita_builder/user/ranking/FBCount")
            await expect(apiToken).toBe('token')
            await expect(commit).toHaveBeenCalledTimes(2)
            await expect(commit).toHaveBeenCalledWith('resetRankingUsers')
            await expect(commit).toHaveBeenCalledWith('setRankingUsers', replaceRankingUsers)
        })

        test('actions: fetchRankingUser/articleCount', async () => {
            let commit = cloneDeep(jest).fn();
            let rootState = store.state;
            let rootGetters = store.getters;
            const selectRankItemId = 2;

            await users.actions.fetchRankingUser({commit, rootState, rootGetters}, selectRankItemId);
            await expect(url).toBe("http://localhost:8080/qiita_builder/user/ranking/articleCount")
            await expect(apiToken).toBe('token')
            await expect(commit).toHaveBeenCalledTimes(2)
            await expect(commit).toHaveBeenCalledWith('resetRankingUsers')
            await expect(commit).toHaveBeenCalledWith('setRankingUsers', replaceRankingUsers)
        })

        test('actions: fetchRankingUser/qiitaRecommendedCount', async () => {
            let commit = cloneDeep(jest).fn();
            let rootState = store.state;
            let rootGetters = store.getters;
            const selectRankItemId = 3;

            await users.actions.fetchRankingUser({commit, rootState, rootGetters}, selectRankItemId);
            await expect(url).toBe("http://localhost:8080/qiita_builder/user/ranking/qiitaRecommendedCount");
            await expect(apiToken).toBe('token');
            await expect(commit).toHaveBeenCalledTimes(2);
            await expect(commit).toHaveBeenCalledWith('resetRankingUsers');
            await expect(commit).toHaveBeenCalledWith('setRankingUsers', replaceRankingUsers);
        })
    })

    //************ mutations ************
    describe('mutations', () => {
        let store;
        beforeEach(() => {
            store = new Vuex.Store(cloneDeep(users));
        })

        test('mutations: setRankingUsers', () => {
            expect(store.state.rankingUsers).toStrictEqual([]);
            store.commit('setRankingUsers', replaceRankingUsers);
            expect(store.state.rankingUsers).toStrictEqual(replaceRankingUsers);

        })

        test('mutations: resetRankingUsers', () => {
            store.replaceState({
                rankingUsers: replaceRankingUsers
            })
            expect(store.state.rankingUsers).toStrictEqual(replaceRankingUsers);
            store.commit('resetRankingUsers');
            expect(store.state.rankingUsers).toStrictEqual([]);
        })
    })

    //************ getters ************
    describe('getters', () => {
        let store;
        beforeEach(() => {
            store = new Vuex.Store(cloneDeep(users))
        })

        test('getters: users', () => {
            store.replaceState({
                rankingUsers: replaceRankingUsers
            });

            const expected = [
                {
                    userId: 1,
                    displayName: 'user1',
                    photoUrl: 'photo1',
                    rank: 1
                },
                {
                    userId: 2,
                    displayName: 'user2',
                    photoUrl: 'photo2',
                    rank: 2
                },
                {
                    userId: 3,
                    displayName: 'user3',
                    photoUrl: 'photo3',
                    rank: 3
                },
                {
                    userId: 4,
                    displayName: 'user4',
                    photoUrl: 'photo4',
                    rank: 4
                },
                {
                    userId: 5,
                    displayName: 'user5',
                    photoUrl: 'photo5',
                    rank: 5
                }
            ];
            expect(store.getters.users.length).toBe(5);
            expect(store.getters.users).toStrictEqual(expected);
        })

        test('getters: relationArticles', () => {
            store.replaceState({
                rankingUsers: replaceRankingUsers
            });

            const expected = [
                {
                    articleId: 1,
                    title: 'title1'
                },
                {
                    articleId: 2,
                    title: 'title2'
                },
                {
                    articleId: 3,
                    title: 'title3'
                },
                {
                    articleId: 5,
                    title: 'title5'
                }
            ];

            expect(store.getters.relationArticles.length).toBe(4);
            expect(store.getters.relationArticles).toStrictEqual(expected);
        })
    })
})


