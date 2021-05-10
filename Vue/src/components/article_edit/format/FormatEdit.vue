<template>
  <v-container class="edit">
    <Edit :height="700"></Edit>
  </v-container>
</template>

<script>
import Edit from '../Edit'

export default {
  name: "FormatEdit",
  data() {
    return {}
  },
  components: {Edit}
}
</script>

<style scoped>
.edit {
  position: sticky;
  top: 8%;
}
</style>