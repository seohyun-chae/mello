<template>
<div class="main-page">
  <div class="main-header">
    <navbar
    :user-id="sendData.userId"
    :brd-list="boards"/>
  </div>
  <div class="main-content">
    <h2 style="margin-left: 10px;">your boards here</h2>
    <!--보드 목록-->
    <div class="board-list-wrapper">
        <el-card class="board-form" v-for="board in boards" :key="board" shadow="hover">
          <el-popconfirm
            title="보드를 삭제하시겠습니까?"
            @confirm="deleteBoard(board)">
            <el-button slot="reference" type="text" style="color:#e66677; font-weight: 800;" icon="el-icon-delete"/>
          </el-popconfirm>
          <h4>{{ board.substring(board.indexOf(':')+1) }}</h4><br/>
          <el-button @click="goBoard(board)">go to board</el-button>
        </el-card>
    </div>
    <!--보드 추가-->
    <el-button style="margin-left: 15px;" v-show="!createBtn" @click="createBtn=true">보드추가</el-button>
    <div v-show="createBtn">
      <el-input
        v-model="sendData.brdName"
        placeholder="보드 이름 입력"
        size="small"
        style="width:400px;"/>
      <el-button @click="AddBoard" size="small">Add</el-button>
      <el-button @click="createBtn=false" size="small">Cancel</el-button>
    </div>
  </div>
</div>
</template>

<script>
import axios from "axios";
import Vue from "vue";
import _ from 'lodash';
Vue.prototype.$http = axios;

import Navbar from '../components/Navbar.vue';

const baseURI = 'http://localhost:8080';
export default {
  name: 'main',
  layout: 'common',
  components: { Navbar },
  data() {
    return {
      boards: [],
      createBtn: false,
      sendData:{
        brdName: '',
        brdCode: '',
        userId: sessionStorage.getItem('loginId')
      }
    };
  },
  mounted() {
    this.getBoard();
  },

  methods: {

    /** 보드 목록 조회 */
    getBoard() {
      axios.get(`${baseURI}/b/boardListR`,{
        params: {
          userId: sessionStorage.getItem('loginId')
        }
      })
      .then(res => {
        let arr = Array.from(res.data);
        this.boards = _.cloneDeep(arr);
        console.log("this.boards ",this.boards);
      })
      .catch(err => {
        console.log(err);
      })
    },

    /** 보드 추가 */
    AddBoard() {
      axios.post(`${baseURI}/b/boardR`, this.sendData)
      .then(res => {
        console.log('Add board');
        this.createBtn = false;
        this.sendData.boardName = '';
        this.getBoard();
      })
      .catch(err => {
        console.log(err);
      })

    },

    /** 보드 삭제 */
    deleteBoard(brd){
      this.sendData.brdCode = brd.substring(0,brd.indexOf(':'));
      this.sendData.brdName = brd.substring(brd.indexOf(':')+1);
      axios.post(`${baseURI}/b/deleteBoard`,this.sendData)
      .then(res => {
        console.log('delete complete');
        this.sendData.brdName = '';
        this.sendData.brdCode = '';
        this.getBoard();
      })
      .catch(err => {
        console.log(err);
      })

    },

    goBoard(brd) {
      this.$router.push({
        name:'board',
        params: {
          brdCode: brd.substring(0,brd.indexOf(':')),
          brdName: brd.substring(brd.indexOf(':')+1)
        }
      })
    },
  },
};
</script>

<style scoped>
  .main-content {
    margin: 0 0 0 20px;
    padding: 0 15 15 15px;
  }
  .board-form {
    margin: 15px;
    padding: 10px;
  }
</style>
