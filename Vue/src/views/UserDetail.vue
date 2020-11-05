<template>
  <div>
    UserDetail
  </div>
</template>

<script>
import {mapState, mapActions} from "vuex"

export default {
  name: "userDetail",
  components: {},
  data() {
    return {
    };
  },
  computed: {
    ...mapState("user", ["userDetail", "postedArticles", "feedbackArticles", "myArticles"])
  },
  methods: {
    ...mapActions("user", ["fetchUserDetail", "fetchPostedArticles", "fetchFeedbackArticles", "fetchMyArticles"])
  },
  created() {
    this.fetchUserDetail(this.$route.query.userId);
    this.fetchPostedArticles(this.$route.query.userId);
    this.fetchFeedbackArticles(this.$route.query.userId);
    this.fetchMyArticles(this.$route.query.userId);
  }
}
</script>