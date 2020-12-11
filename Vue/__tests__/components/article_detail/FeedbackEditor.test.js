/* eslint-disable no-undef */
import { shallowMount, createLocalVue } from "@vue/test-utils";
import Vuex from "vuex";
import Vuetify from 'vuetify';
import FeedbackEditor from "@/components/article_detail/FeedbackEditor";
import { afterEach, beforeEach, describe, jest } from "@jest/globals";

const localVue = createLocalVue();
localVue.use(Vuex);
localVue.use(Vuetify);

let store;
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
let mockMethods;
let wrapper;
const sel = (id) => `[data-test-id="${id}"]`; //sel('id')の形でidにtemplate内に付与されたdata-test属性の名前を指定したら付与された要素を指定できる

beforeEach(() => {
  store = new Vuex.Store({
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
            photoURL:
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

  mockMethods = {
    resetValidation: jest.fn(),
  };

  wrapper = shallowMount(FeedbackEditor, {
    store,
    localVue,
    propsData: {
      feedback: feedback,
    },
    mocks: mockMethods,
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

describe("Testing FeedbackEditor component", () => {
  test("is a Vue instance", () => {
    expect(wrapper.isVueInstance).toBeTruthy();
  });

  describe("Testing exist element", () => {
    test("user icon", () => {
      expect(wrapper.get(sel("userIcon")).attributes("src")).toBe(
        "https://www.nationalgeographic.com/content/dam/animals/thumbs/rights-exempt/mammals/m/mountain-gorilla_thumb.ngsversion.1538530889286.adapt.1900.1.jpg"
      );
    });
    test("user name", () => {
      expect(wrapper.find(sel("userName")).text()).toBe("@gorilla");
    });
    test("content", async () => {
      expect(wrapper.get(sel("postButton")).attributes("disabled")).toBe(
        "true"
      );
      expect(wrapper.find(sel("content")).text()).toBe("");
    });
  });

  describe("Testing props", () => {
    test("feedback", () => {
      expect(wrapper.props().feedback).toBe(feedback);
    });
  });

  describe("Testing filters", () => {
    test("postOrEdit", () => {
      expect(wrapper.find(sel("postButton")).text()).toBe("更新する");

      feedback.feedbackId = null;
      wrapper = shallowMount(FeedbackEditor, {
        store,
        localVue,
        propsData: {
          feedback: feedback,
        },
      });
      expect(wrapper.find(sel("postButton")).text()).toBe("投稿する");
    });
  });

  describe("Testing created", () => {
    test("loginUser", () => {
      expect(wrapper.vm.loginUser.uid).toBe("uiduiduid");
    });
  });

  describe("Testing methods", () => {
    test("closeEditor method", async () => {
      // 実行
      await wrapper.vm.closeEditor();

      await wrapper.vm.$nextTick(); // Wait until $emits have been handled

      expect(mockMethods.resetValidation).toBeCalled();
      expect(wrapper.emitted().closeEditor).toBeTruthy();
      expect(wrapper.emitted().closeEditor.length).toBe(1);
    });

    test("saveFeedback method(valid is true)", async () => {
      // 事前準備
      wrapper.vm.valid = true;

      // 実行
      wrapper.vm.saveFeedback();

      await wrapper.vm.$nextTick(); // Wait until $emits have been handled

      expect(wrapper.emitted().postFeedback).toBeTruthy();
      expect(wrapper.emitted().postFeedback.length).toBe(1);
      expect(wrapper.vm.feedback.content).toBe(undefined);
    });
    test("saveFeedback method(valid is false)", async () => {
      // 事前準備
      wrapper.vm.valid = false;

      // 実行
      wrapper.vm.saveFeedback();

      await wrapper.vm.$nextTick(); // Wait until $emits have been handled

      expect(wrapper.emitted().postFeedback).toBeFalsy();
      expect(wrapper.vm.feedback.content).toBe(undefined);
    });
  });
});
