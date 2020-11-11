<template>
  <v-container>
    <UserCard class="control-margin" v-for="(rankUser, index) in controlledRankUsers" :key="rankUser.userId" :rank-user="rankUser"
              :rank-item-id="selectRankItemId" :rank-num="index + 1"></UserCard>
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
  data() {
    return {
      currentPage: 1,
    }
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
  watch: {
    displayCount(){
      this.currentPage = 1;
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
  }
}
</script>

<style scoped>
.control-margin {
  padding: 0;
  margin: 0;
}
</style>