<template>
  <v-container>
    <Preview class="preview"></Preview>
  </v-container>
</template>

<script>
import Preview from '../Preview'

export default {
  name: "FormatPreview",
  components: {Preview}
}
</script>

<style scoped>
.preview {
  position: sticky;
  top: 8%;
}
</style>