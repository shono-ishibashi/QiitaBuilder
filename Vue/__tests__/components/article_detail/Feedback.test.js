/* eslint-disable no-undef */
import { shallowMount, createLocalVue } from "@vue/test-utils";
import Vuex from "vuex";
import Feedback from "@/components/article_detail/Feedback";
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
let feedback = {
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
};
const sel = (id) => `[data-test-id="${id}"]`; //sel('id')の形でidにtemplate内に付与されたdata-test属性の名前を指定したら付与された要素を指定できる

beforeEach(() => {
  wrapper = shallowMount(Feedback, {
    store,
    localVue,
    propsData: {
      feedback: feedback,
    },
  });
});

afterEach(() => {
  wrapper.destroy();
  feedback = {
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
  };
});

describe("Testing Feedback component", () => {
  test("is a Vue instance", () => {
    expect(wrapper.isVueInstance).toBeTruthy();
  });

  describe("Testing exist element", () => {
    test("user name", () => {
      expect(wrapper.find(sel("userName")).text()).toBe(
        "@" + feedback.postedUser.displayName
      );
    });
    test("created at & updated at", () => {
      // 編集履歴なしの場合
      expect(wrapper.find(sel("lastEdit")).text()).toBe("2020-12-10 11:24");

      // 編集済みの場合
      feedback.updatedAt = "2020-12-11T11:24:20";
      wrapper = shallowMount(Feedback, {
        store,
        localVue,
        propsData: {
          feedback: feedback,
        },
      });
      expect(wrapper.find(sel("lastEdit")).text()).toBe(
        "2020-12-11 11:24 (編集済み)"
      );
    });
    // メニュー
    test("menu icon", () => {
      // 投稿者の場合
      expect(wrapper.find(sel("menuIcon")).isVisible()).toBeTruthy();

      // 非投稿者の場合
      feedback.postedUser.uid = "otherUid";
      wrapper = shallowMount(Feedback, {
        store,
        localVue,
        propsData: {
          feedback: feedback,
        },
      });
      expect(wrapper.html().includes(sel("menuIcon"))).toBeFalsy();
    });
  });

  describe("Testing props", () => {
    test("feedback", () => {
      expect(wrapper.props().feedback).toBe(feedback);
    });
  });

  describe("Testing filters", () => {
    test("date", () => {
      expect(wrapper.find(sel("lastEdit")).text()).toBe("2020-12-10 11:24");
    });
  });

  describe("Testing computed", () => {
    test("lastEditAt1", () => {
      expect(wrapper.vm.lastEditAt).toStrictEqual({
        time: feedback.createdAt,
        text: "",
      });
    });

    test("lastEditAt2", () => {
      feedback.updatedAt = "2020-12-11T11:24:20";
      wrapper = shallowMount(Feedback, {
        store,
        localVue,
        propsData: {
          feedback: feedback,
        },
      });
      expect(wrapper.vm.lastEditAt).toStrictEqual({
        time: feedback.updatedAt,
        text: " (編集済み)",
      });
    });

    test("loginUser", () => {
      expect(wrapper.vm.loginUser.uid).toBe("uiduiduid");
    });
  });

  describe("Testing methods", () => {
    test("editFeedback method", async () => {
      wrapper.vm.$emit("editFeedback", null);
      wrapper.vm.$emit("editFeedback", feedback);

      await wrapper.vm.$nextTick(); // Wait until $emits have been handled
      // 実行
      // assert event has been emitted
      expect(wrapper.emitted().editFeedback).toBeTruthy();

      // assert event count
      expect(wrapper.emitted().editFeedback.length).toBe(2);

      // assert event payload
      expect(wrapper.emitted().editFeedback[1]).toEqual([feedback]);
    });

    test("deleteFeedback method", async () => {
      wrapper.vm.$emit("deleteFeedback", null);
      wrapper.vm.$emit("deleteFeedback", feedback);

      await wrapper.vm.$nextTick(); // Wait until $emits have been handled
      // 実行
      // assert event has been emitted
      expect(wrapper.emitted().deleteFeedback).toBeTruthy();

      // assert event count
      expect(wrapper.emitted().deleteFeedback.length).toBe(2);

      // assert event payload
      expect(wrapper.emitted().deleteFeedback[1]).toEqual([feedback]);
    });
  });
});
