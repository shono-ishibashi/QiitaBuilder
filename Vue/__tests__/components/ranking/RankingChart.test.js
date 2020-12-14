import {shallowMount, createLocalVue} from '@vue/test-utils';
import Component from '@/components/ranking/RankingChart';
import {afterEach, beforeEach, describe, jest} from "@jest/globals";
import 'jest-canvas-mock';

const localVue = createLocalVue();

let wrapper;
let chartData;
let options;
let width;
let height;
let renderChart;

beforeEach(() => {
    chartData = {};
    options = {};
    width = 100;
    height = 60;
    renderChart = jest.fn();
    Component.extends.methods.renderChart = renderChart
    wrapper = shallowMount(Component, {
        localVue,
        propsData: {
            chartData: chartData,
            options: options,
            width: width,
            height: height
        }
    })
})

afterEach(() => {
    wrapper.destroy();
})

describe('Testing RankingChart component', () => {
    test('is a Vue instance', () => {
        expect(wrapper.isVueInstance).toBeTruthy();
    })

    describe('watch', () => {
        test('options', async () => {
            let replaceChartData = {
                labels: ['user1', 'user2', 'user3'],
                datasets: [
                    {
                        label: 'FBした数',
                        data: [3, 2, 1],
                        backgroundColor: 'rgba(91,200,172,0.2)',
                        borderColor: 'rgba(91,200,172,1)',
                        borderWidth: 1
                    }
                ]
            };

            let replaceOptions = {
                scales: {
                    yAxes: [{
                        ticks: {
                            suggestedMax: 10,
                            suggestedMin: 5,
                            stepSize: 5
                        }
                    }]
                },
            };

            await wrapper.setProps({
                chartData: replaceChartData,
                options: replaceOptions,
            });

            await expect(renderChart).toBeCalledWith(replaceChartData, replaceOptions);
        })
    })
})