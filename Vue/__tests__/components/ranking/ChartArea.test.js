import {shallowMount, createLocalVue} from '@vue/test-utils';
import Component from '@/components/ranking/ChartArea';
import {afterEach, beforeEach, describe} from "@jest/globals";

const localVue = createLocalVue();

let wrapper;
let selectRankItemId;
let rankUsers;

const rankUsersFunc = function (numberOfPeople) {
    let controlledRankUsers = [];
    for (let i = 1; i <= numberOfPeople; i++) {
        controlledRankUsers.push({
            userId: i,
            displayName: 'user' + i,
            feedbackCount: numberOfPeople - (i - 1),
            postedArticleCount: (numberOfPeople - (i - 1)) * 2,
            qiitaRecommendedAllCount: (numberOfPeople - (i - 1)) * 3,
            rank: i
        })
    }
    return controlledRankUsers;
}

beforeEach(() => {
    selectRankItemId = 1;
    rankUsers = [];

    wrapper = shallowMount(Component, {
        localVue,
        propsData: {
            selectRankItemId: selectRankItemId,
            rankUsers: rankUsers
        }
    })
})

afterEach(() => {
    wrapper.destroy();
})

describe('Testing ChartArea component', () => {

    test('is a Vue instance', () => {
        expect(wrapper.isVueInstance).toBeTruthy();
    })

    describe('Testing exist element', () => {

        test('RankingChart', async () => {
            //準備
            const controlledRankUsers = await rankUsersFunc(25);
            await wrapper.setProps({
                rankUsers: controlledRankUsers
            })

            await expect(wrapper.findComponent({name: 'RankingChart'}).exists()).toBeTruthy();
            await expect(wrapper.findComponent({name: 'RankingChart'}).isVisible()).toBeTruthy();
            await expect(wrapper.findAllComponents({name: 'RankingChart'}).length).toBe(1);
        })
    })

    describe('Testing props to child-component', () => {

        test('RankingChart', async () => {
            await expect(wrapper.findComponent({name: 'RankingChart'}).props().width).toBe('100%');
            await expect(wrapper.findComponent({name: 'RankingChart'}).props().height).toBe('60%');
            await expect(wrapper.findComponent({name: 'RankingChart'}).classes()).toStrictEqual(['ranking-chart']);
            await expect(wrapper.findComponent({name: 'RankingChart'}).props().chartData).toBe(wrapper.vm.chartData);
            await expect(wrapper.findComponent({name: 'RankingChart'}).props().options).toBe(wrapper.vm.options);
        })
    })

    describe('Testing watch', () => {

        describe('rankUsers/10ユーザー以上いる場合', () => {

            test('selectRankItemId: 1', async () => {
                //準備
                const controlledRankUsers = await rankUsersFunc(25);
                await wrapper.setProps({
                    rankUsers: controlledRankUsers
                })

                //chartData - labels
                await expect(wrapper.vm.chartData.labels).toStrictEqual([
                    'user1', 'user2',
                    'user3', 'user4',
                    'user5', 'user6',
                    'user7', 'user8',
                    'user9', 'user10'
                ]);

                //chartData - datasets
                const datasets = wrapper.vm.chartData.datasets[0];
                await expect(datasets.label).toBe('FBした数')
                await expect(datasets.data.length).toBe(10);
                await expect(datasets.data).toStrictEqual([
                    25, 24, 23, 22, 21, 20, 19, 18, 17, 16
                ])
                await expect(datasets.backgroundColor).toBe('rgba(91,200,172,0.2)');
                await expect(datasets.borderColor).toBe('rgba(91,200,172,1)');
                await expect(datasets.borderWidth).toBe(1);
            })

            test('selectRankItemId: 2', async () => {
                //準備
                const controlledRankUsers = await rankUsersFunc(25);
                await wrapper.setProps({
                    selectRankItemId: 2,
                    rankUsers: controlledRankUsers
                })

                //chartData - labels
                await expect(wrapper.vm.chartData.labels).toStrictEqual([
                    'user1', 'user2',
                    'user3', 'user4',
                    'user5', 'user6',
                    'user7', 'user8',
                    'user9', 'user10'
                ]);

                //chartData - datasets
                const datasets = wrapper.vm.chartData.datasets[0];
                await expect(datasets.label).toBe('記事投稿数')
                await expect(datasets.data.length).toBe(10);
                await expect(datasets.data).toStrictEqual([
                    50, 48, 46, 44, 42, 40, 38, 36, 34, 32
                ])
                await expect(datasets.backgroundColor).toBe('rgba(91,200,172,0.2)');
                await expect(datasets.borderColor).toBe('rgba(91,200,172,1)');
                await expect(datasets.borderWidth).toBe(1);
            })

            test('selectRankItemId: 3', async () => {
                //準備
                const controlledRankUsers = await rankUsersFunc(25);
                await wrapper.setProps({
                    selectRankItemId: 3,
                    rankUsers: controlledRankUsers
                })

                //chartData - labels
                await expect(wrapper.vm.chartData.labels).toStrictEqual([
                    'user1', 'user2',
                    'user3', 'user4',
                    'user5', 'user6',
                    'user7', 'user8',
                    'user9', 'user10'
                ]);

                //chartData - datasets
                const datasets = wrapper.vm.chartData.datasets[0];
                await expect(datasets.label).toBe('Qiita推薦累計数')
                await expect(datasets.data.length).toBe(10);
                await expect(datasets.data).toStrictEqual([
                    75, 72, 69, 66, 63, 60, 57, 54, 51, 48
                ])
                await expect(datasets.backgroundColor).toBe('rgba(91,200,172,0.2)');
                await expect(datasets.borderColor).toBe('rgba(91,200,172,1)');
                await expect(datasets.borderWidth).toBe(1);
            })
        })

        test('rankUsers/10ユーザー以上いない場合', async () => {
            //準備
            const controlledRankUsers = await rankUsersFunc(4);
            await wrapper.setProps({
                rankUsers: controlledRankUsers
            })

            //chartData - labels
            await expect(wrapper.vm.chartData.labels).toStrictEqual([
                'user1', 'user2',
                'user3', 'user4'
            ]);

            //chartData - datasets
            const datasets = wrapper.vm.chartData.datasets[0];
            await expect(datasets.label).toBe('FBした数')
            await expect(datasets.data.length).toBe(4);
            await expect(datasets.data).toStrictEqual([
                4, 3, 2, 1
            ])
            await expect(datasets.backgroundColor).toBe('rgba(91,200,172,0.2)');
            await expect(datasets.borderColor).toBe('rgba(91,200,172,1)');
            await expect(datasets.borderWidth).toBe(1);
        })

        describe('chartData', () => {

            test('countMinが5で割り切れる場合', async () => {
                //準備
                const controlledRankUsers = await rankUsersFunc(25);
                controlledRankUsers[9].feedbackCount = 15;
                await wrapper.setProps({
                    rankUsers: controlledRankUsers
                })

                const ticks = wrapper.vm.options.scales.yAxes[0].ticks;
                await expect(ticks.suggestedMax).toBe(30);
                await expect(ticks.suggestedMin).toBe(10);
                await expect(ticks.stepSize).toBe(5);
                await expect(wrapper.vm.options.responsive).toBeTruthy();
                await expect(wrapper.vm.options.onClick).toBe(wrapper.vm.handle);
            })

            test('countMinが5で割り切れない場合', async () => {
                //準備
                const controlledRankUsers = await rankUsersFunc(25);
                await wrapper.setProps({
                    rankUsers: controlledRankUsers
                })

                const ticks = wrapper.vm.options.scales.yAxes[0].ticks;
                await expect(ticks.suggestedMax).toBe(30);
                await expect(ticks.suggestedMin).toBe(15);
                await expect(ticks.stepSize).toBe(5);
                await expect(wrapper.vm.options.responsive).toBeTruthy();
                await expect(wrapper.vm.options.onClick).toBe(wrapper.vm.handle);
            })

            test('selectRankItemId: 2', async () => {
                //準備
                const controlledRankUsers = await rankUsersFunc(25);
                await wrapper.setProps({
                    selectRankItemId: 2,
                    rankUsers: controlledRankUsers
                })

                const ticks = wrapper.vm.options.scales.yAxes[0].ticks;
                await expect(ticks.suggestedMax).toBe(55);
                await expect(ticks.suggestedMin).toBe(30);
                await expect(ticks.stepSize).toBe(5);
                await expect(wrapper.vm.options.responsive).toBeTruthy();
                await expect(wrapper.vm.options.onClick).toBe(wrapper.vm.handle);
            })

            test('selectRankItemId: 3', async () => {
                //準備
                const controlledRankUsers = await rankUsersFunc(25);
                await wrapper.setProps({
                    selectRankItemId: 3,
                    rankUsers: controlledRankUsers
                })

                const ticks = wrapper.vm.options.scales.yAxes[0].ticks;
                await expect(ticks.suggestedMax).toBe(80);
                await expect(ticks.suggestedMin).toBe(45);
                await expect(ticks.stepSize).toBe(5);
                await expect(wrapper.vm.options.responsive).toBeTruthy();
                await expect(wrapper.vm.options.onClick).toBe(wrapper.vm.handle);
            })

            test('selectRankItemId: 4', async () => {
                //準備
                const controlledRankUsers = await rankUsersFunc(25);
                await wrapper.setProps({
                    selectRankItemId: 4,
                    rankUsers: controlledRankUsers
                })

                const ticks = wrapper.vm.options.scales.yAxes[0].ticks;
                await expect(ticks.suggestedMax).toBe(30);
                await expect(ticks.suggestedMin).toBe(15);
                await expect(ticks.stepSize).toBe(5);
                await expect(wrapper.vm.options.responsive).toBeTruthy();
                await expect(wrapper.vm.options.onClick).toBe(wrapper.vm.handle);
            })

            test('selectRankItemId: string', async () => {
                //準備
                const controlledRankUsers = await rankUsersFunc(25);
                await wrapper.setProps({
                    selectRankItemId: 'abc',
                    rankUsers: controlledRankUsers
                })

                const ticks = wrapper.vm.options.scales.yAxes[0].ticks;
                await expect(ticks.suggestedMax).toBe(30);
                await expect(ticks.suggestedMin).toBe(15);
                await expect(ticks.stepSize).toBe(5);
                await expect(wrapper.vm.options.responsive).toBeTruthy();
                await expect(wrapper.vm.options.onClick).toBe(wrapper.vm.handle);
            })

            test('10ユーザー以上いない場合', async () => {
                //準備
                const controlledRankUsers = await rankUsersFunc(4);
                await wrapper.setProps({
                    rankUsers: controlledRankUsers
                })

                const ticks = wrapper.vm.options.scales.yAxes[0].ticks;
                await expect(ticks.suggestedMax).toBe(5);
                await expect(ticks.suggestedMin).toBe(0);
                await expect(ticks.stepSize).toBe(5);
                await expect(wrapper.vm.options.responsive).toBeTruthy();
                await expect(wrapper.vm.options.onClick).toBe(wrapper.vm.handle);
            })
        })
    })

    describe('Testing methods', () => {

        test('handle', async () => {
            const point = {};
            const event = [
                {
                    _index: 5
                }
            ];
            await wrapper.vm.handle(point, event);
            await expect(wrapper.emitted('receive-index')).toBeTruthy();
            await expect(wrapper.emitted('receive-index')[0]).toEqual([5]);
        })
    })
})