<template>
  <div id="preview">
    <div class="preview" v-html="compiledMarkdown"></div>
  </div>
</template>

<script>
import {mapState} from 'vuex'
import marked from "marked";
import hljs from "highlight.js";

export default {
  name: "Preview",
  created(){
    marked.setOptions({
      langPrefix: "",
      highlight (code, lang) {
        return hljs.highlightAuto(code, [lang]).value;
      },
    });
  },
  computed: {
    ...mapState("article",["article"]),
    compiledMarkdown: function () {
      return marked(this.article.content);
    },
  },
}
</script>

<style src='highlight.js/styles/github-gist.css' scoped >
.preview{
  border:4px solid #000000;
}
</style>