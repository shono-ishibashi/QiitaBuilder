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
        </v-row>
        <v-row>
          <UserList></UserList>
        </v-row>
      </v-col>
      <v-col cols="6">
        <v-row>
          <Chart></Chart>
        </v-row>
        <v-row>
          <RelationArticles></RelationArticles>
        </v-row>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import Chart from "../components/ranking/Chart.vue";
import UserList from "../components/ranking/UserList.vue";
import RelationArticles from "../components/ranking/RelationArticles.vue";
import {mapActions, mapGetters} from "vuex";

export default {
  name: "RankingComponent",
  components: {
    Chart,
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
      selectRankItemId: 1
    }
  },

  computed: {
    //ユーザー一覧
    users() {
      return this.users;
    },

    //関連記事一覧
    relationArticles() {
      return this.relationArticles;
    },
    ...mapGetters("users", ["users", "relationArticles"])
  },

  watch: {
    selectRankItemId: {
      handler() {
        this.fetchRankingUser(this.selectRankItemId);
      }
    }
  },

  created() {
    this.fetchRankingUser(this.selectRankItemId);
  },

  methods: {
    ...mapActions("users", ["fetchRankingUser"])
  }
}
</script>

<style scoped>

</style>