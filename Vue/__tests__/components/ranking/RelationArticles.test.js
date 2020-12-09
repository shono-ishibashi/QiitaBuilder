import {shallowMount, createLocalVue} from '@vue/test-utils';
import Component from '@/components/ranking/RelationArticles';
import {afterEach, beforeEach, describe} from "@jest/globals";

const localVue = createLocalVue();

let wrapper;
let relArticles = [];

beforeEach(() => {
    relArticles = [];
    for (let i = 1; i <= 3; i++) {
        relArticles.push({
            articleId: i
        })
    }

    wrapper = shallowMount(Component, {
        localVue,
        propsData: {
            relArticles: relArticles
        }
    })
})

afterEach(() => {
    wrapper.destroy();
})

describe('Testing RalationArticles component', () => {
    test('is a Vue instance', () => {
        expect(wrapper.isVueInstance).toBeTruthy();
    })

    describe('Testing props to child-component', () => {
        test('ArticleCard', () => {
            expect(wrapper.findAllComponents({name: 'ArticleCard'}).length).toBe(wrapper.vm.relArticles.length);

            expect(wrapper.find('[data-test-id="articleCard0"]').props().index).toBe(0);
            expect(wrapper.find('[data-test-id="articleCard0"]').props().article).toBe(wrapper.vm.relArticles[0]);

            expect(wrapper.find('[data-test-id="articleCard1"]').props().index).toBe(1);
            expect(wrapper.find('[data-test-id="articleCard1"]').props().article).toBe(wrapper.vm.relArticles[1]);

            expect(wrapper.find('[data-test-id="articleCard2"]').props().index).toBe(2);
            expect(wrapper.find('[data-test-id="articleCard2"]').props().article).toBe(wrapper.vm.relArticles[2]);
        })
    })

    test('non-exist relArticles', async () => {
        relArticles = [];
        wrapper = await shallowMount(Component, {
            localVue,
            propsData: {
                relArticles: relArticles
            }
        })

        await expect(wrapper.findAllComponents({name: 'ArticleCard'}).exists()).toBeFalsy();
    })
})

