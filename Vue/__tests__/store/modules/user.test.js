import Vuex from 'vuex';
import index from '@/store/index';
import user from '@/store/modules/user';
import { createLocalVue } from '@vue/test-utils';
import { cloneDeep } from 'lodash'

const localVue = createLocalVue()
localVue.use(Vuex)
describe('mutations', () => {
    let store
    beforeEach(() => {
        store = new Vuex.Store(cloneDeep(user))
    })
    test('mutations: setArticleCardDisplay', () => {
        let articleCardDisplay = "articleCard"
        expect(store.getters.articleCardDisplay).toBeNull()
        store.commit('setArticleCardDisplay', articleCardDisplay)
        expect(store.getters.articleCardDisplay).toBe("articleCard")
    })
})

describe('getters', () => {
    let store
    beforeEach(() => {
        store = new Vuex.Store(cloneDeep(user))
    })
    test('getters: articleCardDisplay', () => {
        store.replaceState({
            articleCardDisplay: null
        })
        expect(store.getters.articleCardDisplay).toBeNull();
    })
})

describe('actions', () => {
    let action
    const testedAction = (context = {}, payload = {}) => {
        return user.actions[action](context, payload)
    }
    let store
    let commit
    let state
    beforeEach(() => {
        store = new Vuex.Store(cloneDeep(user))
        commit = store.commit
        state = store.state
    })
    test('actions: setArticleCardDisplay', async done => {
        action = "setArticleCardDisplay"
        const articleCard = "articleCard"
        await testedAction({ commit, state }, articleCard)
        expect(store.getters.articleCardDisplay).toBe("articleCard")
        done()
    })
})