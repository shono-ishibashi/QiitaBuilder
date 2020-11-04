<template>
  <div>
    Article Detail
    <Article />
    <Feedback />
    <FeedbackEditor />
  </div>
</template>

<script>
import Article from '../components/Article'
import Feedback from '../components/Feedback'
import FeedbackEditor from '../components/FeedbackEditor'
export default {
  components: {
    Article,
    Feedback,
    FeedbackEditor
  }
}
</script>