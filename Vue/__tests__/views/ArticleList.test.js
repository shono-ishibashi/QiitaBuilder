import {shallowMount, createLocalVue} from '@vue/test-utils';
import Vuex from 'vuex';
import Component from '@/views/ArticleList';
import {afterEach, beforeEach, describe, jest} from "@jest/globals";

const localVue = createLocalVue();
localVue.use(Vuex);

let wrapper;
let store;
let articles_actions;
let articles_mutations;
let articles_state;
let articles_getters;
let article_actions;
let article_mutations;
let article_state;
let article_getters;
let auth_getters;
let auth_state;
let myMock = jest.fn();
const sel = id => `[data-test-id="${id}"]`

const updateMount=()=>{
    wrapper = shallowMount(Component, {
        store,
        localVue
    });
}

beforeEach(() => {

    articles_actions = {
        fetchArticles: jest.fn(),
        fetchTags: jest.fn(),
        setToggleSearchWord: jest.fn()
    };
    articles_mutations = {};
    articles_state = {
        articles: [
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
        ],
        searchCriteria: {
            sortNum: 0,
            period: null,
            searchWord: "",
            toggleSearchWord: "0",
            searchTag: [],
            pageSize: 10,
            currentPage: 1,
        },
        totalPage: undefined,
        tags: [
            {
                tagId: 1,
                tagName: 'Java'
            },
            {
                tagId: 2,
                tagName: 'go'
            },
            {
                tagId: 3,
                tagName: 'javaScript'
            },
            {
                tagId: 4,
                tagName: 'Ruby'
            },
            {
                tagId: 5,
                tagName: 'php'
            },
        ],
    };
    articles_getters = {
        tagNameList() {
            return [
                'Java',
                'go',
                'javaScript',
                'Ruby',
                'php'
            ]
        }
    };
    article_actions = {
        toggleProcessFailure: jest.fn(),
    };
    article_mutations = {};
    article_state = {
        processFailure: false,
    };
    article_getters = {};


    auth_state = {
        apiToken: 'token'
    };
    myMock.mockReturnValueOnce(auth_state.apiToken);
    auth_getters = {
        apiToken: myMock
    };

    // rootState,rootGettersは使わない
    store = new Vuex.Store({
        state: {},
        mutations: {},
        actions: {},
        getters: {},
        modules: {
            articles: {
                // namespacedいる
                namespaced: true,
                state: articles_state,
                mutations: articles_mutations,
                actions: articles_actions,
                getters: articles_getters
            },
            article: {
                namespaced: true,
                state: article_state,
                mutations: article_mutations,
                actions: article_actions,
                getters: article_getters
            },
            auth: {
                namespaced: true,
                state: auth_state,
                getters: auth_getters
            }
        }
    });

    updateMount()
})

afterEach(() => {
    wrapper.destroy();
})

