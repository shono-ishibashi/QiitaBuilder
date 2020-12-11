/* eslint-disable no-undef */
import { shallowMount, createLocalVue } from "@vue/test-utils";
import Vuex from "vuex";
import Feedbacks from "@/components/article_detail/Feedbacks";
import { afterEach, beforeEach, describe } from "@jest/globals";

const localVue = createLocalVue();
localVue.use(Vuex);

let wrapper;
let store = new Vuex.Store({
  state: {},
  mutations: {},
  actions: {},
  getters: {},
  modules: {
    auth: {
      namespaced: true,
      state: {
        loginUser: {
          userId: 5,
          password: null,
          postArticles: null,
          displayName: "gorilla",
          photoUrl:
            "https://www.nationalgeographic.com/content/dam/animals/thumbs/rights-exempt/mammals/m/mountain-gorilla_thumb.ngsversion.1538530889286.adapt.1900.1.jpg",
          feedbacks: null,
          myArticles: null,
          uid: "uiduiduid",
          feedbackCount: null,
          postedArticleCount: null,
          qiitaRecommendedAllCount: null,
          usedTags: null,
          isLoginUser: null,
        },
      },
      getters: {},
    },
  },
});
let feedbacks = [
  {
    feedbackId: 64,
    articleId: 14,
    postedUser: {
      userId: 5,
      password: null,
      postArticles: null,
      displayName: "gorilla",
      photoUrl:
        "https://www.nationalgeographic.com/content/dam/animals/thumbs/rights-exempt/mammals/m/mountain-gorilla_thumb.ngsversion.1538530889286.adapt.1900.1.jpg",
      feedbacks: null,
      myArticles: null,
      uid: "uiduiduid",
      feedbackCount: null,
      postedArticleCount: null,
      qiitaRecommendedAllCount: null,
      usedTags: null,
      isLoginUser: null,
    },
    createdAt: "2020-12-10T11:24:20",
    updatedAt: null,
    content: "test",
    feedbackVersion: 1,
    deleteFlag: 0,
  },
  {
    feedbackId: 65,
    articleId: 14,
    postedUser: {
      userId: 5,
      password: null,
      postArticles: null,
      displayName: "gorilla",
      photoUrl:
        "https://www.nationalgeographic.com/content/dam/animals/thumbs/rights-exempt/mammals/m/mountain-gorilla_thumb.ngsversion.1538530889286.adapt.1900.1.jpg",
      feedbacks: null,
      myArticles: null,
      uid: "uiduiduid",
      feedbackCount: null,
      postedArticleCount: null,
      qiitaRecommendedAllCount: null,
      usedTags: null,
      isLoginUser: null,
    },
    createdAt: "2020-12-11T09:09:34",
    updatedAt: null,
    content: "test2",
    feedbackVersion: 1,
    deleteFlag: 0,
  },
];

beforeEach(() => {
  wrapper = shallowMount(Feedbacks, {
    store,
    localVue,
    propsData: {
      feedbacks: feedbacks,
    },
  });
});

afterEach(() => {
  wrapper.destroy();
  feedbacks = [
    {
      feedbackId: 64,
      articleId: 14,
      postedUser: {
        userId: 5,
        password: null,
        postArticles: null,
        displayName: "gorilla",
        photoUrl:
          "https://www.nationalgeographic.com/content/dam/animals/thumbs/rights-exempt/mammals/m/mountain-gorilla_thumb.ngsversion.1538530889286.adapt.1900.1.jpg",
        feedbacks: null,
        myArticles: null,
        uid: "uiduiduid",
        feedbackCount: null,
        postedArticleCount: null,
        qiitaRecommendedAllCount: null,
        usedTags: null,
        isLoginUser: null,
      },
      createdAt: "2020-12-10T11:24:20",
      updatedAt: null,
      content: "test",
      feedbackVersion: 1,
      deleteFlag: 0,
    },
    {
      feedbackId: 65,
      articleId: 14,
      postedUser: {
        userId: 5,
        password: null,
        postArticles: null,
        displayName: "gorilla",
        photoUrl:
          "https://www.nationalgeographic.com/content/dam/animals/thumbs/rights-exempt/mammals/m/mountain-gorilla_thumb.ngsversion.1538530889286.adapt.1900.1.jpg",
        feedbacks: null,
        myArticles: null,
        uid: "uiduiduid",
        feedbackCount: null,
        postedArticleCount: null,
        qiitaRecommendedAllCount: null,
        usedTags: null,
        isLoginUser: null,
      },
      createdAt: "2020-12-11T09:09:34",
      updatedAt: null,
      content: "test2",
      feedbackVersion: 1,
      deleteFlag: 0,
    },
  ];
});

describe("Testing Feedbacks component", () => {
  test("is a Vue instance", () => {
    expect(wrapper.isVueInstance).toBeTruthy();
  });

  describe("Testing props", () => {
    test("feedbacks", () => {
      expect(wrapper.props().feedbacks).toBe(feedbacks);
    });
  });

  describe("Testing methods", () => {
    test("editFeedback method", async () => {
      wrapper.vm.$emit("editFeedback", feedbacks[0]);
      wrapper.vm.$emit("editFeedback", feedbacks[1]);

      await wrapper.vm.$nextTick(); // Wait until $emits have been handled
      // 実行
      // assert event has been emitted
      expect(wrapper.emitted().editFeedback).toBeTruthy();

      // assert event count
      expect(wrapper.emitted().editFeedback.length).toBe(2);

      // assert event payload
      expect(wrapper.emitted().editFeedback[1]).toEqual([feedbacks[1]]);
    });

    test("deleteFeedback method", async () => {
      wrapper.vm.$emit("deleteFeedback", feedbacks[0]);
      wrapper.vm.$emit("deleteFeedback", feedbacks[1]);

      await wrapper.vm.$nextTick(); // Wait until $emits have been handled
      // 実行
      // assert event has been emitted
      expect(wrapper.emitted().deleteFeedback).toBeTruthy();

      // assert event count
      expect(wrapper.emitted().deleteFeedback.length).toBe(2);

      // assert event payload
      expect(wrapper.emitted().deleteFeedback[1]).toEqual([feedbacks[1]]);
    });
  });
  describe("Testing props to child-component", () => {
    test("Feedback", () => {
      const feedbackComponents = wrapper.findAllComponents({ name: "Feedback" });
      // assert component has been existed
      expect(feedbackComponents.at(0).exists()).toBeTruthy();
      expect(feedbackComponents.at(1).exists()).toBeTruthy();

      // assert component count
      expect(feedbackComponents).toHaveLength(2);

      // assert props payload
      expect(feedbackComponents.at(0).vm.$props.feedback).toEqual(feedbacks[0]);
      expect(feedbackComponents.at(1).vm.$props.feedback).toEqual(feedbacks[1]);
    });
  });
});
