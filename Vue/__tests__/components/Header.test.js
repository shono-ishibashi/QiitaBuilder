import {shallowMount, createLocalVue} from "@vue/test-utils";
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
})