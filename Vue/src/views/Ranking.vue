<template>
  <v-container fluid>
    <v-row>
      <v-col cols="6">
        <v-row justify="center" align-content="center">
          <v-col cols="5">
            <v-select
                v-model="selectRankItemId"
                :items="rankItems"
                item-text="item"
                item-value="id"
                color="#5bc8ac"
                outlined
                label="ランキング項目"
            ></v-select>
          </v-col>
          <v-col cols="2">
            <v-select
                :items="displayCountList"
                item-text="text"
                item-value="value"
                v-model="selectDisplayCount"
                color="#5bc8ac"
                label="表示件数"
            >
            </v-select>
          </v-col>
        </v-row>
        <v-row align-content="center" justify="center">
          <v-col cols="6" v-show="isLoading">
            <v-progress-linear
                color="green"
                indeterminate
                rounded
                height="10"
            ></v-progress-linear>
          </v-col>
          <UserList v-show="isDisplay" :rank-users="users" :select-rank-item-id="selectRankItemId"
                    :display-count="selectDisplayCount"></UserList>
        </v-row>
      </v-col>
      <v-col cols="6">
        <v-row>
          <h3>
            <v-icon color="#5bc8ac">mdi-chess-king</v-icon>
            {{ rankTitle }}
          </h3>
        </v-row>
        <v-row align-content="center" justify="center">
          <ChartArea :select-rank-item-id="selectRankItemId" :rank-users="users"
                     @receive-index="toUserDetail($event)"></ChartArea>
        </v-row>
        <v-row>
          <h3>
            <v-icon color="#5bc8ac">
              mdi-book-open-blank-variant
            </v-icon>
            関連記事一覧
          </h3>
        </v-row>
        <v-row align-content="center" justify="center">
          <v-col cols="6" v-show="isLoading">
            <v-progress-linear
                color="green"
                indeterminate
                rounded
                height="10"
            ></v-progress-linear>
          </v-col>
          <RelationArticles v-show="isDisplay" :rel-articles="relationArticles"></RelationArticles>
        </v-row>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import ChartArea from "../components/ranking/ChartArea.vue";
import UserList from "../components/ranking/UserList.vue";
import RelationArticles from "../components/ranking/RelationArticles.vue";
import {mapActions, mapGetters} from "vuex";


export default {
  name: "RankingComponent",

  components: {
    ChartArea,
    UserList,
    RelationArticles
  },

  data() {
    return {
      //ランキング項目
      rankItems: [
        {
          item: 'FBした数',
          id: 1
        },
        {
          item: '記事投稿数',
          id: 2
        },
        {
          item: 'Qiita推薦累計数',
          id: 3
        }
      ],

      //選択されているランキング項目
      selectRankItemId: 1,

      //表示件数リスト
      displayCountList: [
        {
          text: '10件',
          value: 10
        }, {
          text: '20件',
          value: 20
        }, {
          text: '30件',
          value: 30
        }],

      //選択されている表示件数
      selectDisplayCount: 10,

      isLoading: false,
      isDisplay: true
    }
  },

  computed: {
    apiToken() {
      return this.$store.getters["auth/apiToken"];
    },
    rankTitle() {
      let rankTitle;
      switch (this.selectRankItemId) {
        case 1:
          rankTitle = 'FBした数ランキング';
          break;
        case 2:
          rankTitle = '記事投稿数ランキング';
          break;
        case 3:
          rankTitle = 'Qiita推薦累計数ランキング';
          break;
      }
      return rankTitle;
    },
    ...mapGetters("users", ["users", "relationArticles"])
  },

  watch: {
    selectRankItemId: {
      handler() {
        this.isDisplay = false;
        this.isLoading = true;

        this.fetchRankingUser(this.selectRankItemId);
        this.$nextTick();
        setTimeout(() => {
          this.isDisplay = true;
          this.isLoading = false;
        }, 1300)
      }
    },
    apiToken: function () {
      this.fetchRankingUser(this.selectRankItemId);
    }
  },

  methods: {
    ...mapActions("users", ["fetchRankingUser"]),
    toUserDetail(index) {
      const userId = this.users[index].userId;
      this.$router.push({name: 'userDetail', params: {userId: userId}});
    }
  }
}
</script>

<style scoped>

</style>