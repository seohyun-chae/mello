<template>
    <div>
      
      <LoginForm />
      
    </div>
  </template>
  <script>
  import LoginForm from "/components/LoginForm.vue";
  export default {
    name: "Login",
    head(){
    return{
      
      }
    },
    components: {
        LoginForm,
    },
  };
  </script>
  <style>
  </style>