<template>
  <v-container>
    <v-pagination
        v-show="rankUsers.length"
        v-model="currentPage"
        :length="totalPage"
        color="#5bc8ac"
        circle
        data-test-id="topPagination"
    ></v-pagination>
    <UserCard class="control-margin" v-for="(rankUser, index) in controlledRankUsers" :key="rankUser.userId"
              :rank-user="rankUser"
              :rank-item-id="rankItemId" :rank-num="rankUser.rank" :data-test-id="'userCard'+index"></UserCard>
    <v-pagination
        v-show="rankUsers.length"
        v-model="currentPage"
        :length="totalPage"
        color="#5bc8ac"
        circle
        data-test-id="bottomPagination"
    ></v-pagination>
  </v-container>
</template>

<script>
import UserCard from "./UserCard.vue";

export default {
  name: "UserList",

  components: {
    UserCard
  },

  props: {
    rankUsers: {
      type: Array
    },
    displayCount: {
      type: Number,
      required: true
    },
    rankItemId: {
      type: Number,
      required: true
    }
  },

  data() {
    return {
      currentPage: 1,
    }
  },

  computed: {
    totalPage() {
      let totalPage;
      if (this.rankUsers.length % this.displayCount === 0) {
        totalPage = this.rankUsers.length / this.displayCount;
      } else {
        totalPage = Math.floor(this.rankUsers.length / this.displayCount) + 1;
      }
      return totalPage;
    },
    controlledRankUsers() {
      const begin = (this.currentPage - 1) * this.displayCount;
      const end = this.currentPage * this.displayCount;
      return this.rankUsers.slice(begin, end);
    }
  },

  watch: {
    displayCount() {
      this.currentPage = 1;
    },
    currentPage() {
      setTimeout(() => {
        this.$vuetify.goTo(0);
      }, 100)
    },
    rankItemId() {
      this.currentPage = 1;
    }
  }
}
</script>

<style scoped>
.control-margin {
  padding: 0;
  margin: 0;
}
</style>