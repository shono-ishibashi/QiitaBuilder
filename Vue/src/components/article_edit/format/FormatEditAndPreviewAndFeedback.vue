<template>
    <v-row>
      <v-col>
        <v-row class="editAndFeedbacks">
          <Edit :height="425"></Edit>
          <Feedbacks :feedback="article.feedbacks"></Feedbacks>
        </v-row>
      </v-col>
      <v-col>
        <Preview></Preview>
      </v-col>
    </v-row>
</template>

<script>
import Edit from '../Edit'
import Preview from '../Preview'
import Feedbacks from '../../article_detail/Feedbacks'
import {mapState} from 'vuex'

export default {
  name: "FormatEditAndPreviewAndFeedback",
  components: {
    Edit, Preview, Feedbacks
  },
  computed: {
    ...mapState("article", ["article"])
  }
}
</script>

<style scoped>
.editAndFeedbacks{
  position:sticky;
  top:8%;
}
</style>