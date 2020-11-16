<template>
  <v-textarea
      outlined
      v-model="markdownText"
      full-width
      color="#5bc8ac"
      placeholder="markdown記法で書いてください"
      background-color="#ffffff"
      :height="height"
      :rules="[required,blank]"
  >
    input
  </v-textarea>
</template>

<script>
import {mapState, mapActions} from 'vuex'

export default {
  name: "Edit",
  data() {
    return {
      required: value => !!value || "必ず入力してください",
      blank: value => {
        const pattern = /\S/g
        return pattern.test(value) || "空文字のみの入力はできません"
      },
    }
  },
  props: [
    "height"
  ],
  computed: {
    ...mapState("article", ["article"]),
    markdownText: {
      get() {
        return this.article.content
      },
      set(value) {
        this.commitMarkDownText(value)
      }
    },
  },
  methods: {
    ...mapActions("article", ["commitMarkDownText"])
  }
}
</script>

<style scoped>

</style>