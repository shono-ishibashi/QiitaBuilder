import {shallowMount, createLocalVue} from '@vue/test-utils';
import Vuex from 'vuex';
import Component from '@/components/user_detail/UserInfo';
import QiitaAPI from "@/mixins/QiitaAPI";
import {afterEach, beforeEach, describe, jest} from "@jest/globals";
import Vuetify from 'vuetify'
import Pie from "@/components/user_detail/Pie";

const localVue = createLocalVue();
localVue.use(Vuex, Vuetify);
localVue.mixin(QiitaAPI)

let wrapper;
let store;
let user_actions;
let user_mutations;
let user_state;
let user_getters;
const sel = id => `[data-test-id="${id}"]`//sel('id')の形でidにtemplate内に付与されたdata-test属性の名前を指定したら付与された要素を指定できる

beforeEach(() => {
    //jest.resetModules()
    user_actions = {};
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
        notDraftArticles() {
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

describe('Testing UserInfo Component', () => {

    test('is a Vue instance', () => {
        expect(wrapper.isVueInstance).toBeTruthy();
    })
    describe('Testing exist element', () => {
        test('Pie Component', () => {
            //visible
            expect(wrapper.findComponent(Pie).exists()).toBeTruthy();

            //invisible
            user_state.userDetail.usedTags = []
            wrapper = shallowMount(Component, {
                store,
                localVue
            });
            expect(wrapper.findComponent(Pie).exists()).toBeFalsy();
        })
        test('qiita-button', () => {
            //login userのマイページもしくはユーザー詳細画面
            expect(wrapper.find(sel('qiita-button')).exists()).toBeTruthy()

            //login user以外のユーザー詳細画面
            user_state.userDetail.isLoginUser = false;
            wrapper = shallowMount(Component, {
                store,
                localVue
            });
            expect(wrapper.find(sel('qiita-button')).exists()).toBeFalsy()
        })
    })
    describe('Testing watch', () => {
        test('userDetail', async () => {
            //google-paletteのテスト方法
            //'41ab5d''78c679''addd8e'
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
            await console.log('expect 3 color: ' + wrapper.vm.chartDatasets.datasets.backgroundColor)
            //await expect(wrapper.vm.chartDatasets).toStrictEqual(chartDatasetsForTest)
            //await expect(user_actions.setChartData).toBeCalled()
        })

    })
})