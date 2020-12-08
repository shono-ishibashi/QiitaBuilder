<template>
  <div>
    <v-divider inset :key="`div-${index}`"></v-divider>
    <v-list-item class="articleCard">
      <v-list-item-avatar>
        <v-avatar class="grey lighten-1" dark>
          <img
              :src="article.postedUser.photoUrl"
              @click="toUserDetail(article.postedUser.userId)"
              style="cursor: pointer;"
              alt=""
          />
        </v-avatar>
      </v-list-item-avatar>
      <v-list-item-avatar>
        <v-avatar v-show="article.stateFlag === 2" size="25" color="primary">
          <v-tooltip top>
            <template v-slot:activator="{ on, attrs }">
              <v-icon dark v-bind="attrs" v-on.stop="on">
                mdi-check
              </v-icon>
            </template>
            <span>Qiita投稿済み</span>
          </v-tooltip>
        </v-avatar>
      </v-list-item-avatar>
      <v-list-item-content>
        <div class="title-field">
          <v-list-item-title
              class="title"
              style="cursor: pointer;"
              @click="toDetail(article.articleId)"
          >{{ article.title|truncate }}
          </v-list-item-title>
          <v-list-item-subtitle class="tag-field">
            <v-chip-group active-class="primary--text">
              <v-chip
                  v-for="tag in article.tags"
                  :key="tag.tagId"
                  color="#5bc8ac"
                  small
                  dark
                  @click="findByTagId(tag.tagId)"
              >
                {{ tag.tagName }}
              </v-chip>
            </v-chip-group>
          </v-list-item-subtitle>
        </div>
        <div>
          <v-list-item-subtitle class="subtitle-field">
            <!--            ここfilter使って表示の仕方変えてもいいかも-->
            <v-row>

              <v-col cols="5" class="margin-control">
                投稿日：{{ article.createdAt | moment() }}
              </v-col>
              <v-col cols="5" class="margin-control">
                最終更新日：{{ article.updatedAt | moment() }}
              </v-col>
              <v-col cols="2"></v-col>
            </v-row>
            <v-row>

              <v-col cols="4" class="margin-control">
                投稿者：{{ article.postedUser.displayName }}
              </v-col>
              <v-col cols="1"></v-col>
              <v-col cols="1" class="margin-control">
                <v-tooltip top>
                  <template v-slot:activator="{ on, attrs }">
                    <v-icon
                        class="qiita-icon"
                        dark
                        color="#5bc8ac"
                        v-bind="attrs"
                        v-on.stop="on"
                    >Q
                    </v-icon>
                    {{ article.qiitaRecommendPoint }}
                  </template>
                  <span>Qiita推奨数</span>
                </v-tooltip>
              </v-col>
              <v-col cols="1" class="margin-control">
                <v-tooltip top>
                  <template v-slot:activator="{ on, attrs }">
                    <v-icon
                        class="my-icon"
                        dark
                        color="red"
                        v-bind="attrs"
                        v-on.stop="on"
                    >mdi-heart
                    </v-icon>
                    {{ article.registeredMyArticleCount }}
                  </template>
                  <span>お気に入り数</span>
                </v-tooltip>
              </v-col>
              <v-col cols="1" class="margin-control">
                <v-tooltip top>
                  <template v-slot:activator="{ on, attrs }">
                    <v-icon
                        class="feed-icon"
                        color="blue"
                        v-bind="attrs"
                        v-on.stop="on"
                    >mdi-message-processing-outline
                    </v-icon>
                    {{ article.feedbackCount }}
                  </template>
                  <span>コメント数</span>
                </v-tooltip>
              </v-col>
            </v-row>

          </v-list-item-subtitle>
        </div>
      </v-list-item-content>
    </v-list-item>
  </div>
</template>

<script>
import moment from 'moment'
import {mapState, mapGetters, mapActions} from "vuex"

export default {
  name: "ArticleCard",
  data() {
    return {};
  },
  props: {
    article: {
      type: Object,
      required: true,
    },
    index: {
      type: Number,
      required: true,
    },
  },
  computed: {
    ...mapState("articles", ["searchCriteria"]),
    ...mapGetters("auth", ["loginUser"])
  },
  filters: {
    moment(value) {
      return moment(value).format("MM/DD");
    },
    truncate(value) {
      const length = 28;
      const omission = "...";
      if (value.length <= length) {
        return value;
      }
      return value.substring(0, length) + omission;
    }
  },
  methods: {
    ...mapActions("articles", ["fetchArticles"]),
    findByTagId(tagId) {
      if (this.$route.name !== 'articleList') {
        this.$router.push({name: "articleList"})
      }
      this.searchCriteria.searchTag = [tagId];
      this.searchCriteria.currentPage = 1;
      this.fetchArticles(this.searchCriteria);
      this.scrollTop()
    },
    toDetail(articleId) {
      this.$router.push({
        name: "articleDetail",
        params: {articleId},
      });
    },
    toUserDetail(userId) {
      this.$router.push({
        name: "userDetail",
        params: {userId},
      }).catch(err => {
        if (this.$route.name === "userDetail") this.$emit('thisUserPage')
        console.log(err)
      });
    },
    scrollTop() {
      window.scrollTo({
        top: 0,
        behavior: "smooth"
      });
    },
  },
};
</script>

<style scoped>
.articleCard {
  margin: 10px;
}

/*.title-field {*/
/*  margin-top: 15px;*/
/*  height: 70px;*/
/*}*/

/*.title {*/
/*  font-weight: bold;*/
/*  font-size: 40px;*/
/*  padding-top: 10px;*/
/*  !*padding-left: 30px;*!*/
/*  padding-bottom: 4px;*/
/*}*/

/*.tag-field {*/
/*  !*padding-left: 20px;*!*/
/*}*/

/*.subtitle-field {*/
/*  height: 35px;*/
/*  margin-bottom: 10px;*/
/*}*/

.margin-control {
  margin: 0;
  padding-top: 0;
  padding-bottom: 0;
}
</style>
