<template>
  <v-container>
    <UserCard class="control-margin" v-for="(rankUser, index) in rankUsers" :key="rankUser.userId" :rank-user="rankUser"
              :rank-item-id="selectRankItemId" :rank-num="index + 1"></UserCard>
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
    selectRankItemId: null
  }
}
</script>

<style scoped>
.control-margin {
  padding: 0px;
  margin: 0px;
}
</style>