<template>
  <div>
    <v-divider inset :key="`div-${index}`"></v-divider>
    <v-list-item class="articleCard">
      <v-list-item-avatar>
        <v-avatar
            class="grey lighten-1"
            dark
        >
          <v-tooltip bottom>
            <template v-slot:activator="{ on, attrs }">
              <span
                  v-bind="attrs"
                  v-on.stop="on"
                  class="white--text headline"
              >
                <img :src="article.postedUser.photoUrl"/>
              </span>
            </template>
            <span>{{ article.postedUser.displayName }}</span>
          </v-tooltip>
        </v-avatar>
      </v-list-item-avatar>
      <v-list-item-avatar>
        <v-avatar
            v-show="article.stateFlag===2"
            size="25"
            color="primary"
        >
          <v-tooltip top>
            <template v-slot:activator="{ on, attrs }">
              <v-icon
                  dark
                  v-bind="attrs"
                  v-on.stop="on">
                mdi-check
              </v-icon>
            </template>
            <span>Qiita投稿済み</span>
          </v-tooltip>
        </v-avatar>
      </v-list-item-avatar>
      <v-list-item-content>
        <div class="title-field">
          <v-list-item-title v-text="article.title" class="title"></v-list-item-title>
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
              <v-col cols="3">
                投稿日時：{{ article.createdAt|moment() }}
              </v-col>
              <v-col cols="3">
                最終更新日時：{{ article.updatedAt|moment() }}
              </v-col>
              <v-col cols="2"></v-col>
              <v-col cols="1">
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
              <v-col cols="1">
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
                  <span>My記事登録数</span>
                </v-tooltip>
              </v-col>
              <v-col cols="1">
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
import {mapState,mapGetters, mapActions} from "vuex"

export default {
  name: "ArticleCard",
  data() {
    return {}
  },
  props: {
    article: {
      type: Object,
      required: true
    },
    index: {
      type: Number,
      required: true
    }
  },
  computed: {
    ...mapState("articles", ["searchCriteria"]),
    ...mapGetters("auth",["loginUser"])
  },
  filters: {
    moment(value) {
      return moment(value).format("MM/DD hh:mm");
    }
  },
  methods: {
    ...mapActions("articles", ["fetchArticles"]),
    findByTagId(tagId) {
      this.searchCriteria.searchTag = [tagId];
      this.fetchArticles(this.searchCriteria)
    }
  }
}
</script>

<style scoped>
.articleCard {
  height: 110px;
}

.title-field {
  margin-top: 15px;
  height: 70px;
}

.title {
  font-weight: bold;
  font-size: 40px;
  padding-top: 10px;
  padding-left: 30px;
  padding-bottom: 4px;
}

.tag-field {
  padding-left: 20px;
}

.subtitle-field {
  height: 35px;
  margin-bottom: 10px;
}

</style>