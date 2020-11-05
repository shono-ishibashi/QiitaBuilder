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
                人
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
                  active="true"
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
              <v-col>
                最終更新日：{{ article.updatedAt|moment() }}
              </v-col>
              <v-col>
                <v-icon class="qiita-icon" dark>Q</v-icon>
                {{ article.qiitaRecommendPoint }}
              </v-col>
              <v-col>
                <v-icon class="my-icon" dark>M</v-icon>
                {{ article.registeredMyArticleCount }}
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
import {mapState, mapActions} from "vuex"

export default {
  name: "ArticleCard",
  data() {
    return {
    }
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
    ...mapState("articles", ["searchCriteria"])
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
  /*background-color: #EEEEEE;*/
  margin-bottom: 20px;
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
  height: 30px;
}

.qiita-icon {
  background-color: #5bc8ac;
}

.my-icon {
  background-color: red;
}

.qiita-post-check {
  background-color: blue;
}
</style>