// 大きいところから徐々に小さい部分にテストを移行していく
describe('Testing ArticleList Component', () => {
    test('is a Vue instance', () => {
        expect(wrapper.isVueInstance).toBeTruthy();
    })

    describe('testing exist element', () => {

        test('v-progress-linear', async () => {
            //visible
            await wrapper.setData({isLoading: true})
            await expect(wrapper.findAllComponents({name: 'v-progress-linear'}).isVisible()).toBeTruthy();

            //invisible
            await wrapper.setData({isLoading: false})
            await expect(wrapper.findAllComponents({name: 'v-progress-linear'}).isVisible()).toBeFalsy();

            await expect(wrapper.findAllComponents({name: 'v-progress-linear'}).exists()).toBeTruthy();
            await expect(wrapper.findAllComponents({name: 'v-progress-linear'}).length).toBe(1);
        })

        test('v-radio-group/ v-radio', () => {
            expect(wrapper.findComponent({name: 'v-radio-group'}).exists()).toBeTruthy();
            expect(wrapper.findAllComponents({name: 'v-radio-group'}).length).toBe(1);

            expect(wrapper.findComponent({name: 'v-radio'}).exists()).toBeTruthy();
            expect(wrapper.findAllComponents({name: 'v-radio'}).length).toBe(2);
            // v-radio ( 記事検索時 )
            const searchArticle = wrapper.find(sel('toggle-search0'))
            expect(searchArticle.props().label).toBe('記事検索')
            expect(searchArticle.props().value).toBe('0')
            // v-radio ( ユーザー検索時 )
            const searchUser = wrapper.find(sel('toggle-search1'))
            expect(searchUser.props().label).toBe('ユーザー検索')
            expect(searchUser.props().value).toBe('1')
        })

        test('v-text-field', () => {
            // text-fieldの存在確認
            let textField = wrapper.findComponent({name: 'v-text-field'})
            expect(textField.exists()).toBeTruthy();
            expect(wrapper.findAllComponents({name: 'v-text-field'}).length).toBe(1);
            // radioボタンでtextFieldのコンポーネント変更時の挙動確認
            expect(textField.props().label).toBe('記事を検索')
            articles_state.searchCriteria.toggleSearchWord = 1
            updateMount()
            textField = wrapper.findComponent({name: 'v-text-field'})
            expect(textField.props().label).toBe('ユーザーを検索')
        })

        test('v-autocomplete', () => {
            const autocompleteField = wrapper.findComponent({name: 'v-autocomplete'})
            expect(autocompleteField.exists()).toBeTruthy();
            expect(wrapper.findAllComponents({name: 'v-autocomplete'}).length).toBe(1);
            expect(autocompleteField.props().items).toStrictEqual(articles_state.tags)
            expect(autocompleteField.props().label).toBe('タグを選択')
        })

        test('v-tabs/ v-tab', () => {
            // v-tabs
            expect(wrapper.findComponent({name: 'v-tabs'}).exists()).toBeTruthy();
            expect(wrapper.findAllComponents({name: 'v-tabs'}).length).toBe(1);
            //v-tab
            expect(wrapper.findComponent({name: 'v-tab'}).exists()).toBeTruthy();
            expect(wrapper.findAllComponents({name: 'v-tab'}).length).toBe(3);
            expect(wrapper.find(sel('tab0')).text()).toBe('全て')
            expect(wrapper.find(sel('tab1')).text()).toBe('週間')
            expect(wrapper.find(sel('tab2')).text()).toBe('月間')
        })

        test('v-select', () => {
            expect(wrapper.findComponent({name: 'v-select'}).exists()).toBeTruthy();
            expect(wrapper.findAllComponents({name: 'v-select'}).length).toBe(2);
            const sortSelect = wrapper.find(sel('sort'))
            const pageSizeSelect = wrapper.find(sel('pageSize'))
            // 並び順
            expect(sortSelect.props().items).toStrictEqual(wrapper.vm.sortList)
            expect(sortSelect.props().label).toBe('並び替え')
            // 表示件数
            expect(pageSizeSelect.props().items).toStrictEqual(wrapper.vm.displayCountList)
            expect(pageSizeSelect.props().label).toBe('表示件数')
        })

        test('v-list', async () => {
            // ArticleCard
            await expect(wrapper.findComponent({name: 'ArticleCard'}).exists()).toBeTruthy();
            await expect(wrapper.findAllComponents({name: 'ArticleCard'}).length).toBe(10);
            // visible
            await wrapper.setData({isDisplay: true})
            await expect(wrapper.findAllComponents({name: 'ArticleCard'}).isVisible()).toBeTruthy();
            // invisible
            await wrapper.setData({isDisplay: false})
            await expect(wrapper.findAllComponents({name: 'ArticleCard'}).isVisible()).toBeFalsy();

            // no-article-field
            articles_state.articles = []
            updateMount()
            const noArticleField = wrapper.find(sel('no-article-field'))
            await expect(noArticleField.exists()).toBeTruthy();
            // visible
            await wrapper.setData({isDisplay: true})
            await expect(noArticleField.isVisible()).toBeTruthy();
            // invisible
            await wrapper.setData({isDisplay: false})
            await expect(noArticleField.isVisible()).toBeFalsy();
        })

        test('v-pagination', () => {
            // 記事が一件以上存在する時
            let pagination=wrapper.findComponent({name: 'v-pagination'})
            expect(pagination.exists()).toBeTruthy();
            expect(wrapper.findAllComponents({name: 'v-pagination'}).length).toBe(1);

            // 記事が一件もなかった場合
            articles_state.articles = []
            updateMount()
            pagination=wrapper.findComponent({name: 'v-pagination'})
            expect(pagination.exists()).toBeFalsy();
        })
    })

    describe('v-model| v-radio/ v-select/ v-text-field/ v-autocomplete/ v-tab/ v-pagination', () => {

        test('radio-group/ radio', () => {
            // v-radio-group
            let toggleSearchWord = wrapper.find(sel('toggle-search'))
            expect(toggleSearchWord.props().value).toBe('0')
            // toggle-searchの切り替え
            articles_state.searchCriteria.toggleSearchWord = '1'
            updateMount()
            toggleSearchWord = wrapper.find(sel('toggle-search'))
            expect(toggleSearchWord.props().value).toBe('1')
        })

        test('text-field/ search article', () => {
            // text-fieldのvalueのtest
            articles_state.searchCriteria.searchWord = 'title'
            updateMount()
            let textField = wrapper.find(sel('search-article'))
            expect(textField.exists()).toBeTruthy();
            expect(textField.props().value).toBe('title')
            // toggle-searchの切り替え後の状態test
            articles_state.searchCriteria.toggleSearchWord = 1
            updateMount()
            textField = wrapper.find(sel('search-article'))
            expect(textField.exists()).toBeFalsy();
        })

        test('text-field/ search user', () => {
            let textField = wrapper.find(sel('search-user'))
            expect(textField.exists()).toBeFalsy();
            // text-fieldのvalueのtest
            // toggle-searchの切り替え後の状態test
            articles_state.searchCriteria.searchWord = 'user'
            articles_state.searchCriteria.toggleSearchWord = 1
            updateMount()
            textField = wrapper.find(sel('search-user'))
            expect(textField.exists()).toBeTruthy();
            expect(textField.props().value).toBe('user')
        })

        test('v-select/ sort',()=>{
            // default
            let sortSelect = wrapper.find(sel('sort'))
            expect(sortSelect.props().value).toBe(0)
            // sortNum == 1
            articles_state.searchCriteria.sortNum=1
            updateMount()
            sortSelect = wrapper.find(sel('sort'))
            expect(sortSelect.props().value).toBe(1)
            // sortNum == 2
            articles_state.searchCriteria.sortNum=2
            updateMount()
            sortSelect = wrapper.find(sel('sort'))
            expect(sortSelect.props().value).toBe(2)
            // sortNum == 3
            articles_state.searchCriteria.sortNum=3
            updateMount()
            sortSelect = wrapper.find(sel('sort'))
            expect(sortSelect.props().value).toBe(3)
        })

        test('v-select/ pageSize',()=>{
            window.scrollTo=()=>{
                return {
                    top: 0,
                    behavior: "smooth"
                }
            };
            // default
            let pageSizeDir = wrapper.find(sel('pageSize'))
            expect(pageSizeDir.props().value).toBe(10)
            // pageSize == 20
            articles_state.searchCriteria.pageSize=20
            updateMount()
            pageSizeDir = wrapper.find(sel('pageSize'))
            expect(pageSizeDir.props().value).toBe(20)
            // pageSize == 30
            articles_state.searchCriteria.pageSize=30
            updateMount()
            pageSizeDir = wrapper.find(sel('pageSize'))
            expect(pageSizeDir.props().value).toBe(30)
        })

        test('v-autocomplete',()=>{
            let autocompleteDir=wrapper.findComponent({name: 'v-autocomplete'})
            expect(autocompleteDir.props().value).toStrictEqual([])
            // push('Java')
            articles_state.searchCriteria.searchTag.push({tagOd:1,tagName:'Java'})
            updateMount()
            autocompleteDir=wrapper.findComponent({name: 'v-autocomplete'})
            expect(autocompleteDir.props().value).toStrictEqual([{tagOd:1,tagName:'Java'}])
        })

        test('v-tab',()=>{

        })

        test('v-pagination',()=>{

        })
    })

    // 子コンポーネントにデータを渡せているか
    describe('Testing props to child-component', () => {

    })
})