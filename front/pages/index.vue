<template>
  <div
    v-loading.fullscreen.lock="loading"
  />
</template>

<script>
export default {
  name: 'Index',
  data () {
    return {
      loading: true
    }
  },
  mounted () {
    this.loginChk()
  },
  methods: {
    loginChk () {
      const id = ''

      if (id === 'shchae') {
        this.$router.push('/main')
      } else {
        this.$router.push('/login')
      }
    }
  }
}
</script>
