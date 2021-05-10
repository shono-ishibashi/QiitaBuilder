import {shallowMount, createLocalVue, mount} from "@vue/test-utils";
import Component from '@/components/Header';
import Vuex from 'vuex';
import {afterEach, beforeEach, describe, jest} from "@jest/globals";

const localVue = createLocalVue();
localVue.use(Vuex);

let wrapper;
let store;
let auth_getters;
let auth_actions;
let article_actions;
let routerSpy;
let routeSpy;
let myMixin;

beforeEach(() => {
    auth_actions = {
        logout: jest.fn()
    };
    auth_getters = {
        loginUser: jest.fn().mockReturnValue('user')
    };
    article_actions = {
        resetArticle: jest.fn()
    };

    store = new Vuex.Store({
        modules: {
            auth: {
                namespaced: true,
                actions: auth_actions,
                getters: auth_getters
            },
            article: {
                namespaced: true,
                actions: article_actions
            }
        }
    })

    routerSpy = {
        push: jest.fn()
    };
    routeSpy = {
        path: '/path'
    };

    myMixin = {
        methods: {
            toQiitaAPIAuthentication: jest.fn()
        }
    };

    wrapper = shallowMount(Component, {
        localVue,
        store,
        mixins: [myMixin],
        mocks: {
            $router: routerSpy,
            $route: routeSpy
        }
    })
})

afterEach(() => {
    wrapper.destroy();
})

