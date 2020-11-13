<template>
    <v-row>
      <v-col cols="6">
        <Edit class="edit" :height="700"></Edit>
      </v-col>
      <v-col cols="6" class="preview">
        <Preview></Preview>
      </v-col>
    </v-row>
</template>

<script>
import Edit from '../Edit'
import Preview from '../Preview'

export default {
  name: "FormatEditAndPreview",
  data(){
    return{
    }
  },
  components:{
    Edit,
    Preview
  }
}
</script>

<style scoped>
.edit{
  position:sticky;
  top:8%;
}
</style>