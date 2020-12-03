<template>
  <v-container class="control-margin">
    <v-row justify="center" align-content="center" class="control-margin">
      <v-col cols="1" class="rank control-margin">
        <v-icon v-if="rankNum === 1" color="#FFCC00">
          mdi-trophy-variant
        </v-icon>
        <v-icon v-else-if="rankNum === 2" color="#AAAAAA">
          mdi-trophy-variant
        </v-icon>
        <v-icon v-else-if="rankNum === 3" color="#996600">
          mdi-trophy-variant
        </v-icon>
        <div v-else>
          {{ rankNum }}
        </div>
      </v-col>
      <v-col cols="10">
        <v-list-item class="control-margin">
          <v-list-item-avatar size="70" class="avatar control-margin">
            <v-avatar
                color="orange"
                size="70"
                @click="toUserDetail(rankUser.userId)"
            >
              <v-img :src="rankUser.photoUrl"></v-img>
            </v-avatar>
          </v-list-item-avatar>
          <v-list-item-content class="control-margin">
            <div>
              <v-list-item-title class="name control-margin">
                <v-btn
                    @click="toUserDetail(rankUser.userId)"
                    text><span class="name">{{ rankUser.displayName }}</span>
                </v-btn>
              </v-list-item-title>
            </div>
            <div>
              <v-list-item-subtitle>
                <v-row class="control-margin">
                  <v-col cols="4" align="center" :class="[{box : (rankItemId === 1)}]">
                    FBした数
                    <div :class="[{count : true}, {select : (rankItemId === 1)}]">{{ rankUser.feedbackCount }}</div>
                  </v-col>
                  <v-col cols="4" align="center" :class="[{box : (rankItemId === 2)}]">
                    記事投稿数
                    <div :class="[{count : true}, {select : (rankItemId === 2)}]">{{ rankUser.postedArticleCount }}
                    </div>
                  </v-col>
                  <v-col cols="4" align="center" :class="[{box : (rankItemId === 3)}]">
                    Qiita推薦累計数
                    <div :class="[{count : true}, {select : (rankItemId === 3)}]">{{
                        rankUser.qiitaRecommendedAllCount
                      }}
                    </div>
                  </v-col>
                </v-row>
              </v-list-item-subtitle>
            </div>
          </v-list-item-content>
        </v-list-item>
      </v-col>
    </v-row>
    <v-divider inset></v-divider>
  </v-container>
</template>

<script>
export default {
  name: "UserCard",

  props: {
    rankUser: {
      type: Object,
      required: true
    },
    rankItemId: {
      type: Number,
      required: true
    },
    rankNum: {
      type: Number,
      required: true
    }
  },

  methods: {
    toUserDetail(userId) {
      this.$router.push({name: 'userDetail', params: {userId: userId}});
    }
  }
}
</script>

<style scoped>
.name {
  font-size: 150%;
  font-weight: bold;
}

.count {
  font-size: 16px;
}

.box {
  background: #f0f7ff;
  border: dashed 2px #5bc8ac;
  border-radius: 10px;
}

.select {
  font-weight: bold;
  color: black;
}

.rank {
  font-size: 20px;
  font-weight: bold;
  color: #6d6d6d;
  display: flex;
  justify-content: center;
  align-items: center;
}

.avatar {
  cursor: pointer;
}

.control-margin {
  margin: 0;
  padding: 0;
}
</style>