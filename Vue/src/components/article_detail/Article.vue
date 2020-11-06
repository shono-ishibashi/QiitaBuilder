<template>
  <v-app class="white d-flex">
    <v-container class="area">
      <v-row align="center" class="spacer">
        <v-col cols="1" sm="1" md="1">
          <v-avatar size="42px">
            <img src="https://cdn.vuetifyjs.com/images/john.jpg" alt="John" />
          </v-avatar>
        </v-col>
        <v-col class="" cols="2" sm="2" md="2">
          <strong>@dddddd{{ article.postedUser.displayName }}</strong>
        </v-col>
        <!-- 投稿日または更新日 -->
        <v-col class="hidden-xs-only" sm="4" md="4">
          <span v-if="article.updatedAt">
            {{ article.updatedAt | date }}に更新
          </span>
          <span v-else> {{ article.createdAt | date }}に作成 </span>
        </v-col>
        <v-col class="" sm="3" md="3">
          <v-menu offset-y>
            <template v-slot:activator="{ attrs, on }">
              <v-btn
                v-bind="attrs"
                v-on="on"
                class="white"
                style="text-transform: none;"
              >
                {{ article.stateFlag | namestatus }}
              </v-btn>
            </template>
            <v-list>
              <v-list-item v-for="item in qiitaMenus" :key="item" link>
                <v-list-item-title v-text="item"></v-list-item-title>
              </v-list-item>
            </v-list>
          </v-menu>
        </v-col>
        <v-col class="" sm="2" md="2">
          <v-menu offset-y>
            <template v-slot:activator="{ attrs, on }">
              <v-btn icon v-bind="attrs" v-on="on" class="">
                <v-icon>mdi-format-list-bulleted</v-icon>
              </v-btn>
            </template>

            <v-list>
              <v-list-item v-for="item in menus" :key="item" link>
                <v-list-item-title v-text="item"></v-list-item-title>
              </v-list-item>
            </v-list>
          </v-menu>
        </v-col>
      </v-row>
      <v-row align="center" class="spacer">
        <v-col
          class="hidden-xl-only hidden-lg-only hidden-md-only"
          cols="2"
          sm="2"
        >
          <v-btn elevation="2" id="qiita_btn" style="text-transform: none;"
            >Qiita</v-btn
          >
        </v-col>
        <v-col
          class="hidden-xl-only hidden-lg-only hidden-md-only"
          cols="2"
          sm="2"
        >
          <v-btn elevation="2" id="my_btn" style="text-transform: none;"
            >My記事登録</v-btn
          >
        </v-col>
      </v-row>

      <h1>{{ article.title }}</h1>
      <div>
        tags :
        <ul>
          <li v-for="tag in article.tags" :key="tag.tagId">
            {{ tag.tagName }}
          </li>
        </ul>
      </div>
    </v-container>
    <v-container>
      <v-main>
        content :
        {{ article.content }}
      </v-main>
    </v-container>
  </v-app>
</template>

<script>
export default {
  data() {
    return {
      menus: ["記事を編集する", "記事を削除する"],
      qiitaMenus: ["Qiitaに投稿する", "Qiitaを更新する"],
    };
  },
  props: ["article"],
  filters: {
    namestatus: function(value) {
      if (value == 2) {
        return "Qiitaに投稿済み";
      }
      return "Qiitaに未投稿";
    },
    date: function(value) {
      if (!value) return "";
      var date = value.split("T")[0];
      var ymd = date.split("-");
      return ymd[0] + "年" + ymd[1] + "月" + ymd[2] + "日";
    },
  },
};
</script>

<style scoped>
.area {
  padding: 20px;
}
#qiita_btn {
  position: sticky;
  top: 35%;
}
#my_btn {
  position: sticky;
  top: 45%;
}
</style>
