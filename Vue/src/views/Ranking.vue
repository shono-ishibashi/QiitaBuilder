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
                item-color="green"
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
                item-color="green"
            >
            </v-select>
          </v-col>
        </v-row>
        <v-row align-content="center" justify="center">
          <v-col cols="6">
            <v-progress-linear
                v-show="isLoading"
                color="green"
                indeterminate
                rounded
                height="10"
            ></v-progress-linear>
          </v-col>
          <UserList v-show="isDisplay" :rank-users="users"
                    :display-count="selectDisplayCount" :rank-item-id="rankItemId"></UserList>
        </v-row>
      </v-col>
      <v-col cols="6">
        <v-row>
          <h3 v-if="rankUsersLength">
            <v-icon color="#5bc8ac">mdi-chess-king</v-icon>
            {{ rankTitle }}
          </h3>
        </v-row>
        <v-row align-content="center" justify="center">
          <v-col cols="6">
            <v-progress-linear
                v-show="isLoading"
                color="green"
                indeterminate
                rounded
                height="10"
            ></v-progress-linear>
          </v-col>
          <ChartArea v-show="isDisplay" :select-rank-item-id="selectRankItemId" :rank-users="users"
                     @receive-index="toUserDetail($event)"></ChartArea>
        </v-row>
        <v-row>
          <h3 v-if="rankUsersLength">
            <v-icon color="#5bc8ac">
              mdi-book-open-blank-variant
            </v-icon>
            関連記事一覧
          </h3>
        </v-row>
        <v-row align-content="center" justify="center">
          <v-col cols="6">
            <v-progress-linear
                v-show="isLoading"
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
    <v-row align-content="center" justify="center" v-if="rankUsersLength === 0">
      <v-alert
          text
          dense
          color="teal"
          icon="mdi-emoticon-confused"
          border="left"
      >
        ランキング該当ユーザーが存在しません
      </v-alert>
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

      //選択されているランキング項目（初期表示：FBした数）
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

      //Load
      isLoading: false,

      //{display: none}の切り替え
      isDisplay: true,

      //UserListコンポーネントに渡すランキング項目ID
      rankItemId: 1,

      //ランキングユーザー数
      rankUsersLength: null
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
      async handler() {
        await this.fetchRankingUser(this.selectRankItemId);
        this.rankUsersLength = Number(this.users.length);
        await this.$nextTick();

        this.isDisplay = false;
        this.isLoading = true;

        if (this.users.length) {
          setTimeout(() => {
            this.isDisplay = true;
            this.isLoading = false;
          }, 1300)
        } else {
          this.isDisplay = false;
          this.isLoading = false;
        }
        this.rankItemId = this.selectRankItemId
      }
    },
    apiToken: {
      async handler() {
        await this.fetchRankingUser(this.selectRankItemId);
        this.rankUsersLength = Number(this.users.length);
      }
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