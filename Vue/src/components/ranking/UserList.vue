<template>
  <v-container>
    <v-pagination
        v-model="currentPage"
        :length="totalPage"
        color="#5bc8ac"
        circle
    ></v-pagination>
    <UserCard class="control-margin" v-for="rankUser in controlledRankUsers" :key="rankUser.userId"
              :rank-user="rankUser"
              :rank-item-id="selectRankItemId" :rank-num="rankUser.rank"></UserCard>
    <v-pagination
        v-model="currentPage"
        :length="totalPage"
        color="#5bc8ac"
        circle
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
    selectRankItemId: {
      type: Number,
      required: true
    },
    displayCount: {
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
      if (this.rankUsers.length / this.displayCount === 0) {
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