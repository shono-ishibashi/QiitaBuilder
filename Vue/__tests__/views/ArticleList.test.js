import {shallowMount, createLocalVue} from '@vue/test-utils';
import Vuex from 'vuex';
import Vuetify from 'vuetify'
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
let auth_actions;
let window_state;
let window_getters;
let window_mutation;
let window_actions;
let myMock = jest.fn();
const sel = id => `[data-test-id="${id}"]`

const updateMount = () => {
    wrapper = shallowMount(Component, {
        store,
        localVue
    });
}

const sleep = (msec) => {
    return new Promise(function (resolve) {
        setTimeout(function () {
            resolve()
        }, msec);
    })
}

const scrollTop = () => {
    window.scrollTo({
        top: 0,
        behavior: "smooth"
    });
}

beforeEach(() => {

    articles_actions = {
        fetchArticles: jest.fn(),
        fetchTags: jest.fn(),
        setToggleSearchWord: jest.fn()
    };
    articles_mutations = {
        resetArticles: jest.fn(),
        resetSearchCriteria: jest.fn()
    };
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
    auth_actions = {
        logout: jest.fn()
    };
    window_state = {
        notFound: false,
        forbidden: false,
        internalServerError: false,
    };
    window_getters = {};
    window_mutation = {};
    window_actions = {
        setForbidden: jest.fn(),
        setNotFound: jest.fn(),
        setInternalServerError: jest.fn(),
        clearErrors: jest.fn()
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
                getters: auth_getters,
                actions: auth_actions
            },
            window: {
                namespaced: true,
                state: window_state,
                getters: window_getters,
                mutation: window_mutation,
                actions: window_actions
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
            let pagination = wrapper.findComponent({name: 'v-pagination'})
            expect(pagination.exists()).toBeTruthy();
            expect(wrapper.findAllComponents({name: 'v-pagination'}).length).toBe(1);

            // 記事が一件もなかった場合
            articles_state.articles = []
            updateMount()
            pagination = wrapper.findComponent({name: 'v-pagination'})
            expect(pagination.exists()).toBeFalsy();
        })
    })

    describe('v-model| v-radio/ v-select/ v-text-field/ v-autocomplete/ v-pagination', () => {

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

        test('v-select/ sort', () => {
            // default
            let sortSelect = wrapper.find(sel('sort'))
            expect(sortSelect.props().value).toBe(0)
            // sortNum == 1
            articles_state.searchCriteria.sortNum = 1
            updateMount()
            sortSelect = wrapper.find(sel('sort'))
            expect(sortSelect.props().value).toBe(1)
            // sortNum == 2
            articles_state.searchCriteria.sortNum = 2
            updateMount()
            sortSelect = wrapper.find(sel('sort'))
            expect(sortSelect.props().value).toBe(2)
            // sortNum == 3
            articles_state.searchCriteria.sortNum = 3
            updateMount()
            sortSelect = wrapper.find(sel('sort'))
            expect(sortSelect.props().value).toBe(3)
        })

        test('v-select/ pageSize', () => {
            window.scrollTo = () => {
                return {
                    top: 0,
                    behavior: "smooth"
                }
            };
            // default
            let pageSizeDir = wrapper.find(sel('pageSize'))
            expect(pageSizeDir.props().value).toBe(10)
            // pageSize == 20
            articles_state.searchCriteria.pageSize = 20
            updateMount()
            pageSizeDir = wrapper.find(sel('pageSize'))
            expect(pageSizeDir.props().value).toBe(20)
            // pageSize == 30
            articles_state.searchCriteria.pageSize = 30
            updateMount()
            pageSizeDir = wrapper.find(sel('pageSize'))
            expect(pageSizeDir.props().value).toBe(30)
        })

        test('v-autocomplete', () => {
            let autocompleteDir = wrapper.findComponent({name: 'v-autocomplete'})
            expect(autocompleteDir.props().value).toStrictEqual([])
            // push('Java')
            articles_state.searchCriteria.searchTag.push({tagId: 1, tagName: 'Java'})
            updateMount()
            autocompleteDir = wrapper.findComponent({name: 'v-autocomplete'})
            expect(autocompleteDir.props().value).toStrictEqual([{tagId: 1, tagName: 'Java'}])
            // push('Ruby')
            articles_state.searchCriteria.searchTag.push({tagId: 2, tagName: 'Ruby'})
            updateMount()
            autocompleteDir = wrapper.findComponent({name: 'v-autocomplete'})
            expect(autocompleteDir.props().value).toStrictEqual([{tagId: 1, tagName: 'Java'}, {
                tagId: 2,
                tagName: 'Ruby'
            }])
        })

        test('v-pagination', () => {
            let paginationDir = wrapper.findComponent({name: 'v-pagination'})
            expect(paginationDir.props().value).toBe(1)
            // 2ページ目に遷移
            articles_state.searchCriteria.currentPage = 2
            updateMount()
            paginationDir = wrapper.findComponent({name: 'v-pagination'})
            expect(paginationDir.props().value).toBe(2)
        })
    })

    describe('Testing computed', () => {

        test('apiToken', async () => {
            await expect(auth_getters.apiToken).toHaveBeenCalled();
            await expect(wrapper.vm.apiToken).toBe('token');
        })
    })

    describe('Testing watch', () => {


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
            await expect(wrapper.vm.isLoading).toBeFalsy()
            await expect(wrapper.vm.isDisplay).toBeTruthy()
            await expect(articles_actions.fetchArticles).not.toBeCalled();
            await expect(articles_actions.fetchTags).not.toBeCalled();

            // 監視対象の変更

            const func = function () {
                wrapper.vm.apiToken = 'test';
            } //computedのset()が実行される

            await func();

            // 処理後
            await wrapper.vm.$nextTick();
            await expect(wrapper.vm.isLoading).toBeTruthy()
            await expect(wrapper.vm.isDisplay).toBeFalsy()
            await expect(articles_actions.fetchArticles).toBeCalled();
            await sleep(1000)
            await expect(articles_actions.fetchTags).toBeCalled();
            await expect(wrapper.vm.isLoading).toBeFalsy()
            await expect(wrapper.vm.isDisplay).toBeTruthy()
        })

        test('searchCriteria.sortNum', async () => {
            // 処理前
            await expect(wrapper.vm.isLoading).toBeFalsy()
            await expect(wrapper.vm.isDisplay).toBeTruthy()
            await expect(articles_actions.fetchArticles).not.toBeCalled();
            // 監視対象の変更
            await wrapper.setData({searchCriteria: {sortNum: 1}})
            // 処理後
            await expect(articles_state.searchCriteria.currentPage).toBe(1)
            await expect(wrapper.vm.isLoading).toBeTruthy()
            await expect(wrapper.vm.isDisplay).toBeFalsy()
            await expect(articles_actions.fetchArticles).toBeCalled();
            await sleep(1000)
            await expect(wrapper.vm.isLoading).toBeFalsy()
            await expect(wrapper.vm.isDisplay).toBeTruthy()
        })

        test('searchCriteria.period', async () => {
            // 処理前
            await expect(wrapper.vm.isLoading).toBeFalsy()
            await expect(wrapper.vm.isDisplay).toBeTruthy()
            await expect(articles_actions.fetchArticles).not.toBeCalled();
            // 監視対象の変更
            await wrapper.setData({searchCriteria: {period: 1}})
            // 処理後
            await expect(articles_state.searchCriteria.currentPage).toBe(1)
            await expect(wrapper.vm.isLoading).toBeTruthy()
            await expect(wrapper.vm.isDisplay).toBeFalsy()
            await expect(articles_actions.fetchArticles).toBeCalled();
            await sleep(1000)
            await expect(wrapper.vm.isLoading).toBeFalsy()
            await expect(wrapper.vm.isDisplay).toBeTruthy()
        })

        test('searchCriteria.pageSize', async () => {
            // 処理前
            await expect(articles_actions.fetchArticles).not.toBeCalled();
            // 監視対象の変更
            await wrapper.setData({searchCriteria: {pageSize: 20}})
            // 処理後
            await expect(articles_state.searchCriteria.currentPage).toBe(1)
            await expect(articles_actions.fetchArticles).toBeCalled();
            scrollTop()
        })

        test('searchCriteria.currentPage', async () => {
            // 処理前
            await expect(articles_actions.fetchArticles).not.toBeCalled();
            // 監視対象の変更
            await wrapper.setData({searchCriteria: {pageSize: 20}})
            // 処理後
            await expect(articles_state.searchCriteria.currentPage).toBe(1)
            await expect(articles_actions.fetchArticles).toBeCalled();
            await sleep(50)
            await scrollTop()
        })

    })

    describe('Testing methods property', () => {

        test('changePeriod method', async () => {
            expect(articles_state.searchCriteria.period).toBe(null)
            await wrapper.vm.changePeriod(1)
            expect(articles_state.searchCriteria.period).toBe(1)
        })

        test('enable_submit method', async () => {
            expect(wrapper.vm.can_submit_search).toBeFalsy()
            await wrapper.vm.enable_submit()
            expect(wrapper.vm.can_submit_search).toBeTruthy()
        })

        test('submit method', async () => {
        //     let vuetify
        //     beforeEach(() => {
        //         vuetify = new Vuetify()
        //     })
        //
        //     wrapper = shallowMount(Component, {
        //         store,
        //         localVue,
        //         vuetify
        //     });
        //     // await expect(wrapper.vm.can_submit_search).toBeFalsy()
        //     await wrapper.setData({can_submit_search: true})
        //     // validationを通過した時
        //     // wrapper.vm.$refs.search_form.value=true
        //     await wrapper.vm.submit()
        //     await expect(wrapper.vm.isLoading).toBeTruthy()
        //     await expect(wrapper.vm.isDisplay).toBeFalsy()
        //     await expect(articles_state.searchCriteria.currentPage).toBe(1)
        //     await expect(articles_actions.fetchArticles).toBeCalled();
        //     await sleep(1000)
        //     await expect(wrapper.vm.isLoading).toBeFalsy()
        //     await expect(wrapper.vm.isDisplay).toBeTruthy()
        //     await expect(wrapper.vm.can_submit_search).toBeTruthy()
        })

        test('clickSubmit method', async () => {
            await expect(wrapper.vm.can_submit_search).toBeFalsy()
            await wrapper.vm.clickSubmit()
            await expect(wrapper.vm.can_submit_search).toBeTruthy()
        })

        test('reset method', async () => {
            await wrapper.vm.reset()
            await expect(articles_actions.fetchArticles).toBeCalled();
            await sleep(1000)
            await expect(wrapper.vm.isLoading).toBeFalsy()
            await expect(wrapper.vm.isDisplay).toBeTruthy()
        })

        test('errorHandle method', async () => {
            // statusCode = 401 の時
            let error = {response: {status: 401}}
            await wrapper.vm.errorHandle(error)
            await expect(wrapper.vm.nonValidToken).toBeTruthy()
            await expect(auth_actions.logout).toBeCalled();
            // statusCode = 500 の時
            error = {response: {status: 500}}
            await wrapper.vm.errorHandle(error)
            await expect(window_actions.setInternalServerError).toBeCalled();
            // else
            error = {response: {status: 409}}
            await wrapper.vm.errorHandle(error)
            await expect(article_actions.toggleProcessFailure).toBeCalled();
        })

        test('articleErrorHandle method',async ()=>{
            // statusCode = 400 の時
            let error = {response: {status: 400}}
            await wrapper.vm.articleErrorHandle(error)
            await expect(window_actions.setNotFound).toBeCalled();
            // statusCode = 401 の時
            error = {response: {status: 401}}
            await wrapper.vm.articleErrorHandle(error)
            await expect(window_actions.setNotFound).toBeCalled();
            // statusCode = 403 の時
            error = {response: {status: 403}}
            await wrapper.vm.articleErrorHandle(error)
            await expect(window_actions.setForbidden).toBeCalled();
            // else
            wrapper.vm.errorHandle=jest.fn()
            error = {response: {status: 409}}
            await wrapper.vm.articleErrorHandle(error)
            await expect(wrapper.vm.errorHandle).toBeCalled();
        })
    })

    // 子コンポーネントにデータを渡せているか
    describe('Testing props to child-component', () => {

        test('ArticleCard', () => {
            expect(wrapper.findAllComponents({name: 'ArticleCard'}).length).toBe(wrapper.vm.articles.length);

            expect(wrapper.find(sel('articleCard0')).props().index).toBe(0);
            expect(wrapper.find(sel('articleCard0')).props().article).toBe(wrapper.vm.articles[0]);
            expect(wrapper.find(sel('articleCard1')).props().index).toBe(1);
            expect(wrapper.find(sel('articleCard1')).props().article).toBe(wrapper.vm.articles[1]);
            expect(wrapper.find(sel('articleCard2')).props().index).toBe(2);
            expect(wrapper.find(sel('articleCard2')).props().article).toBe(wrapper.vm.articles[2]);
            expect(wrapper.find(sel('articleCard3')).props().index).toBe(3);
            expect(wrapper.find(sel('articleCard3')).props().article).toBe(wrapper.vm.articles[3]);
            expect(wrapper.find(sel('articleCard4')).props().index).toBe(4);
            expect(wrapper.find(sel('articleCard4')).props().article).toBe(wrapper.vm.articles[4]);
            expect(wrapper.find(sel('articleCard5')).props().index).toBe(5);
            expect(wrapper.find(sel('articleCard5')).props().article).toBe(wrapper.vm.articles[5]);
            expect(wrapper.find(sel('articleCard6')).props().index).toBe(6);
            expect(wrapper.find(sel('articleCard6')).props().article).toBe(wrapper.vm.articles[6]);
            expect(wrapper.find(sel('articleCard7')).props().index).toBe(7);
            expect(wrapper.find(sel('articleCard7')).props().article).toBe(wrapper.vm.articles[7]);
            expect(wrapper.find(sel('articleCard8')).props().index).toBe(8);
            expect(wrapper.find(sel('articleCard8')).props().article).toBe(wrapper.vm.articles[8]);
            expect(wrapper.find(sel('articleCard9')).props().index).toBe(9);
            expect(wrapper.find(sel('articleCard9')).props().article).toBe(wrapper.vm.articles[9]);

        })
    })
})