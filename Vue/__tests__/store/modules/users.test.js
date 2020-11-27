import Vuex from 'vuex';
import index from '@/store/index';
import users from '@/store/modules/users';
import {createLocalVue} from '@vue/test-utils';
import {cloneDeep} from 'lodash'

const localVue = createLocalVue();
localVue.use(Vuex);


const replaceRankingUsers = [
    {
        user: {
            userId: 1,
            displayName: 'user1',
            photoUrl: "photo1"
        },
        relationArticle: {
            articleId: 1,
            title: 'title1'
        },
        rank: 1
    },
    {
        user: {
            userId: 2,
            displayName: 'user2',
            photoUrl: "photo2"
        },
        relationArticle: {
            articleId: 2,
            title: 'title2'
        },
        rank: 2
    },
    {
        user: {
            userId: 3,
            displayName: 'user3',
            photoUrl: "photo3"
        },
        relationArticle: {
            articleId: 3,
            title: 'title3'
        },
        rank: 3
    },
    {
        user: {
            userId: 4,
            displayName: 'user4',
            photoUrl: "photo4"
        },
        relationArticle: {
            articleId: 2,
            title: 'title2'
        },
        rank: 4
    },
    {
        user: {
            userId: 5,
            displayName: 'user5',
            photoUrl: "photo5"
        },
        relationArticle: {
            articleId: 5,
            title: 'title5'
        },
        rank: 5
    },];

describe('store/users.js', () => {
    let store;
    beforeEach(() => {
        store = new Vuex.Store(cloneDeep(users))
    })

    //************ getters ************
    describe('getters', () => {
        test('getters: users', () => {
            store.replaceState({
                rankingUsers: replaceRankingUsers
            });

            const expected = [
                {
                    userId: 1,
                    displayName: 'user1',
                    photoUrl: 'photo1',
                    rank: 1
                },
                {
                    userId: 2,
                    displayName: 'user2',
                    photoUrl: 'photo2',
                    rank: 2
                },
                {
                    userId: 3,
                    displayName: 'user3',
                    photoUrl: 'photo3',
                    rank: 3
                },
                {
                    userId: 4,
                    displayName: 'user4',
                    photoUrl: 'photo4',
                    rank: 4
                },
                {
                    userId: 5,
                    displayName: 'user5',
                    photoUrl: 'photo5',
                    rank: 5
                }
            ];
            expect(store.getters.users).toStrictEqual(expected);
        })

        test('getters: relationArticles', () => {
            store.replaceState({
                rankingUsers: replaceRankingUsers
            });

            const expected = [
                {
                    articleId: 1,
                    title: 'title1'
                },
                {
                    articleId: 2,
                    title: 'title2'
                },
                {
                    articleId: 3,
                    title: 'title3'
                },
                {
                    articleId: 5,
                    title: 'title5'
                }
            ];

            expect(store.getters.relationArticles.length).toBe(4);
            expect(store.getters.relationArticles).toStrictEqual(expected);
        })
    })
})


