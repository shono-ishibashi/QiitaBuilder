import {shallowMount, createLocalVue} from '@vue/test-utils';
import Vuex from 'vuex';
import Component from '@/views/UserDetail';
import {afterEach, beforeEach, describe, jest} from "@jest/globals";
import Vuetify from "vuetify";

const localVue = createLocalVue();
localVue.use(Vuex);

let wrapper;
let store;
let user_actions;
let user_mutations;
let user_state;
let user_getters;
let auth_getters;
let auth_state;
let myMock = jest.fn();
const sel = id => `[data-testid="${id}"]`//sel('id')の形でidにtemplate内に付与されたdata-test属性の名前を指定したら付与された要素を指定できる

beforeEach(() => {

    user_actions = {
        setArticlesAndTags: jest.fn(),
        setArticles: jest.fn(),
        setChartData: jest.fn(),
        fetchUserDetail: jest.fn(),
        fetchPostedArticles: jest.fn(),
        fetchFeedbackArticles: jest.fn(),
        fetchMyArticles: jest.fn(),
        findUserIdByUid: jest.fn(),
        clearState: jest.fn()
    };
    user_mutations = {};
    user_state = {
        userDetail: {
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
        },
        postedArticles: [],
        myArticles: [],
        feedbackArticles: [],
        displayArticles: [],
        usedTags: [],
        chartData: null,
    };
    user_getters = {
        userId() {
            return 1
        },
        postedArticles() {
            return [
                {
                    articleId: 1
                },
                {
                    articleId: 2
                },
                {
                    articleId: 3
                }]
        },
        notDraftArticles() {
            return [{
                articleId: 1
            },
                {
                    articleId: 2
                },
                {
                    articleId: 3
                }]
        },
        draftArticles() {
            return [{
                articleId: 1
            },
                {
                    articleId: 2
                },
                {
                    articleId: 3
                }]
        },
        feedbackArticles() {
            return [{
                articleId: 1
            },
                {
                    articleId: 2
                },
                {
                    articleId: 3
                }]
        },
        myArticles() {
            return [{
                articleId: 1
            },
                {
                    articleId: 2
                },
                {
                    articleId: 3
                }]
        },
        displayArticles() {
            return [
                {
                    articleId: 1
                },
                {
                    articleId: 2
                },
                {
                    articleId: 3
                }]
        },
        usedTags() {
            return [{
                tagId: 1,
                tagName: 'Java',
            }, {
                tagId: 2,
                tagName: 'go'
            }, {
                tagId: 3,
                tagName: "Javascript"
            }]
        },
        chartData() {
            return [
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
        },
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
            user: {
                namespaced: true,
                state: user_state,
                mutations: user_mutations,
                actions: user_actions,
                getters: user_getters
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

describe('Testing UserDetail Component', () => {
    test('is a Vue instance', () => {
        expect(wrapper.isVueInstance).toBeTruthy();
    })
    describe('Testing exist element', () => {
        test('UserInfo Component', async () => {
            //visible
            await wrapper.setData({isDisplay: true})
            await expect(wrapper.findAllComponents({name: 'UserInfo'}).isVisible()).toBeTruthy();

            //invisible
            await wrapper.setData({isDisplay: false})
            await expect(wrapper.findAllComponents({name: 'UserInfo'}).isVisible()).toBeFalsy();

            await expect(wrapper.findComponent({name: 'UserInfo'}).exists()).toBeTruthy();
            await expect(wrapper.findAllComponents({name: 'UserInfo'}).length).toBe(1);
        })
        test('v-progress-linear', async () => {
            //visible
            await wrapper.setData({isLoading: true})
            await expect(wrapper.findAllComponents({name: 'v-progress-linear'}).isVisible()).toBeTruthy();

            //invisible
            await wrapper.setData({isLoading: false})
            await expect(wrapper.findAllComponents({name: 'v-progress-linear'}).isVisible()).toBeFalsy();

            await expect(wrapper.findComponent({name: 'v-progress-linear'}).exists()).toBeTruthy();
            await expect(wrapper.findAllComponents({name: 'v-progress-linear'}).length).toBe(2);
        })
        test('change list tabs', () => {
            //login userのマイページもしくはユーザー詳細画面
            expect(wrapper.find(sel('login-tab0')).text()).toBe('投稿記事')
            expect(wrapper.find(sel('login-tab1')).text()).toBe('FBした記事')
            expect(wrapper.find(sel('login-tab2')).text()).toBe('お気に入り記事')
            expect(wrapper.find(sel('login-tab3')).text()).toBe('下書き記事')
            expect(wrapper.find(sel('login-tab4')).exists()).toBeFalsy();

            expect(wrapper.find(sel('not-login-tabs')).exists()).toBeFalsy();

            //login user以外のユーザー詳細画面
            user_state.userDetail.isLoginUser = false;
            wrapper = shallowMount(Component, {
                store,
                localVue
            });
            expect(wrapper.find(sel('login-tabs')).exists()).toBeFalsy();
            expect(wrapper.find(sel('not-login-tabs')).exists()).toBeTruthy();
            expect(wrapper.find(sel('not-login-tab0')).text()).toBe('公開中の投稿記事')
            expect(wrapper.find(sel('not-login-tab1')).text()).toBe('公開中のFBした記事')
            expect(wrapper.find(sel('not-login-tab2')).text()).toBe('公開中のお気に入り記事')
            expect(wrapper.find(sel('not-login-tabs3')).exists()).toBeFalsy();
        })
        test('change list state selector', () => {
            const stateSelector = wrapper.find(sel('list-state-selector'))
            expect(stateSelector.props().items.length).toBe(3)
            expect(stateSelector.props().label).toBe('絞り込み')
            expect(stateSelector.props().items[0].name).toBe('全記事')
            expect(stateSelector.props().items[0].id).toBe(10)
            expect(stateSelector.props().items[1].name).toBe('Qiita未投稿記事')
            expect(stateSelector.props().items[1].id).toBe(1)
            expect(stateSelector.props().items[2].name).toBe('Qiita投稿済み記事')
            expect(stateSelector.props().items[2].id).toBe(2)

            expect(stateSelector.props().value).toBe(wrapper.vm.displayListState)
        })
        test('change sort num selector', () => {
            const sortSelector = wrapper.find(sel('sort-selector'))
            expect(sortSelector.props().items.length).toBe(4)
            expect(sortSelector.props().label).toBe('並び順')
            expect(sortSelector.props().items[0].state).toBe('新着順')
            expect(sortSelector.props().items[0].key).toBe(0)
            expect(sortSelector.props().items[1].state).toBe('更新順')
            expect(sortSelector.props().items[1].key).toBe(1)
            expect(sortSelector.props().items[2].state).toBe('Qiita推奨数順')
            expect(sortSelector.props().items[2].key).toBe(2)
            expect(sortSelector.props().items[3].state).toBe('お気に入り登録数順')
            expect(sortSelector.props().items[3].key).toBe(3)

            expect(sortSelector.props().value).toBe(wrapper.vm.sortNum)
        })
        test('search title text field', () => {
            const textField = wrapper.find(sel('search-title'))
            expect(textField.exists()).toBeTruthy();
            expect(textField.props().label).toBe('記事タイトルを入力')
        })
        test('Article card component', () => {
            const articleCard0 = wrapper.find(sel('article-card0'))
            const articleCard1 = wrapper.find(sel('article-card1'))
            const articleCard2 = wrapper.find(sel('article-card2'))
            expect(wrapper.find(sel('article-card3')).exists()).toBeFalsy()
            expect(articleCard0.props().index).toBe(0)
            expect(articleCard1.props().index).toBe(1)
            expect(articleCard2.props().index).toBe(2)

            expect(articleCard0.props().article).toBe(wrapper.vm.sortedArticles[0])
            expect(articleCard1.props().article).toBe(wrapper.vm.sortedArticles[1])
            expect(articleCard2.props().article).toBe(wrapper.vm.sortedArticles[2])
        })
        test('v-alert no articles', () => {
            expect(wrapper.find(sel('no-articles-alert')).isVisible()).toBeFalsy()
            user_getters = {
                userId() {
                    return 1
                },
                postedArticles() {
                    return []
                },
                notDraftArticles() {
                    return []
                },
                draftArticles() {
                    return []
                },
                feedbackArticles() {
                    return []
                },
                myArticles() {
                    return []
                },
                displayArticles() {
                    return []
                },
                usedTags() {
                    return []
                },
                chartData() {
                    return []
                },
            }
            store = new Vuex.Store({
                state: {},
                mutations: {},
                actions: {},
                getters: {},
                modules: {
                    user: {
                        namespaced: true,
                        state: user_state,
                        mutations: user_mutations,
                        actions: user_actions,
                        getters: user_getters
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
            const noArticleAlert = wrapper.find(sel('no-articles-alert'))
            expect(noArticleAlert.isVisible()).toBeTruthy();
        })
        test('v-pagination length', () => {
            expect(wrapper.find(sel('article-pagination')).props().length).toBe(1)
            user_getters = {
                userId() {
                    return 1
                },
                postedArticles() {
                    return []
                },
                notDraftArticles() {
                    return []
                },
                draftArticles() {
                    return []
                },
                feedbackArticles() {
                    return []
                },
                myArticles() {
                    return []
                },
                displayArticles() {
                    return [
                        {
                            articleId: 1
                        },
                        {
                            articleId: 2
                        },
                        {
                            articleId: 3
                        },
                        {
                            articleId: 4
                        },
                        {
                            articleId: 5
                        },
                        {
                            articleId: 6
                        }
                    ]
                },
                usedTags() {
                    return []
                },
                chartData() {
                    return []
                },
            }
            store = new Vuex.Store({
                state: {},
                mutations: {},
                actions: {},
                getters: {},
                modules: {
                    user: {
                        namespaced: true,
                        state: user_state,
                        mutations: user_mutations,
                        actions: user_actions,
                        getters: user_getters
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
            expect(wrapper.find(sel('article-pagination')).props().length).toBe(2)
        })
    })
})