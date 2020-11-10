<template>
    <v-row>
      <v-col>
        <Edit></Edit>
      </v-col>
      <v-col>
        <Preview></Preview>
      </v-col>
    </v-row>
</template>

<script>
import Edit from '../Edit'
import Preview from '../Preview'

export default {
  name: "EditAndPreview",
  components:{
    Edit,
    Preview
  }
}
</script>

<style scoped>

</style>

<!--<template>-->
<!--  <form id="post-form">-->
<!--    <div id="editor">-->
<!--      <textarea v-model="markdownText">input</textarea>-->
<!--    </div>-->
<!--    <div id="preview">-->
<!--      <div v-html="compiledMarkdown"></div>-->
<!--    </div>-->

<!--    <div>生データ</div>-->
<!--    {{ markdownText }}-->
<!--  </form>-->
<!--</template>-->
<!--<script>-->
<!--import marked from "marked";-->
<!--import hljs from "highlight.js";-->
<!--export default {-->
<!--  name: "postForm",-->
<!--  created: function () {-->
<!--    marked.setOptions({-->
<!--      langPrefix: "",-->
<!--      highlight: function (code, lang) {-->
<!--        return hljs.highlightAuto(code, [lang]).value;-->
<!--      },-->
<!--    });-->
<!--  },-->
<!--  computed: {-->
<!--    compiledMarkdown: function () {-->
<!--      return marked(this.markdownText);-->
<!--    },-->
<!--  },-->
<!--  data: function () {-->
<!--    return {-->
<!--      markdownText:-->
<!--          '## Java で hello world を出力させる方法\n```java\npublic static void main(String[] args){\n    System.out.println("hello world");\n}\n```',-->
<!--    };-->
<!--  },-->
<!--};-->
<!--</script>-->
<!--<style src='highlight.js/styles/github-gist.css'></style>-->
