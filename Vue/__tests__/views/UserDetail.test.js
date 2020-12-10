import {shallowMount, createLocalVue} from '@vue/test-utils';
import Vuex from 'vuex';
import Component from '@/views/UserDetail';
import {afterEach, beforeEach, describe, jest} from "@jest/globals";
import * as palette from 'google-palette/palette'
jest.mock('google-palette/palette.js')

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
const sel = id => `[data-test-id="${id}"]`//sel('id')の形でidにtemplate内に付与されたdata-test属性の名前を指定したら付与された要素を指定できる

beforeEach(() => {
    //jest.resetModules()
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
                    usedTagCount: 2
                },
                {
                    tagId: 3,
                    tagName: 'Javascript',
                    usedTagCount: 3
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
                    articleId: 1,
                    title: 'title1',
                    createdAt: "00:00:01",
                    updatedAt: "11:11:11",
                    stateFlag: 1,
                    feedbackCount: 1,
                    registeredMyArticleCount: 3,
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
                    updatedAt: "00:22:22",
                    stateFlag: 2,
                    feedbackCount: 2,
                    registeredMyArticleCount: 2,
                    qiitaRecommendPoint: 2,
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
                    updatedAt: "00:03:33",
                    stateFlag: 1,
                    feedbackCount: 2,
                    registeredMyArticleCount: 1,
                    qiitaRecommendPoint: 3,
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
/*const mock_fn=jest.fn()

jest.mock('google-palette', function () {
    return 'a'/!*{
        palette: function (value){
            console.log('palette: ' +value)
            return 'a'
        }
    }*!/
})*/
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
    describe('Testing computed', () => {

        describe('sortedArticles', () => {

            test('sortedArticles/sortNum=0(order by createdAt)', async () => {
                await wrapper.setData({sortNum: 0});
                await expect(wrapper.vm.sortedArticles[0].articleId).toBe(3)
                await expect(wrapper.vm.sortedArticles[2].articleId).toBe(1)
            })

            test('sortedArticles/sortNum=1(order by updateAt)', async () => {
                await wrapper.setData({sortNum: 1});
                await expect(wrapper.vm.sortedArticles[0].articleId).toBe(1)
                await expect(wrapper.vm.sortedArticles[2].articleId).toBe(3)
            })

            test('sortedArticles/sortNum=2(order by qiitaRecommendPoint)', async () => {
                await wrapper.setData({sortNum: 2});
                await expect(wrapper.vm.sortedArticles[0].articleId).toBe(3)
                await expect(wrapper.vm.sortedArticles[2].articleId).toBe(1)
            })

            test('sortedArticles/sortNum=0(order by myArticleCount)', async () => {
                await wrapper.setData({sortNum: 3});
                await expect(wrapper.vm.sortedArticles[0].articleId).toBe(1)
                await expect(wrapper.vm.sortedArticles[2].articleId).toBe(3)
            })

            test('sortedArticles/paging', async () => {
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
                                articleId: 1,
                                title: 'title1',
                                createdAt: "00:00:01",
                                updatedAt: "11:11:11",
                                stateFlag: 1,
                                feedbackCount: 1,
                                registeredMyArticleCount: 3,
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
                                updatedAt: "00:22:22",
                                stateFlag: 2,
                                feedbackCount: 2,
                                registeredMyArticleCount: 2,
                                qiitaRecommendPoint: 2,
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
                                updatedAt: "00:03:33",
                                stateFlag: 1,
                                feedbackCount: 2,
                                registeredMyArticleCount: 1,
                                qiitaRecommendPoint: 3,
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
                                updatedAt: "11:11:11",
                                stateFlag: 1,
                                feedbackCount: 1,
                                registeredMyArticleCount: 3,
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
                                articleId: 5,
                                title: 'title5',
                                createdAt: "00:00:05",
                                updatedAt: "00:22:22",
                                stateFlag: 2,
                                feedbackCount: 2,
                                registeredMyArticleCount: 2,
                                qiitaRecommendPoint: 2,
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
                                articleId: 6,
                                title: 'title6',
                                createdAt: "00:00:06",
                                updatedAt: "00:03:33",
                                stateFlag: 1,
                                feedbackCount: 2,
                                registeredMyArticleCount: 1,
                                qiitaRecommendPoint: 3,
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
                await wrapper.setData({sortNum: 0});
                await wrapper.setData({paging: {now: 2, pageSize: 5}});
                await expect(wrapper.vm.sortedArticles[0].articleId).toBe(1);
                await expect(wrapper.find(sel('article-card0')).props().article.articleId).toBe(1)
            })
        })

        //lengthのテストは同様の内容をTesting exist elements内のv-pagination lengthで行ったので省略

        describe('apiToken', () => {
            test('apiToken', async () => {
                await expect(auth_getters.apiToken).toHaveBeenCalled();
                await expect(wrapper.vm.apiToken).toBe('token');
            })
        })
    })
    describe('Testing watch', () => {
        test('userDetail', async () => {

            /*const mockPalette=require('google-palette')
            const palette=require('google-palette/palette').palette().mockReturnValue('aaa')*/
            //const paletteSpy = jest.spyOn(, 'default').mockReturnValueOnce('41ab5d').mockReturnValueOnce('78c679').mockReturnValueOnce('addd8e')

            //const paletteMock=jest.spyOn(palette,"palette")
            //paletteMock.mockReturnValue('a')

            /*const palette=jest.fn()
            palette.mockReturnValue('aaa')
            console.log(palette('cb-YlGn',3))*/
            //('cb-YlGn',3)
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
                localVue,
                data() {
                    //storeを変更したいがwrap後は検知できないので、storeを新たに上書きしwrapするしかないが
                    // それではwatchのテストとして成立しないのでstoreの役割をここのdataとcomputedに持たせている
                    return {
                        userDetailInStore: {
                            userId: 0,
                            displayName: "",
                            photoUrl: "",
                            usedTags: [],
                            feedbackCount: 0,
                            qiitaRecommendedAllCount: 0,
                            postedArticleCount: 0,//Qiita投稿済み記事数(stateFlag=2)
                            isLoginUser: false,
                        }
                    }
                },
                computed: {
                    userDetail: {
                        get() {
                            return this.userDetailInStore
                        },
                        set(val) {
                            this.userDetailInStore = val
                        }
                    }
                },
            });

            console.log(palette)

            expect(wrapper.vm.chartDatasets).toStrictEqual({
                labels: [],
                datasets: [
                    {
                        data: [],
                        backgroundColor: [],
                    },
                ]
            })
            const changeUserDetail = function () {
                //上記の仮storeのおかげでここでwrapしなおさず変更できる
                wrapper.vm.userDetail = {
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
                            usedTagCount: 2
                        },
                        {
                            tagId: 3,
                            tagName: 'Javascript',
                            usedTagCount: 3
                        }
                    ],
                    feedbackCount: 3,
                    qiitaRecommendedAllCount: 2,
                    postedArticleCount: 0,
                    isLoginUser: true,
                }
            }
            await changeUserDetail();
           /* const chartDatasetsForTest = {
                labels: ['Java', 'go', 'Javascript'],
                datasets: [
                    {
                        data: [1, 2, 3],
                        backgroundColor: ['#41ab5d', '#78c679', '#addd8e'],
                    },
                ]
            }*/
            //await wrapper.vm.$nextTick();
            console.log('expect 3 color: '+wrapper.vm.chartDatasets.datasets.backgroundColor)
            //await expect(wrapper.vm.chartDatasets).toStrictEqual(chartDatasetsForTest)
            //await expect(user_actions.setChartData).toBeCalled()
        })

    })
})