describe('Testing Header component', () => {
    test('is a Vue instance', () => {
        expect(wrapper.isVueInstance).toBeTruthy();
    })

    describe('Testing exist element', () => {
        test('v-app-bar', () => {
            const appBarWrapper = wrapper.findComponent({name: 'v-app-bar'});
            expect(appBarWrapper.isVisible()).toBeTruthy();
            expect(appBarWrapper.props().color).toBe('#5bc8ac');
            expect(appBarWrapper.props().height).toBe('70px');
            expect(appBarWrapper.classes()).toStrictEqual(['app-bar']);
        })

        test('v-toolbar-title', async () => {
            wrapper = mount(Component, {
                localVue,
                store,
                mixins: [myMixin],
                mocks: {
                    $router: routerSpy,
                    $route: routeSpy
                }
            })
            await wrapper.find('.v-toolbar__title').trigger('click');
            await expect(routerSpy.push).toBeCalledWith({name: "articleList"}, expect.anything());
            await expect(wrapper.find('.v-toolbar__title').text()).toBe('Qiita Builder');
        })

        test('header btn', async () => {
            ////// v-if="loginUser" …　true
            expect(wrapper.findAll('.header-btn').isVisible()).toBeTruthy();
            expect(wrapper.findAll('.header-btn').length).toBe(3);

            // 記事一覧
            expect(wrapper.find('[data-test-id="toArticleList"]').isVisible()).toBeTruthy();

            // 記事投稿
            expect(wrapper.find('[data-test-id="toArticleNew"]').isVisible()).toBeTruthy();

            // ランキング
            expect(wrapper.find('[data-test-id="toRanking"]').isVisible()).toBeTruthy();

            ////// v-if="loginUser" …　false
            // *** 準備 ***
            auth_getters = {
                loginUser: jest.fn().mockReturnValue(null)
            };
            store = await new Vuex.Store({
                modules: {
                    auth: {
                        namespaced: true,
                        actions: auth_actions,
                        getters: auth_getters
                    },
                    article: {
                        namespaced: true,
                        actions: article_actions
                    }
                }
            })
            wrapper = await shallowMount(Component, {
                localVue,
                store,
                mixins: [myMixin],
                mocks: {
                    $router: routerSpy,
                    $route: routeSpy
                }
            })

            // *** 検証 ***
            await expect(wrapper.findAll('.header-btn').exists()).toBeFalsy();

            // 記事一覧
            await expect(wrapper.find('[data-test-id="toArticleList"]').exists()).toBeFalsy();

            // 記事投稿
            await expect(wrapper.find('[data-test-id="toArticleNew"]').exists()).toBeFalsy();

            // ランキング
            await expect(wrapper.find('[data-test-id="toRanking"]').exists()).toBeFalsy();
        })

        test('v-icon', () => {
            // 記事一覧
            expect(wrapper.find('[data-test-id="toArticleList"]').find('v-icon-stub').isVisible()).toBeTruthy();
            expect(wrapper.find('[data-test-id="toArticleList"]').find('v-icon-stub').text())
                .toBe('mdi-format-list-bulleted-type');

            // 記事投稿
            expect(wrapper.find('[data-test-id="toArticleNew"]').find('v-icon-stub').isVisible()).toBeTruthy();
            expect(wrapper.find('[data-test-id="toArticleNew"]').find('v-icon-stub').text())
                .toBe('mdi-square-edit-outline');

            // ランキング
            expect(wrapper.find('[data-test-id="toRanking"]').find('v-icon-stub').isVisible()).toBeTruthy();
            expect(wrapper.find('[data-test-id="toRanking"]').find('v-icon-stub').text())
                .toBe('mdi-chess-king');
        })

        test('v-menu', async () => {
            expect(wrapper.findComponent({name: 'v-menu'}).isVisible()).toBeTruthy();

            auth_getters = {
                loginUser: jest.fn().mockReturnValue(null)
            };
            store = await new Vuex.Store({
                modules: {
                    auth: {
                        namespaced: true,
                        actions: auth_actions,
                        getters: auth_getters
                    },
                    article: {
                        namespaced: true,
                        actions: article_actions
                    }
                }
            })
            wrapper = await shallowMount(Component, {
                localVue,
                store,
                mixins: [myMixin],
                mocks: {
                    $router: routerSpy,
                    $route: routeSpy
                }
            })

            await expect(wrapper.findComponent({name: 'v-menu'}).exists()).toBeFalsy();
        })

        test('v-list', () => {
            expect(wrapper.findComponent({name: 'v-list'}).isVisible()).toBeTruthy();
            expect(wrapper.findComponent({name: 'v-list'}).findAllComponents({name: 'v-list-item'}).isVisible()).toBeTruthy();
            expect(wrapper.findComponent({name: 'v-list'}).findAllComponents({name: 'v-list-item'}).length).toBe(5);
        })
    })

    describe('Testing element property', () => {
        test('header btn', async () => {
            // 記事一覧ボタン
            const toArticleListButtonWrapper = wrapper.find('[data-test-id="toArticleList"]');

            expect(toArticleListButtonWrapper.props().color).toBe('#008b8b');
            await toArticleListButtonWrapper.vm.$emit('click');
            await expect(routerSpy.push).toBeCalledWith({name: "articleList"}, expect.anything());

            // 記事投稿ボタン
            const toArticleNewButtonWrapper = wrapper.find('[data-test-id="toArticleNew"]');

            expect(toArticleNewButtonWrapper.props().color).toBe('#008b8b');
            await toArticleNewButtonWrapper.vm.$emit('click');
            await expect(article_actions.resetArticle).toBeCalled();
            await expect(routerSpy.push).toBeCalledWith({name: "articleNew"}, expect.anything());

            // ランキングボタン
            const toRankingButtonWrapper = wrapper.find('[data-test-id="toRanking"]');

            expect(toRankingButtonWrapper.props().color).toBe('#008b8b');
            await toRankingButtonWrapper.vm.$emit('click');
            await expect(routerSpy.push).toBeCalledWith({name: "ranking"}, expect.anything());
        })

        test('menu v-list', async () => {

            routerSpy = {
                push: jest.fn().mockRejectedValue('catch')
            };
            wrapper = shallowMount(Component, {
                localVue,
                store,
                mixins: [myMixin],
                mocks: {
                    $router: routerSpy,
                    $route: routeSpy
                }
            });

            // toMyPage
            await wrapper.findComponent({name: 'v-menu'}).find('[data-test-id="menuToMyPage"]').vm.$emit('click');
            await expect(routerSpy.push).toBeCalledWith("/user/0?defaultList=MyPage");

            // toMyArticles
            await wrapper.findComponent({name: 'v-menu'}).find('[data-test-id="menuToMyArticles"]').vm.$emit('click');
            await expect(routerSpy.push).toBeCalledWith('/user/0?defaultList=MyArticles');

            // toDraftArticles
            await wrapper.findComponent({name: 'v-menu'}).find('[data-test-id="menuToDraftArticles"]').vm.$emit('click');
            await expect(routerSpy.push).toBeCalledWith('/user/0?defaultList=DraftArticles');

            // toQiitaAPIAuthentication
            await wrapper.findComponent({name: 'v-menu'}).find('[data-test-id="menuToQiitaAPIAuthentication"]').vm.$emit('click');
            await expect(myMixin.methods.toQiitaAPIAuthentication).toBeCalled();

            // logout
            await wrapper.findComponent({name: 'v-menu'}).find('[data-test-id="menuLogout"]').vm.$emit('click');
            await expect(auth_actions.logout).toBeCalled();
        })
    })

    describe('Testing computed', () => {
        test('loginUser', async () => {
            expect(wrapper.vm.loginUser).toBe(auth_getters.loginUser())
        })
    })

    describe('Testing methods', () => {
        test('toArticleList', async () => {
            await wrapper.vm.toArticleList();
            await expect(routerSpy.push).toBeCalledWith({name: "articleList"}, expect.anything());
        })

        test('toArticleNew', async () => {
            await wrapper.vm.toArticleNew();
            await expect(article_actions.resetArticle).toBeCalled();
            await expect(routerSpy.push).toBeCalledWith({name: "articleNew"}, expect.anything());
        })

        test('toRanking', async () => {
            await wrapper.vm.toRanking();
            await expect(routerSpy.push).toBeCalledWith({name: "ranking"}, expect.anything());
        })

        test('toMyPage', async () => {
            routerSpy = {
                push: jest.fn().mockRejectedValue('catch')
            };
            wrapper = await shallowMount(Component, {
                localVue,
                store,
                mixins: [myMixin],
                mocks: {
                    $router: routerSpy,
                    $route: routeSpy
                }
            });
            await wrapper.vm.toMyPage();
            await expect(routerSpy.push).toBeCalledWith('/user/0?defaultList=MyPage');
        })

        test('toMyArticles', async () => {
            routerSpy = {
                push: jest.fn().mockRejectedValue('catch')
            };
            wrapper = await shallowMount(Component, {
                localVue,
                store,
                mixins: [myMixin],
                mocks: {
                    $router: routerSpy,
                    $route: routeSpy
                }
            });
            await wrapper.vm.toMyArticles();
            await expect(routerSpy.push).toBeCalledWith('/user/0?defaultList=MyArticles');
        })

        test('toDraftArticles', async () => {
            routerSpy = {
                push: jest.fn().mockRejectedValue('catch')
            };
            wrapper = await shallowMount(Component, {
                localVue,
                store,
                mixins: [myMixin],
                mocks: {
                    $router: routerSpy,
                    $route: routeSpy
                }
            });
            await wrapper.vm.toDraftArticles();
            await expect(routerSpy.push).toBeCalledWith('/user/0?defaultList=DraftArticles');
        })
    })
})