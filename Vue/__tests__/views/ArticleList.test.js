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

beforeEach(() => {

    articles_actions = {
        fetchArticles: jest.fn(),
        fetchTags: jest.fn()
    };
    articles_mutations = {};
    articles_state = {
        articles: [],
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
        tags: [],
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

    // 仮想mountをしてくれる
    wrapper = shallowMount(Component, {
        store,
        localVue
    });
})

afterEach(() => {
    wrapper.destroy();
})

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
    })
})