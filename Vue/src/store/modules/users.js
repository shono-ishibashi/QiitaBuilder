import axios from "axios";

export default {
    namespaced: true,
    state: {
        rankingUsers: []
    },

    mutations: {
        setRankingUsers(state, rankingUsers) {
            state.rankingUsers = rankingUsers;
        },

        resetRankingUsers(state) {
            state.rankingUsers = [];
        }
    },

    actions: {
        fetchRankingUser({commit, rootGetters}, selectRankItemId) {
            let path;
            switch (selectRankItemId) {
                case 1:
                    path = '/FBCount';
                    console.log(path)
                    break;
                case 2:
                    path = '/articleCount';
                    console.log(path)
                    break;
                case 3:
                    path = '/qiitaRecommendedCount';
                    console.log(path)
                    break;
                default:
                    //予期しないselectRankItemIdの場合はFBした数ランキングにする
                    path = '/FBCount';
                    console.log(path)
                    break;
            }
            const url = rootGetters.API_URL + "user/ranking" + path;

            axios
                .get(url)
                .then(rankingUserList => {
                    console.log(rankingUserList)
                    commit("resetRankingUsers");
                    commit("setRankingUsers", rankingUserList);
                }).catch(err => console.log("エラーだよ", err));
        }
    },

    getters: {
        users: (state) => {
            const users = [];
            state.rankingUsers.forEach(rankUser => {
                users.push(rankUser.user);
            })
            return users;
        },
        relationArticles: (state) => {
            const relationArticles = [];
            state.rankingUsers.forEach((rankUser => {
                relationArticles.push(rankUser.relationArticle);
            }));

            //重複を取り除く
            const compare = [];
            const distinctRelationArticles = relationArticles.filter(el => {
                if (compare.indexOf(el["articleId"]) === -1) {
                    compare.push(el["articleId"]);
                    return el;
                }
            })
            return distinctRelationArticles;
        }
    }
}