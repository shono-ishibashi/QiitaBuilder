<template>
  <v-container fluid class="preview">
    <Editor
      mode="viewer"
      ref="editor"
      hint="Hint"
      :outline="true"
      :render-config="renderConfig"
      v-model="content"
    />
  </v-container>
</template>

<script>
import { Editor } from "vuetify-markdown-editor";

export default {
  components: {
    Editor,
  },
  name: "Preview",
  data() {
    return {
      renderConfig: {
        // Mermaid config
        mermaid: {
          theme: "dark"
        }
      }
    };
  },
  computed: {
    content(){
      return this.$store.state.article.article.content;
    }
  },
};
</script>

<style scoped>
.preview {
  background-color: #ffffff;
  border: 4px solid #a9a9a9;
  min-height: 700px;
  height:auto !important;
}
</style>
