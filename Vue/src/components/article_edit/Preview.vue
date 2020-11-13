<template>
  <v-container fluid class="preview">
    <div v-html="compiledMarkdown"></div>
  </v-container>
</template>

<script>
import {mapState, mapGetters} from 'vuex'
import marked from "marked";
import hljs from "highlight.js";

export default {
  name: "Preview",
  created() {
    marked.setOptions({
      langPrefix: "",
      highlight(code, lang) {
        return hljs.highlightAuto(code, [lang]).value;
      },
    });
  },
  computed: {
    ...mapState("article", ["article"]),
    ...mapGetters("article", ["compiledMarkdown"]),
  },
}
</script>

<style src="highlight.js/styles/github-gist.css"></style>
<style scoped>
.preview {
  background-color: #ffffff;
  border: 4px solid #a9a9a9;
  height: 480px;
  overflow: scroll;
}
</style>