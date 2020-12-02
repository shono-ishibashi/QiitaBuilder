import {shallowMount, createLocalVue} from '@vue/test-utils';
import Vuex from 'vuex';
import Component from '@/views/Ranking';

const localVue = createLocalVue();
localVue.use(Vuex);

let wrapper;
let store;
let users_actions;
let users_mutations;
let users_state;
let users_getters;
let auth_getters;
let auth_state;
let myMock = jest.fn();

beforeEach(() => {

    users_actions = {
        fetchRankingUser: jest.fn()
    };
    users_mutations = {};
    users_state = {
        rankingUsers: []
    };
    users_getters = {
        users() {
            return [
                {
                    userId: 1
                },
                {
                    userId: 2
                },
                {
                    userId: 3
                }
            ]
        },
        relationArticles() {
            return [
                {
                    articleId: 1
                },
                {
                    articleId: 2
                },
                {
                    articleId: 3
                }
            ]
        }
    };


    auth_state = {
        apiToken: 'token'
    };
    myMock.mockReturnValueOnce(auth_state.apiToken);
    auth_getters = {
        apiToken: myMock
    };

    store = new Vuex.Store({
        state: {},
        mutations: {},
        actions: {},
        getters: {},
        modules: {
            users: {
                namespaced: true,
                state: users_state,
                mutations: users_mutations,
                actions: users_actions,
                getters: users_getters
            },
            auth: {
                namespaced: true,
                state: auth_state,
                getters: auth_getters
            }
        }
    });

    wrapper = shallowMount(Component, {
        store,
        localVue
    });
})

afterEach(() => {
    wrapper.destroy();
})


describe('Testing Ranking component', () => {
    test('is a Vue instance', () => {
        expect(wrapper.isVueInstance).toBeTruthy();
    })
})

describe('Testing exist element', () => {
    test('v-select', () => {
        expect(wrapper.findComponent({name: 'v-select'}).exists()).toBeTruthy();
        expect(wrapper.findAllComponents({name: 'v-select'}).length).toBe(2);
    })

    test('v-progress-linear', async () => {
        //visible
        await wrapper.setData({isLoading: true})
        await expect(wrapper.findAllComponents({name: 'v-progress-linear'}).isVisible()).toBeTruthy();

        //invisible
        await wrapper.setData({isLoading: false})
        await expect(wrapper.findAllComponents({name: 'v-progress-linear'}).isVisible()).toBeFalsy();

        await expect(wrapper.findComponent({name: 'v-progress-linear'}).exists()).toBeTruthy();
        await expect(wrapper.findAllComponents({name: 'v-progress-linear'}).length).toBe(3);
    })

    test('UserList', async () => {
        //visible
        await wrapper.setData({isDisplay: true})
        await expect(wrapper.findAllComponents({name: 'UserList'}).isVisible()).toBeTruthy();

        //invisible
        await wrapper.setData({isDisplay: false})
        await expect(wrapper.findAllComponents({name: 'UserList'}).isVisible()).toBeFalsy();

        await expect(wrapper.findComponent({name: 'UserList'}).exists()).toBeTruthy();
        await expect(wrapper.findAllComponents({name: 'UserList'}).length).toBe(1);
    })

    test('ChartArea', async () => {
        //visible
        await wrapper.setData({isDisplay: true})
        await expect(wrapper.findAllComponents({name: 'ChartArea'}).isVisible()).toBeTruthy();

        //invisible
        await wrapper.setData({isDisplay: false})
        await expect(wrapper.findAllComponents({name: 'ChartArea'}).isVisible()).toBeFalsy();

        await expect(wrapper.findComponent({name: 'ChartArea'}).exists()).toBeTruthy();
        await expect(wrapper.findAllComponents({name: 'ChartArea'}).length).toBe(1);
    })

    test('RelationArticles', async () => {
        //visible
        await wrapper.setData({isDisplay: true})
        await expect(wrapper.findAllComponents({name: 'RelationArticles'}).isVisible()).toBeTruthy();

        //invisible
        await wrapper.setData({isDisplay: false})
        await expect(wrapper.findAllComponents({name: 'RelationArticles'}).isVisible()).toBeFalsy();

        await expect(wrapper.findComponent({name: 'RelationArticles'}).exists()).toBeTruthy();
        await expect(wrapper.findAllComponents({name: 'RelationArticles'}).length).toBe(1);
    })

    test('v-alert', async () => {
        //non-exist
        await expect(wrapper.findComponent({name: 'v-alert'}).exists()).toBeFalsy();

        //exist
        await wrapper.setData({rankUsersLength: 0})
        await expect(wrapper.findComponent({name: 'v-alert'}).exists()).toBeTruthy();

        //non-exist
        await wrapper.setData({rankUsersLength: 10})
        await expect(wrapper.findComponent({name: 'v-alert'}).exists()).toBeFalsy()
    })
})

