import {shallowMount, createLocalVue} from '@vue/test-utils';
import Vuex from 'vuex';
import Component from '@/views/UserDetail';
import {afterEach, beforeEach, describe, jest} from "@jest/globals";

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
        test('login list tabs', () => {
            const loginTab0=wrapper.find(sel('login-list-tabs0'))
            expect(wrapper.find(sel('login-list-tabs0')).text()).toBe('投稿記事');
            expect(wrapper.find(sel('login-list-tabs1')).exists).toBeTruthy();
            expect(loginTab0.text()).toBe('投稿記事');
            console.log(loginTab0.text())
            const tabs=wrapper.findAllComponents({name:'v-tab'}).at(0)
            console.log(tabs.text())
        })
    })
})