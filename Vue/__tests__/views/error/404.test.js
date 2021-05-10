import {shallowMount, createLocalVue} from '@vue/test-utils';
import Component from '@/views/error/404';
import VueRouter from "vue-router";
import {afterEach, beforeEach, describe} from "@jest/globals";

const localVue = createLocalVue();
localVue.use(VueRouter);

const router = new VueRouter();
let wrapper;

beforeEach(() => {
    wrapper = shallowMount(Component, {
        localVue,
        router
    });
})

afterEach(() => {
    wrapper.destroy();
})
describe('Testing App component', () => {

    test('is a Vue instance', () => {
        expect(wrapper.isVueInstance).toBeTruthy();
    })

    describe('Testing element exist', () => {
        test('h1', () => {
            expect(wrapper.find('h1').isVisible()).toBeTruthy();
            expect(wrapper.find('h1').text()).toBe('404 Not Found');
        })

        test('p', () => {
            expect(wrapper.findAll('p').isVisible()).toBeTruthy();
            expect(wrapper.findAll('p').length).toBe(2);
        })

        test('router-link', () => {
            expect(wrapper.findComponent({name: 'router-link'}).isVisible()).toBeTruthy();
            expect(wrapper.findComponent({name: 'router-link'}).props().to).toStrictEqual({name: 'articleList'});
            expect(wrapper.findComponent({name: 'router-link'}).text()).toBe('QiitaBuilderトップページへ');
        })
    })
})
