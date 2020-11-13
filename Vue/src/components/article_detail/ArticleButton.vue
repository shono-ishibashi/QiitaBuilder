<template>
  <div>
    <v-row>
      <v-col class="hidden-xl-only hidden-lg-only hidden-md-only" cols="7">
        <!-- 登録済み -->
        <v-tooltip top v-if="recommendId">
          <template v-slot:activator="{ on, attrs }">
            <v-btn
              v-bind="attrs"
              v-on="on"
              class="mx-2"
              dark
              block
              elevation="2"
              large
              color="green"
              @click="toggleRecommend"
            >
              <v-icon dark>
                mdi-thumb-up
              </v-icon>
              <span style="padding-left: 10px" class="white--text">
                {{ qiitaRecommendPoint }}
              </span>
            </v-btn>
          </template>
          <span>Qiita推薦</span>
        </v-tooltip>
        <v-tooltip top v-if="!recommendId">
          <template v-slot:activator="{ on, attrs }">
            <v-btn
              v-bind="attrs"
              v-on="on"
              class="mx-2"
              block
              elevation="2"
              large
              outlined
              color="green"
              @click="toggleRecommend"
            >
              <v-icon color="green">
                mdi-thumb-up
              </v-icon>
              <span style="padding-left: 10px" class="green--text">
                {{ qiitaRecommendPoint }}
              </span>
            </v-btn>
          </template>
          <span>Qiita推薦</span>
        </v-tooltip>
      </v-col>
      <v-col class="hidden-xl-only hidden-lg-only hidden-md-only" cols="5">
        <!-- 登録済み -->
        <v-tooltip top v-if="myArticleId">
          <template v-slot:activator="{ on, attrs }">
            <v-btn
              v-bind="attrs"
              v-on="on"
              class="mx-2"
              dark
              block
              elevation="2"
              large
              color="pink"
              @click="toggleMyArticle"
            >
              <v-icon dark>
                mdi-heart
              </v-icon>
            </v-btn>
          </template>
          <span>My記事登録</span>
        </v-tooltip>
        <!-- 未登録 -->
        <v-tooltip top v-if="!myArticleId">
          <template v-slot:activator="{ on, attrs }">
            <v-btn
              v-bind="attrs"
              v-on="on"
              class="mx-2"
              block
              elevation="2"
              large
              outlined
              @click="toggleMyArticle"
            >
              <v-icon color="blue-grey">
                mdi-heart
              </v-icon>
            </v-btn>
          </template>
          <span>My記事登録</span>
        </v-tooltip>
      </v-col>
    </v-row>
  </div>
</template>

<script>
export default {
  props: ["myArticleId", "recommendId", "qiitaRecommendPoint"],
  methods: {
    toggleRecommend() {
      this.$emit("toggleRecommend");
    },
    toggleMyArticle() {
      this.$emit("toggleMyArticle");
    },
  },
};
</script>
