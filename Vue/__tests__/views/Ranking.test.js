import {shallowMount, createLocalVue} from '@vue/test-utils';
import Vuex from 'vuex';
import Component from '@/views/Ranking';
import {afterEach, beforeEach, describe, jest} from "@jest/globals";

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

    describe('Testing exist element', () => {
        test('v-select', () => {
            expect(wrapper.findComponent({name: 'v-select'}).exists()).toBeTruthy();
            expect(wrapper.findAllComponents({name: 'v-select'}).length).toBe(2);

            //ランキング項目
            const rankItemSelectWrapper = wrapper.find('[data-test-id="selectRankItem"]');
            expect(rankItemSelectWrapper.props().label).toBe('ランキング項目');
            expect(rankItemSelectWrapper.props().items[0].item).toBe('FBした数');
            expect(rankItemSelectWrapper.props().items[0].id).toBe(1);
            expect(rankItemSelectWrapper.props().items[1].item).toBe('記事投稿数');
            expect(rankItemSelectWrapper.props().items[1].id).toBe(2);
            expect(rankItemSelectWrapper.props().items[2].item).toBe('Qiita推薦累計数');
            expect(rankItemSelectWrapper.props().items[2].id).toBe(3);

            expect(rankItemSelectWrapper.props().value).toBe(wrapper.vm.selectRankItemId);

            //表示件数
            const displayCountSelectWrapper = wrapper.find('[data-test-id="displayCount"]');
            expect(displayCountSelectWrapper.props().label).toBe('表示件数');
            expect(displayCountSelectWrapper.props().items[0].text).toBe('10件');
            expect(displayCountSelectWrapper.props().items[0].value).toBe(10);
            expect(displayCountSelectWrapper.props().items[1].text).toBe('20件');
            expect(displayCountSelectWrapper.props().items[1].value).toBe(20);
            expect(displayCountSelectWrapper.props().items[2].text).toBe('30件');
            expect(displayCountSelectWrapper.props().items[2].value).toBe(30);

            expect(displayCountSelectWrapper.props().value).toBe(wrapper.vm.selectDisplayCount);
        })

        test('v-progress-linear', async () => {
            //visible
            await wrapper.setData({isLoading: true})
            await expect(wrapper.findAllComponents({name: 'v-progress-linear'}).isVisible()).toBeTruthy();

            //invisible
            await wrapper.setData({isLoading: false})
            await expect(wrapper.findAllComponents({name: 'v-progress-linear'}).isVisible()).toBeFalsy();

            await expect(wrapper.findAllComponents({name: 'v-progress-linear'}).exists()).toBeTruthy();
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

    describe('Testing v-select/v-model', () => {
        test('rankItem', async () => {
            await expect(wrapper.find('[data-test-id="selectRankItem"]').props().value).toBe(wrapper.vm.selectRankItemId);

            //VueInstance.dataを変える
            let changeValue = 2;
            await wrapper.setData({
                selectRankItemId: changeValue
            })
            await expect(wrapper.find('[data-test-id="selectRankItem"]').props().value).toBe(changeValue);

            //v-select.valueを変える
            changeValue = 3;

            //子コンポーネントはスタブ化するとv-onイベントが発火しなくなるため、$emitで疑似v-modelを行う。
            await wrapper.find('[data-test-id="selectRankItem"]').vm.$emit('input', changeValue)

            await wrapper.vm.$nextTick(() => {
                expect(wrapper.vm.selectRankItemId).toBe(changeValue);
            });
        })

        test('displayCount', async () => {
            await expect(wrapper.find('[data-test-id="displayCount"]').props().value).toBe(wrapper.vm.selectDisplayCount);

            //VueInstance.dataを変える
            let changeValue = 20;
            await wrapper.setData({
                selectDisplayCount: changeValue
            })
            await expect(wrapper.find('[data-test-id="displayCount"]').props().value).toBe(changeValue);

            //v-select.valueを変える
            changeValue = 30;

            //子コンポーネントはスタブ化するとv-onイベントが発火しなくなるため、$emitで疑似v-modelを行う。
            await wrapper.find('[data-test-id="displayCount"]').vm.$emit('input', changeValue)

            await wrapper.vm.$nextTick(() => {
                expect(wrapper.vm.selectDisplayCount).toBe(changeValue);
            });
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
            await expect(users_actions.fetchRankingUser).not.toBeCalled();
            await expect(wrapper.vm.rankUsersLength).toBeNull();

            // 監視対象の変更

            const func = function () {
                wrapper.vm.apiToken = 'test';
            } //computedのset()が実行される

            await func();

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

    describe('Testing methods property', () => {
        test('toUserDetail method', async () => {
            //事前準備
            const routerSpy = {
                push: jest.fn()
            };
            wrapper = shallowMount(Component, {
                store,
                localVue,
                mocks: {
                    $router: routerSpy
                }
            });

            // 実行
            await wrapper.vm.toUserDetail(1)
            await expect(routerSpy.push).toBeCalled();
            await expect(routerSpy.push).toBeCalledWith({"name": "userDetail", "params": {"userId": 2}});

            await wrapper.vm.toUserDetail(2)
            await expect(routerSpy.push).toBeCalled();
            await expect(routerSpy.push).toBeCalledWith({"name": "userDetail", "params": {"userId": 3}});
        })
    })

    describe('Testing props to child-component', () => {
        test('UserList', () => {
            expect(wrapper.findComponent({name: 'UserList'}).vm.$props.rankUsers).toEqual([{"userId": 1}, {"userId": 2}, {"userId": 3}]);
            expect(wrapper.findComponent({name: 'UserList'}).vm.$props.displayCount).toEqual(10);
            expect(wrapper.findComponent({name: 'UserList'}).vm.$props.rankItemId).toEqual(1);
        })

        test('ChartArea', () => {
            expect(wrapper.findComponent({name: 'ChartArea'}).vm.$props.selectRankItemId).toEqual(1);
            expect(wrapper.findComponent({name: 'ChartArea'}).vm.$props.rankUsers).toEqual([{"userId": 1}, {"userId": 2}, {"userId": 3}]);
        })

        test('RelationArticles', () => {
            expect(wrapper.findComponent({name: 'RelationArticles'}).vm.$props.relArticles).toEqual([{"articleId": 1}, {"articleId": 2}, {"articleId": 3}]);
        })
    })
})

