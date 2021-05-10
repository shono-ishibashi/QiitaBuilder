import {shallowMount, createLocalVue} from '@vue/test-utils';
import Component from '@/components/ranking/UserCard';
import {afterEach, beforeEach, describe, jest} from "@jest/globals";

const localVue = createLocalVue();

let wrapper;
let rankUser;
let rankItemId;
let rankNum;
let routerSpy;

beforeEach(() => {
    rankUser = {
        userId: 1,
        photoUrl: 'user-photo',
        displayName: 'user',
        feedbackCount: 3,
        postedArticleCount: 5,
        qiitaRecommendedAllCount: 2
    }
    rankItemId = 1;
    rankNum = 1;
    routerSpy = {
        push: jest.fn()
    };
    wrapper = shallowMount(Component, {
        localVue,
        propsData: {
            rankUser: rankUser,
            rankItemId: rankItemId,
            rankNum: rankNum
        },
        mocks: {
            $router: routerSpy
        }
    });
})

afterEach(() => {
    wrapper.destroy();
})

describe('Testing UserCard component', () => {

    test('is a Vue instance', () => {
        expect(wrapper.isVueInstance).toBeTruthy();
    })

    describe('Testing exist element', () => {
        test('v-icon/rankNum : 1', () => {
            expect(wrapper.find('v-icon-stub').exists()).toBeTruthy();
            expect(wrapper.findAll('v-icon-stub').length).toBe(1);
            expect(wrapper.find('v-icon-stub').isVisible()).toBeTruthy();
            expect(wrapper.find('v-icon-stub').attributes().color).toBe('#FFCC00');
            expect(wrapper.find('v-icon-stub').text()).toBe('mdi-trophy-variant');
        })

        test('v-icon/rankNum : 2', async () => {
            await wrapper.setProps({
                rankNum: 2
            })
            await expect(wrapper.find('v-icon-stub').exists()).toBeTruthy();
            await expect(wrapper.findAll('v-icon-stub').length).toBe(1);
            await expect(wrapper.find('v-icon-stub').isVisible()).toBeTruthy();
            await expect(wrapper.find('v-icon-stub').attributes().color).toBe('#AAAAAA');
            await expect(wrapper.find('v-icon-stub').text()).toBe('mdi-trophy-variant');
        })

        test('v-icon/rankNum : 3', async () => {
            await wrapper.setProps({
                rankNum: 3
            })
            await expect(wrapper.find('v-icon-stub').exists()).toBeTruthy();
            await expect(wrapper.findAll('v-icon-stub').length).toBe(1);
            await expect(wrapper.find('v-icon-stub').isVisible()).toBeTruthy();
            await expect(wrapper.find('v-icon-stub').attributes().color).toBe('#996600');
            await expect(wrapper.find('v-icon-stub').text()).toBe('mdi-trophy-variant');
        })

        test('v-icon/rankNum : 4', async () => {
            await wrapper.setProps({
                rankNum: 4
            })
            await expect(wrapper.find('v-icon-stub').exists()).toBeFalsy();
            await expect(wrapper.find('v-col-stub').text()).toBe('4')
        })

        test('v-list-item', () => {
            expect(wrapper.findComponent({name: 'v-list-item'}).exists()).toBeTruthy();
            expect(wrapper.findComponent({name: 'v-list-item'}).isVisible()).toBeTruthy()
        })

        test('v-list-item-avatar', async () => {
            expect(wrapper.findComponent({name: 'v-list-item-avatar'}).exists()).toBeTruthy();
            expect(wrapper.findComponent({name: 'v-list-item-avatar'}).isVisible()).toBeTruthy();

            //v-avatar
            expect(wrapper.findComponent({name: 'v-avatar'}).exists()).toBeTruthy();
            expect(wrapper.findComponent({name: 'v-avatar'}).isVisible()).toBeTruthy();
            expect(wrapper.findComponent({name: 'v-avatar'}).props().size).toBe('70');

            await wrapper.findComponent({name: 'v-avatar'}).vm.$emit('click');
            await expect(routerSpy.push).toBeCalledWith({
                "name": "userDetail",
                "params": {"userId": wrapper.vm.rankUser.userId}
            });

            //v-img
            expect(wrapper.findComponent({name: 'v-img'}).exists()).toBeTruthy();
            expect(wrapper.findComponent({name: 'v-img'}).isVisible()).toBeTruthy();
            expect(wrapper.findComponent({name: 'v-img'}).props().src).toBe(wrapper.vm.rankUser.photoUrl);
        })

        test('v-list-item-title', async () => {
            expect(wrapper.find('v-list-item-title-stub').exists()).toBeTruthy();
            expect(wrapper.find('v-list-item-title-stub').isVisible()).toBeTruthy();

            //v-btn
            expect(wrapper.find('v-list-item-title-stub').findComponent({name: 'v-btn'}).isVisible()).toBeTruthy();
            expect(wrapper.findComponent({name: 'v-btn'}).text()).toBe(wrapper.vm.rankUser.displayName);
            await wrapper.findComponent({name: 'v-btn'}).vm.$emit('click');
            await expect(routerSpy.push).toBeCalledWith({
                "name": "userDetail",
                "params": {"userId": wrapper.vm.rankUser.userId}
            });
        })

        test('v-list-item-subtitle', async () => {
            expect(wrapper.find('v-list-item-subtitle-stub').findAll('v-col-stub').length).toBe(3);

            let fbCountWrapper = wrapper.find('v-list-item-subtitle-stub').find('[data-test-id="fbCount"]').find('div');
            let postedArticleCountWrapper = wrapper.find('v-list-item-subtitle-stub').find('[data-test-id="postedArticleCount"]').find('div');
            let qiitaRecommendedCountWrapper = wrapper.find('v-list-item-subtitle-stub').find('[data-test-id="qiitaRecommendedCount"]').find('div');

            const updateWrapper = function () {
                fbCountWrapper = wrapper.find('v-list-item-subtitle-stub').find('[data-test-id="fbCount"]').find('div');
                postedArticleCountWrapper = wrapper.find('v-list-item-subtitle-stub').find('[data-test-id="postedArticleCount"]').find('div');
                qiitaRecommendedCountWrapper = wrapper.find('v-list-item-subtitle-stub').find('[data-test-id="qiitaRecommendedCount"]').find('div');
            }

            // FBCount
            expect(Number(fbCountWrapper.text())).toBe(wrapper.vm.rankUser.feedbackCount);
            expect(Number(postedArticleCountWrapper.text())).toBe(wrapper.vm.rankUser.postedArticleCount);
            expect(Number(qiitaRecommendedCountWrapper.text())).toBe(wrapper.vm.rankUser.qiitaRecommendedAllCount);

            expect(fbCountWrapper.classes()).toStrictEqual(['count', 'select']);
            expect(postedArticleCountWrapper.classes()).toStrictEqual(['count']);
            expect(qiitaRecommendedCountWrapper.classes()).toStrictEqual(['count']);

            // postedArticleCount
            await wrapper.setProps({
                rankItemId: 2
            })
            await updateWrapper();

            await wrapper.vm.$nextTick(() => {
                expect(fbCountWrapper.classes()).toStrictEqual(['count']);
                expect(postedArticleCountWrapper.classes()).toStrictEqual(['count', 'select']);
                expect(qiitaRecommendedCountWrapper.classes()).toStrictEqual(['count']);
            })

            // qiitaRecommendedCount
            await wrapper.setProps({
                rankItemId: 3
            })
            await updateWrapper();

            await wrapper.vm.$nextTick(() => {
                expect(fbCountWrapper.classes()).toStrictEqual(['count']);
                expect(postedArticleCountWrapper.classes()).toStrictEqual(['count']);
                expect(qiitaRecommendedCountWrapper.classes()).toStrictEqual(['count', 'select']);
            })
        })
    })

    describe('Testing methods', () => {
        test('toUserDetail', async () => {
            const userId = 2;
            await wrapper.vm.toUserDetail(userId);
            await expect(routerSpy.push).toBeCalledWith({"name": "userDetail", "params": {"userId": userId}});
        })
    })
})