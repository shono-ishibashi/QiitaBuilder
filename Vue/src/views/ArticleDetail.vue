<template>
  <div>
    Article Detail
    <article/>
    <feedback/>
    <feedbackEditor/>
  </div>
</template>

<script>
import article from '../components/Article'
import feedback from '../components/Feedback'
import feedbackEditor from '../components/FeedbackEditor'
export default {
  components:{
    article,
    feedback,
    feedbackEditor
  }
}
</script>