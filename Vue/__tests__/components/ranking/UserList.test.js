import {shallowMount, createLocalVue} from '@vue/test-utils';
import Component from '@/components/ranking/UserList';
import {afterEach, beforeEach, describe, jest} from "@jest/globals";
import Vuetify from "vuetify";

const localVue = createLocalVue();

let wrapper;
let rankUsers;
let displayCount;
let rankItemId;

beforeEach(() => {

    rankUsers = [];
    for (let i = 1; i <= 25; i++) {
        rankUsers.push({
            userId: i,
            rank: i
        })
    }
    displayCount = 10;
    rankItemId = 1;
    const goTo = jest.fn();
    goTo.init = jest.fn();
    goTo.framework = {};
    const vuetify = new Vuetify();
    vuetify.framework.goTo = goTo;

    wrapper = shallowMount(Component, {
        localVue,
        vuetify,
        propsData: {
            rankUsers: rankUsers,
            displayCount: displayCount,
            rankItemId: rankItemId
        }
    })
})

afterEach(() => {
    wrapper.destroy();
})
describe('Testing UserList component', () => {

    test('is a Vue instance', () => {
        expect(wrapper.isVueInstance).toBeTruthy();
    })

    describe('Testing exist element', () => {
        test('v-pagination/rankUserが存在する場合', () => {
            expect(wrapper.findComponent({name: 'v-pagination'}).exists()).toBeTruthy();
            expect(wrapper.findAllComponents({name: 'v-pagination'}).length).toBe(2);
            expect(wrapper.findAllComponents({name: 'v-pagination'}).isVisible()).toBeTruthy();
        })

        test('v-pagination/rankUserが存在しない場合', async () => {

            await wrapper.setProps({
                rankUsers: []
            })
            await wrapper.vm.$nextTick();

            await expect(wrapper.findComponent({name: 'v-pagination'}).exists()).toBeTruthy();
            await expect(wrapper.findAllComponents({name: 'v-pagination'}).length).toBe(2);
            await expect(wrapper.findAllComponents({name: 'v-pagination'}).isVisible()).toBeFalsy();
        })

        test('UserCard/rankUserが存在する場合', async () => {
            expect(wrapper.findComponent({name: 'UserCard'}).exists()).toBeTruthy();
            expect(wrapper.findComponent({name: 'UserCard'}).isVisible()).toBeTruthy();

            //表示件数：10件
            await expect(wrapper.findAllComponents({name: 'UserCard'}).length).toBe(10);
            await wrapper.setData({
                currentPage: 3
            })
            await wrapper.vm.$nextTick(() => {
                expect(wrapper.findAllComponents({name: 'UserCard'}).length).toBe(5);
            })

            //表示件数：20件
            await wrapper.setProps({
                displayCount: 20
            })
            await wrapper.vm.$nextTick(() => {
                expect(wrapper.findAllComponents({name: 'UserCard'}).length).toBe(20);
            });
            await wrapper.setData({
                currentPage: 2
            })
            await wrapper.vm.$nextTick(() => {
                expect(wrapper.findAllComponents({name: 'UserCard'}).length).toBe(5);
            })

            //表示件数：30件
            await wrapper.setProps({
                displayCount: 30
            })
            await wrapper.vm.$nextTick(() => {
                expect(wrapper.findAllComponents({name: 'UserCard'}).length).toBe(25);
            });
        })

        test('UserCard/rankUserが存在しない場合', async () => {
            await wrapper.setProps({
                rankUsers: []
            })
            await wrapper.vm.$nextTick();
            await expect(wrapper.findComponent({name: 'UserCard'}).exists()).toBeFalsy();
        })
    })

    describe('Testing element props', () => {
        test('v-pagination', () => {
            const pageNationWrapper1 = wrapper.find('[data-test-id="topPagination"]');
            expect(pageNationWrapper1.props().value).toBe(wrapper.vm.currentPage);
            expect(pageNationWrapper1.props().length).toBe(wrapper.vm.totalPage);
            expect(pageNationWrapper1.props().color).toBe('#5bc8ac');

            const pageNationWrapper2 = wrapper.find('[data-test-id="bottomPagination"]');
            expect(pageNationWrapper2.props().value).toBe(wrapper.vm.currentPage);
            expect(pageNationWrapper2.props().length).toBe(wrapper.vm.totalPage);
            expect(pageNationWrapper2.props().color).toBe('#5bc8ac');
        })

        test('UserCard', () => {
            for (let i = 0; i < 10; i++) {
                const testId = 'userCard' + i;
                const userCardWrapper = wrapper.find('[data-test-id="' + testId + '"]');
                expect(userCardWrapper.classes()).toStrictEqual(['control-margin']);
                expect(userCardWrapper.props().rankUser).toStrictEqual(wrapper.vm.rankUsers[i]);
                expect(userCardWrapper.props().rankNum).toBe(wrapper.vm.rankUsers[i].rank);
                expect(userCardWrapper.props().rankItemId).toBe(wrapper.vm.rankItemId);
            }
        })
    })

    describe('Testing computed', () => {
        test('totalPage/10件表示', async () => {
            //rankUser:25件の場合
            await expect(wrapper.vm.totalPage).toBe(3);

            //rankUser:20件の場合
            rankUsers = [];
            for (let i = 1; i <= 20; i++) {
                rankUsers.push({
                    userId: i,
                    rank: i
                })
            }
            await wrapper.setProps({
                rankUsers: rankUsers
            });
            await expect(wrapper.vm.totalPage).toBe(2);

            //rankUser:5件の場合
            rankUsers = [];
            for (let i = 1; i <= 5; i++) {
                rankUsers.push({
                    userId: i,
                    rank: i
                })
            }
            await wrapper.setProps({
                rankUsers: rankUsers
            });
            await expect(wrapper.vm.totalPage).toBe(1);
        })

        test('totalPage/20件表示', async () => {
            await wrapper.setProps({
                displayCount: 20
            });
            //rankUser:25件の場合
            await expect(wrapper.vm.totalPage).toBe(2);

            //rankUser:20件の場合
            rankUsers = [];
            for (let i = 1; i <= 20; i++) {
                rankUsers.push({
                    userId: i,
                    rank: i
                })
            }
            await wrapper.setProps({
                rankUsers: rankUsers
            });
            await expect(wrapper.vm.totalPage).toBe(1);

            //rankUser:5件の場合
            rankUsers = [];
            for (let i = 1; i <= 5; i++) {
                rankUsers.push({
                    userId: i,
                    rank: i
                })
            }
            await wrapper.setProps({
                rankUsers: rankUsers
            });
            await expect(wrapper.vm.totalPage).toBe(1);
        })

        test('totalPage/30件表示', async () => {
            await wrapper.setProps({
                displayCount: 30
            });
            //rankUser:25件の場合
            await expect(wrapper.vm.totalPage).toBe(1);
        })

        test('controlledRankUsers/10件表示', async () => {
            //1ページ目
            let controlledRankUsers;
            controlledRankUsers = wrapper.vm.controlledRankUsers;
            await expect(wrapper.vm.controlledRankUsers.length).toBe(10);
            await expect(controlledRankUsers[0]).toStrictEqual(rankUsers[0]);
            await expect(controlledRankUsers[1]).toStrictEqual(rankUsers[1]);
            await expect(controlledRankUsers[2]).toStrictEqual(rankUsers[2]);
            await expect(controlledRankUsers[7]).toStrictEqual(rankUsers[7]);
            await expect(controlledRankUsers[8]).toStrictEqual(rankUsers[8]);
            await expect(controlledRankUsers[9]).toStrictEqual(rankUsers[9]);

            //2ページ目
            await wrapper.setData({currentPage: 2});
            controlledRankUsers = wrapper.vm.controlledRankUsers;
            await expect(wrapper.vm.controlledRankUsers.length).toBe(10);
            await expect(controlledRankUsers[0]).toStrictEqual(rankUsers[10]);
            await expect(controlledRankUsers[1]).toStrictEqual(rankUsers[11]);
            await expect(controlledRankUsers[2]).toStrictEqual(rankUsers[12]);
            await expect(controlledRankUsers[7]).toStrictEqual(rankUsers[17]);
            await expect(controlledRankUsers[8]).toStrictEqual(rankUsers[18]);
            await expect(controlledRankUsers[9]).toStrictEqual(rankUsers[19]);

            //3ページ目
            await wrapper.setData({currentPage: 3});
            controlledRankUsers = wrapper.vm.controlledRankUsers;
            await expect(wrapper.vm.controlledRankUsers.length).toBe(5);
            await expect(controlledRankUsers[0]).toStrictEqual(rankUsers[20]);
            await expect(controlledRankUsers[1]).toStrictEqual(rankUsers[21]);
            await expect(controlledRankUsers[2]).toStrictEqual(rankUsers[22]);
            await expect(controlledRankUsers[3]).toStrictEqual(rankUsers[23]);
            await expect(controlledRankUsers[4]).toStrictEqual(rankUsers[24]);
        })

        test('controlledRankUsers/20件表示', async () => {
            await wrapper.setProps({
                displayCount: 20
            });
            //1ページ目
            let controlledRankUsers;
            controlledRankUsers = wrapper.vm.controlledRankUsers;
            await expect(wrapper.vm.controlledRankUsers.length).toBe(20);
            await expect(controlledRankUsers[0]).toStrictEqual(rankUsers[0]);
            await expect(controlledRankUsers[1]).toStrictEqual(rankUsers[1]);
            await expect(controlledRankUsers[2]).toStrictEqual(rankUsers[2]);
            await expect(controlledRankUsers[17]).toStrictEqual(rankUsers[17]);
            await expect(controlledRankUsers[18]).toStrictEqual(rankUsers[18]);
            await expect(controlledRankUsers[19]).toStrictEqual(rankUsers[19]);

            //2ページ目
            await wrapper.setData({currentPage: 2});
            controlledRankUsers = wrapper.vm.controlledRankUsers;
            await expect(wrapper.vm.controlledRankUsers.length).toBe(5);
            await expect(controlledRankUsers[0]).toStrictEqual(rankUsers[20]);
            await expect(controlledRankUsers[1]).toStrictEqual(rankUsers[21]);
            await expect(controlledRankUsers[2]).toStrictEqual(rankUsers[22]);
            await expect(controlledRankUsers[3]).toStrictEqual(rankUsers[23]);
            await expect(controlledRankUsers[4]).toStrictEqual(rankUsers[24]);
        })

        test('controlledRankUsers/30件表示', async () => {
            await wrapper.setProps({
                displayCount: 30
            });
            //1ページ目
            let controlledRankUsers;
            controlledRankUsers = wrapper.vm.controlledRankUsers;
            await expect(wrapper.vm.controlledRankUsers.length).toBe(25);
            await expect(controlledRankUsers[0]).toStrictEqual(rankUsers[0]);
            await expect(controlledRankUsers[1]).toStrictEqual(rankUsers[1]);
            await expect(controlledRankUsers[2]).toStrictEqual(rankUsers[2]);
            await expect(controlledRankUsers[22]).toStrictEqual(rankUsers[22]);
            await expect(controlledRankUsers[23]).toStrictEqual(rankUsers[23]);
            await expect(controlledRankUsers[24]).toStrictEqual(rankUsers[24]);
        })
    })

    describe('Testing watch', () => {
        test('displayCount', async () => {
            await wrapper.setData({currentPage: 2});
            await expect(wrapper.vm.currentPage).toBe(2);
            await wrapper.setProps({displayCount: 2});
            await expect(wrapper.vm.currentPage).toBe(1);
        })

        test('currentPage', async () => {
            jest.useFakeTimers();
            await wrapper.setData({currentPage: 2});
            await jest.advanceTimersByTime(200);
            await expect(wrapper.vm.$vuetify.goTo).toBeCalledWith(0);
        })

        test('rankItemId', async () => {
            await expect(wrapper.vm.currentPage).toBe(1);
            await wrapper.setData({currentPage: 2});
            await expect(wrapper.vm.currentPage).toBe(2);
            await wrapper.setProps({rankItemId: 2});
            await expect(wrapper.vm.currentPage).toBe(1);
        })
    })
})
