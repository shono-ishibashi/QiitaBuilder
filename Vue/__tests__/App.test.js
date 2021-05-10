import {shallowMount, createLocalVue} from '@vue/test-utils';
import VueRouter from "vue-router";
import Vuex from 'vuex';
import Component from '@/App';
import {afterEach, beforeEach, describe, jest} from "@jest/globals";
import Vuetify from "vuetify";

const localVue = createLocalVue();
localVue.use(VueRouter);
localVue.use(Vuex);

const router = new VueRouter();

let wrapper;
let store;
let window_getters;
let isNotFoundMock = jest.fn();
let isInternalServerErrorMock = jest.fn();
let isForbiddenMock = jest.fn();

beforeEach(() => {
    isNotFoundMock.mockReturnValue(false);
    isInternalServerErrorMock.mockReturnValue(false);
    isForbiddenMock.mockReturnValue(false);
    window_getters = {
        isNotFound: isNotFoundMock,
        isInternalServerError: isInternalServerErrorMock,
        isForbidden: isForbiddenMock
    };

    store = new Vuex.Store({
        modules: {
            window: {
                namespaced: true,
                getters: window_getters
            }
        },
    });

    const goTo = jest.fn();
    goTo.init = jest.fn();
    goTo.framework = {};
    const vuetify = new Vuetify();
    vuetify.framework.goTo = goTo;

    wrapper = shallowMount(Component, {
        localVue,
        router,
        store,
        vuetify
    })
})

afterEach(() => {
    wrapper.destroy();
})
describe('Testing App component', () => {

    test('is a Vue instance', () => {
        expect(wrapper.isVueInstance).toBeTruthy();
    })

    describe('Testing exist element', () => {
        test('Header', () => {
            expect(wrapper.findComponent({name: 'Header'}).isVisible()).toBeTruthy();
        })
        test('NotFound', async () => {
            ///// non-exist
            await expect(wrapper.findComponent({name: 'error404'}).exists()).toBeFalsy();

            ///// exist
            //事前準備
            window_getters.isNotFound = jest.fn().mockReturnValue(true);
            store = new Vuex.Store({
                modules: {
                    window: {
                        namespaced: true,
                        getters: window_getters
                    }
                },
            });
            wrapper = await shallowMount(Component, {
                localVue,
                router,
                store
            })

            await expect(wrapper.findComponent({name: 'error404'}).exists()).toBeTruthy();
            await expect(wrapper.findComponent({name: 'error404'}).isVisible()).toBeTruthy();
        })

        test('Forbidden', async () => {
            ///// non-exist
            await expect(wrapper.findComponent({name: 'error403'}).exists()).toBeFalsy();

            ///// exist
            //事前準備
            window_getters.isForbidden = jest.fn().mockReturnValue(true);
            store = new Vuex.Store({
                modules: {
                    window: {
                        namespaced: true,
                        getters: window_getters
                    }
                },
            });
            wrapper = await shallowMount(Component, {
                localVue,
                router,
                store
            })

            await expect(wrapper.findComponent({name: 'error403'}).exists()).toBeTruthy();
            await expect(wrapper.findComponent({name: 'error403'}).isVisible()).toBeTruthy();
        })

        test('InternalServerError', async () => {
            ///// non-exist
            await expect(wrapper.findComponent({name: 'error500'}).exists()).toBeFalsy();

            ///// exist
            //事前準備
            window_getters.isInternalServerError = jest.fn().mockReturnValue(true);
            store = new Vuex.Store({
                modules: {
                    window: {
                        namespaced: true,
                        getters: window_getters
                    }
                },
            });
            wrapper = await shallowMount(Component, {
                localVue,
                router,
                store
            })

            await expect(wrapper.findComponent({name: 'error500'}).exists()).toBeTruthy();
            await expect(wrapper.findComponent({name: 'error500'}).isVisible()).toBeTruthy();
        })

        test('Footer', () => {
            expect(wrapper.findComponent({name: 'Footer'}).isVisible()).toBeTruthy();
        })

        test('transition', () => {
            expect(wrapper.findComponent({name: 'transition'}).exists()).toBeTruthy();
            expect(wrapper.findComponent({name: 'transition'}).findComponent({name: 'v-btn'}).exists()).toBeTruthy();
        })

        test('v-btn', async () => {
            // invisible
            await expect(wrapper.vm.fab).toBeFalsy();
            await expect(wrapper.findComponent({name: 'v-btn'}).isVisible()).toBeFalsy();

            // visible
            await wrapper.setData({
                fab: true
            })
            await expect(wrapper.findComponent({name: 'v-btn'}).isVisible()).toBeTruthy();

            await expect(wrapper.findComponent({name: 'v-btn'}).find('v-icon-stub').exists()).toBeTruthy();
        })
    })

    describe('Testing element property', () => {

        test('transition', () => {
            expect(wrapper.findComponent({name: 'transition'}).props().name).toBe('fade');
        })

        test('v-btn', async () => {
            await expect(wrapper.findComponent({name: 'v-btn'}).props().color).toBe('#008b8b');

            await wrapper.findComponent({name: 'v-btn'}).vm.$emit('click');
            await expect(wrapper.vm.$vuetify.goTo).toBeCalledWith(0);
        })

    })

    describe('Testing computed', () => {
        test('forbidden', async () => {
            await expect(window_getters.isForbidden).toHaveBeenCalled();
            await expect(wrapper.vm.forbidden).toBe(false);
        })

        test('notFound', async () => {
            await expect(window_getters.isNotFound).toHaveBeenCalled();
            await expect(wrapper.vm.notFound).toBe(false);
        })

        test('internalServerError', async () => {
            await expect(window_getters.isInternalServerError).toHaveBeenCalled();
            await expect(wrapper.vm.internalServerError).toBe(false);
        })
    })

    describe('Testing methods', () => {

        test('onScroll/pageYOffset', async () => {
            await expect(wrapper.vm.fab).toBeFalsy();
            window.pageYOffset = 600;
            const e = {
                target: {
                    scrollTop: 600
                }
            }
            await wrapper.vm.onScroll(e);
            await expect(wrapper.vm.fab).toBeTruthy();
        })

        test('onScroll/e', async () => {
            await expect(wrapper.vm.fab).toBeFalsy();
            const e = {
                target: {
                    scrollTop: 600
                }
            }
            await wrapper.vm.onScroll(e);
            await expect(wrapper.vm.fab).toBeTruthy();
        })

        test('toTop', async () => {
            await wrapper.vm.toTop();
            await expect(wrapper.vm.$vuetify.goTo).toBeCalledWith(0);
        })
    })
})
