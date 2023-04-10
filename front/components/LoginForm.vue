<template>
  <div class="bg">
    <el-header>
    </el-header>

    <el-main>
      <el-card class="box-card">
        <el-row class="center">
          <el-row>
            <el-col>
              <span>MELLO</span>
            </el-col>
          </el-row>
          
          <el-row>
            <el-input label="userId" placeholder="아이디" v-model="sendForm.userId"></el-input>
          </el-row>
          <el-row>
            <el-input label="password" placeholder="비밀번호" v-model="sendForm.password" show-password v-on:keyup.enter.native="login"></el-input>
          </el-row>

          <el-row>   <!--로그인 버튼-->
            <el-button type="info" size="medium"  @click="login" class="button-bg">로그인</el-button>
          </el-row>      
        </el-row>
      </el-card>
    </el-main>

  </div>
</template>

<script>
import axios from "axios";
import Vue from "vue";
Vue.prototype.$http = axios;
const baseURI = 'http://localhost:8080';

  export default {
    name: "loginForm",
    layout: "common",
    data() {
      return {
        sendForm:{
          userId: '',
          password: ''
        },
        testForm:{
          key: 'watermelon',
          value: 'dark green'
        }
      };
    },
    methods: {
      async login() {
        await axios.post(`${baseURI}/loginR`, this.sendForm)
          .then(res => {
            console.log('login?');
            if(res.data.loginY){
              sessionStorage.setItem('loginId',res.data.loginId);
              this.$router.push('/main')
            }else {
              alert(res.data.loginId);
            }
          })
          .catch(err => {
            console.log(err);
          })
      },
    }
  }
</script>

<style scoped>
  .bg {
  background-image: url("./assets/images/bgImg2_sm.png");
  background-position: center;
  width: 100%;
  height: 900px;
  }
  .box-card{
    width: 500px;
    margin: 0 auto;
  }
  .form{
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    color: #6591af;
    background-color:#6591af;
    }
  .center{
    justify-content: center;
    align-items: center;
    background-color:white; 
    padding: 10px 60px 10px 60px; 
    text-align:center;
  }
  .el-row{
    margin: 10px;
  }
  .button-bg{
    width: 300px; 
    background-color:#6591af;
  }
</style>
