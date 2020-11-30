import {shallowMount, createLocalVue} from '@vue/test-utils';
import Vuex from 'vuex';
import Component from '@/views/Ranking';

const localVue = createLocalVue();
localVue.use(Vuex);

let wrapper;
let store;
let actions;
let mutations;
let state;
let getters;

beforeEach(() => {

    actions = {
        fetchRankingUser: jest.fn()
    };
    mutations = {};
    state = {
        rankingUsers: []
    };
    getters = {
        users(state) {
            return state.rankingUsers;
        },
        relationArticles(state) {
            return state.rankingUsers;
        }
    };

    store = new Vuex.Store({
        modules: {
            users: {
                namespaced: true,
                state,
                mutations,
                actions,
                getters
            },
            auth: {
                state: {
                    apiToken: 'token'
                },
                getters: {
                    apiToken: jest.fn()
                }
            }
        }
    })

    wrapper = shallowMount(Component, {
        store,
        localVue
    })
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
        await wrapper.setData({isLoading : true})
        await expect(wrapper.findAllComponents({name: 'v-progress-linear'}).isVisible()).toBeTruthy();

        //invisible
        await wrapper.setData({isLoading : false})
        await expect(wrapper.findAllComponents({name: 'v-progress-linear'}).isVisible()).toBeFalsy();

        await expect(wrapper.findComponent({name: 'v-progress-linear'}).exists()).toBeTruthy();
        await expect(wrapper.findAllComponents({name: 'v-progress-linear'}).length).toBe(3);
    })

    test('UserList', async () => {
        //visible
        await wrapper.setData({isDisplay : true})
        await expect(wrapper.findAllComponents({name: 'UserList'}).isVisible()).toBeTruthy();

        //invisible
        await wrapper.setData({isDisplay : false})
        await expect(wrapper.findAllComponents({name: 'UserList'}).isVisible()).toBeFalsy();

        await expect(wrapper.findComponent({name: 'UserList'}).exists()).toBeTruthy();
        await expect(wrapper.findAllComponents({name: 'UserList'}).length).toBe(1);
    })

    test('ChartArea', async () => {
        //visible
        await wrapper.setData({isDisplay : true})
        await expect(wrapper.findAllComponents({name: 'ChartArea'}).isVisible()).toBeTruthy();

        //invisible
        await wrapper.setData({isDisplay : false})
        await expect(wrapper.findAllComponents({name: 'ChartArea'}).isVisible()).toBeFalsy();

        await expect(wrapper.findComponent({name: 'ChartArea'}).exists()).toBeTruthy();
        await expect(wrapper.findAllComponents({name: 'ChartArea'}).length).toBe(1);
    })

    test('RelationArticles', async () => {
        //visible
        await wrapper.setData({isDisplay : true})
        await expect(wrapper.findAllComponents({name: 'RelationArticles'}).isVisible()).toBeTruthy();

        //invisible
        await wrapper.setData({isDisplay : false})
        await expect(wrapper.findAllComponents({name: 'RelationArticles'}).isVisible()).toBeFalsy();

        await expect(wrapper.findComponent({name: 'RelationArticles'}).exists()).toBeTruthy();
        await expect(wrapper.findAllComponents({name: 'RelationArticles'}).length).toBe(1);
    })

    test('v-alert/exist', async () => {
        await wrapper.setData({rankUsersLength: 0})
        await expect(wrapper.findComponent({name: 'v-alert'}).exists()).toBeTruthy();
    })

    test('v-alert/non-exist', () => {
        expect(wrapper.findComponent({name: 'v-alert'}).exists()).toBeFalsy();
    })
})
