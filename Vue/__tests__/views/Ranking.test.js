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


describe('Testing App component', () => {

    it('is a Vue instance', () => {
        expect(wrapper.isVueInstance).toBeTruthy();
    })
})
