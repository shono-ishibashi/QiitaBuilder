import {shallowMount, createLocalVue} from '@vue/test-utils';
import VueRouter from "vue-router";
import Vuex from 'vuex';
import Component from '@/App';
import {afterEach, beforeEach, describe, jest} from "@jest/globals";

const localVue = createLocalVue();
localVue.use(VueRouter);
localVue.use(Vuex);

const router = new VueRouter();

let wrapper;
let store;
let isNotFoundMock = jest.fn();
let isInternalServerErrorMock = jest.fn();

beforeEach(() => {
    isNotFoundMock.mockReturnValue(false);
    isInternalServerErrorMock.mockReturnValue(false);

    store = new Vuex.Store({
        modules: {
            windows: {
                namespaced: true,
                getters: {
                    isNotFound: isNotFoundMock,
                    isInternalServerError: isInternalServerErrorMock
                }
            }
        },
    })

    wrapper = shallowMount(Component, {
        localVue,
        router,
        store
    })
})

afterEach(() => {
    wrapper.destroy();
})
describe('Testing App component', () => {

    test('is a Vue instance', () => {
        expect(wrapper.isVueInstance).toBeTruthy();
    })
})