describe('Testing computed', () => {

    describe('rankTitle', () => {
        test('rankTitle/FBCount', async () => {
            await wrapper.setData({selectRankItemId: 1});
            await expect(wrapper.vm.rankTitle).toBe('FBした数ランキング');
        })

        test('rankTitle/articleCount', async () => {
            await wrapper.setData({selectRankItemId: 2});
            await expect(wrapper.vm.rankTitle).toBe('記事投稿数ランキング');
        })

        test('rankTitle/qiitaRecommendedCount', async () => {
            await wrapper.setData({selectRankItemId: 3});
            await expect(wrapper.vm.rankTitle).toBe('Qiita推薦累計数ランキング');
        })

        test('rankTitle/予期しないselectItemId1', async () => {
            await wrapper.setData({selectRankItemId: 10});
            await expect(wrapper.vm.rankTitle).toBe('FBした数ランキング');
        })

        test('rankTitle/予期しないselectItemId2', async () => {
            await wrapper.setData({selectRankItemId: 'aaa'});
            await expect(wrapper.vm.rankTitle).toBe('FBした数ランキング');
        })
    })

    describe('apiToken', () => {
        test('apiToken', async () => {
            await expect(auth_getters.apiToken).toHaveBeenCalled();
            await expect(wrapper.vm.apiToken).toBe('token');
        })
    })
})

describe('Testing watch property', () => {

    test('apiToken', async () => {
        //computedの上書き
        wrapper = await shallowMount(Component, {
            store,
            localVue,
            data() {
                return {
                    testData: ''
                }
            },
            computed: {
                apiToken: {
                    get() {
                        return 'computed token' + this.testData;
                    },
                    set(val) {
                        this.testData = val;
                    }
                }
            }
        });

        // 処理前
        await expect(users_actions.fetchRankingUser).not.toBeCalled();
        await expect(wrapper.vm.rankUsersLength).toBeNull();

        // 監視対象の変更

        const func = function () {
            wrapper.vm.apiToken = 'test';
        } //computedのset()が実行される

        await func()

        // 処理後
        await wrapper.vm.$nextTick();
        await expect(users_actions.fetchRankingUser).toBeCalled();
        await expect(wrapper.vm.rankUsersLength).toBe(3);
    })


    test('selectRankItemId/rankUserが存在する場合', async () => {
        jest.useFakeTimers();
        // 処理前
        await expect(users_actions.fetchRankingUser).not.toBeCalled();
        await expect(wrapper.vm.rankUsersLength).toBeNull();
        await expect(wrapper.vm.rankItemId).toBe(1);
        await expect(wrapper.vm.isLoading).toBeFalsy();
        await expect(wrapper.vm.isDisplay).toBeTruthy();

        // 監視対象の変更
        await wrapper.setData({selectRankItemId: 2});

        // 処理後
        await expect(users_actions.fetchRankingUser).toBeCalled();
        await expect(wrapper.vm.rankUsersLength).toBe(3)

        await wrapper.vm.$nextTick(() => {
            expect(wrapper.vm.isLoading).toBeTruthy();
            expect(wrapper.vm.isDisplay).toBeFalsy();
        })

        await jest.advanceTimersByTime(1300);
        await wrapper.vm.$nextTick(() => {
            expect(wrapper.vm.isLoading).toBeFalsy();
            expect(wrapper.vm.isDisplay).toBeTruthy();
        })
        await expect(wrapper.vm.rankItemId).toBe(2);
    })

    test('selectRankItemId/rankUserが存在しない場合', async () => {
        // 事前準備
        users_actions = {
            fetchRankingUser: jest.fn()
        };
        users_mutations = {};
        users_state = {
            rankingUsers: []
        };
        users_getters = {
            users() {
                return []
            },
            relationArticles() {
                return []
            }
        };

        store = new Vuex.Store({
            state: {},
            mutations: {},
            actions: {},
            getters: {},
            modules: {
                users: {
                    namespaced: true,
                    state: users_state,
                    mutations: users_mutations,
                    actions: users_actions,
                    getters: users_getters
                }
            }
        });
        wrapper = shallowMount(Component, {
            store,
            localVue
        });

        // 処理前
        await expect(users_actions.fetchRankingUser).not.toBeCalled();
        await expect(wrapper.vm.rankUsersLength).toBeNull();
        await expect(wrapper.vm.rankItemId).toBe(1);
        await expect(wrapper.vm.isLoading).toBeFalsy();
        await expect(wrapper.vm.isDisplay).toBeTruthy();

        // 監視対象の変更
        await wrapper.setData({selectRankItemId: 2});

        // 処理後
        await expect(users_actions.fetchRankingUser).toBeCalled();
        await expect(wrapper.vm.rankUsersLength).toBe(0);

        await wrapper.vm.$nextTick(() => {
            expect(wrapper.vm.isLoading).toBeFalsy();
            expect(wrapper.vm.isDisplay).toBeFalsy();
        })

        await expect(wrapper.vm.rankItemId).toBe(2);
    })
})
