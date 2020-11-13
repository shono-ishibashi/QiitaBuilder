<template>
  <v-row>
    <v-col>
      <v-row class="editAndFeedbacks">
        <Edit :height="350" class="edit"></Edit>
        <v-container class="feedback-area">
          <v-row class="feedback-title">
            <v-col cols="1">
              <v-icon large dark>mdi-comment</v-icon>
            </v-col>
            <v-col cols="">
              <h3 class="white--text">フィードバック</h3>
            </v-col>
          </v-row>
          <Feedback
              v-for="feedback in article.feedbacks"
              :feedback="feedback"
              :key="feedback.feedbackId"
          ></Feedback>
          <v-container v-if="article.feedbacks.length===0" class="no-feedback-field">
            フィードバックはありません
          </v-container>
        </v-container>
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
import Feedback from '../../article_detail/Feedback'
import {mapState} from 'vuex'

export default {
  name: "FormatEditAndPreviewAndFeedback",
  components: {
    Edit, Preview, Feedback
  },
  computed: {
    ...mapState("article", ["article"])
  }
}
</script>

<style scoped>
.editAndFeedbacks {
  position: sticky;
  top: 8%;
}

.edit {
  margin-bottom: 0;
}

.feedback-area {
  background-color: #5bc8ac;
  border:2px solid #008b8b;
  border-radius:15px;
  padding: 20px;
  height: 330px;
  overflow: scroll;
}

.no-feedback-field {
  display: flex;
  justify-content: center;
  align-items: center;
  font-weight: bold;
  background-color: #ffffff;
  margin-top: 30px;
  height: 200px;
}
</style>