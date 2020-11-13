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
        <v-row>
          <UserList :rank-users="users" :select-rank-item-id="selectRankItemId"
                    :display-count="selectDisplayCount"></UserList>
        </v-row>
      </v-col>
      <v-col cols="6">
        <v-row>
          <ChartArea :select-rank-item-id="selectRankItemId" :rank-users="users"
                     @receive-index="toUserDetail($event)"></ChartArea>
        </v-row>
        <v-row>
          <RelationArticles :rel-articles="relationArticles"></RelationArticles>
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
      selectDisplayCount: 10
    }
  },

  computed: {
    apiToken() {
      return this.$store.getters["auth/apiToken"];
    },
    ...mapGetters("users", ["users", "relationArticles"])
  },

  watch: {
    selectRankItemId: {
      handler() {
        this.fetchRankingUser(this.selectRankItemId);
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