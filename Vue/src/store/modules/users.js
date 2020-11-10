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
        fetchRankingUser({commit, rootState, rootGetters}, selectRankItemId) {
            let path;
            switch (selectRankItemId) {
                case 1:
                    path = '/FBCount';
                    break;
                case 2:
                    path = '/articleCount';
                    break;
                case 3:
                    path = '/qiitaRecommendedCount';
                    break;
                default:
                    //予期しないselectRankItemIdの場合はFBした数ランキングにする
                    path = '/FBCount';
                    break;
            }
            const url = rootGetters.API_URL + "user/ranking" + path;
            const apiToken = rootState.auth.apiToken;

            axios
                .get(url, {
                    headers: {
                        "Authorization": apiToken
                    }
                })
                .then(rankingUserList => {

                    commit("resetRankingUsers");

                    commit("setRankingUsers", rankingUserList.data);
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
            state.rankingUsers.forEach(rankUser => {
                if(null !== rankUser.relationArticle){
                    relationArticles.push(rankUser.relationArticle);
                }
            })

            //重複を取り除く
            const resultRelArt = relationArticles
                .filter((element, index, self) =>
                    self.findIndex(e => e.articleId === element.articleId) === index);

            return resultRelArt;
        }
    }
}