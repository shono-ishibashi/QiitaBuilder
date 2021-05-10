import {shallowMount, createLocalVue} from '@vue/test-utils';
import Vuex from 'vuex';
import Component from '@/components/ArticleCard';
import {afterEach, beforeEach, describe, jest} from "@jest/globals";
import Vuetify from 'vuetify'

const localVue = createLocalVue();
localVue.use(Vuex, Vuetify);

let wrapper;
let store;
let articles_actions;
let articles_mutations;
let articles_state;
let articles_getters;
const sel = id => `[data-test-id="${id}"]`

const article = {
    articleId: 1,
    title: 'title1',
    // createdAt: Date.now(),
    // updatedAt: Date.now(),
    stateFlag: 1,
    feedbackCount: 1,
    registeredMyArticleCount: 2,
    qiitaRecommendPoint: 1,
    postedUser: {
        userId: 1,
        displayName: "user1",
        photoUrl: "photo1"
    },
    tags: {
        tagId: 1,
        tagName: 'java'
    }
}

const updateMount = () => {
    wrapper = shallowMount(Component, {
        store,
        localVue,
        propsData: {
            article: article,
            index: 0
        },
    });
}

beforeEach(() => {

    articles_actions = {
        fetchArticles: jest.fn()
    };
    articles_mutations = {};
    articles_state = {
        articles: article
        ,
        searchCriteria: {
            sortNum: 0,
            period: null,
            searchWord: "",
            toggleSearchWord: "0",
            searchTag: [],
            pageSize: 10,
            currentPage: 1,
        },
    };
    articles_getters = {};

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
        }
    });

    updateMount()
})

afterEach(() => {
    wrapper.destroy();
})

describe('Testing ArticleCard Component', () => {
    test('is a Vue instance', () => {
        expect(wrapper.isVueInstance).toBeTruthy();
    })
    describe('testing exist element',()=>{
        test('v-avatar',()=>{
           expect(wrapper.findComponent({name:'v-avatar'}).isVisible()).toBeTruthy();
        })
    })
})