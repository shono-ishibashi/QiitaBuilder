<template>
  <div id="editor">
    <v-textarea
        outlined
        v-model="markdownText"
        full-width
        rows="20"
    >
      input
    </v-textarea>
  </div>
</template>

<script>
import {mapState, mapActions} from 'vuex'

export default {
  name: "Edit",
  data() {
    return {}
  },
  computed: {
    ...mapState("article", ["article"]),
    markdownText: {
      get() {
        return this.article.content
      },
      set(value) {
        this.commitMarkDownText(value)
      }
    }
  },
  methods: {
    ...mapActions("article", ["commitMarkDownText"])
  }
}
</script>

<style scoped>

</style>