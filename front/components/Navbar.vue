<template>
  <div class="navigation-bar">
    <el-row>
          <!-- 로고 -->
        <el-col :span="2">
          <div class="logo-area">
            <img alt="" src="../assets/images/logo_.png" width="90px" height="30px" style="cursor: pointer;"  @click="goMain()" />
          </div>
        </el-col>
        <!-- 보드 목록 셀렉트 박스 -->
        <el-col :span="8" offset=6>
          <!-- <div class="board-select">
            <el-select v-model="selectedBoard" class="select-style" placeholder="your boards">
              <el-option
                v-for="bd in boardList"
                :key="bd.code"
                :label="bd.name"
                :value="bd.code"
                @click="goBoard(bd.code)"/>
            </el-select>
          </div> -->
          <!-- <div class="search-box">
            <el-input placeholder="Search" prefix-icon="el-icon-search" v-model="searchWord"></el-input>
            <el-button size="medium" @click="search">검색</el-button>
          </div> -->
        </el-col>

        <el-col :span="2" :offset=4>
          <div class="profile-area">
            <h4 style="color: white; margin-right: 10px;" >{{ userId }}</h4>
            <el-button class="button-sm" @click="logout()"><span>logout</span></el-button>
          </div>
        </el-col>
    </el-row>
    <!-- <div v-if="result" style="background-color: white;">
      <el-table
        :data="result"
        style="width:100%">
        <el-table-column
          prop="result.cardTitle"
          label="card title"
          width="180">
        </el-table-column>
        <el-table-column
          prop="result.listName"
          label="list name"
          width="180">
        </el-table-column>
        <el-table-column
          prop="result.brdName"
          label="board name"
          width="180">
        </el-table-column>
      </el-table>
    </div> -->
  </div>
</template>

<script>
import axios from "axios";
import Vue from "vue";
Vue.prototype.$http = axios;
const baseURI = 'http://localhost:8080';

export default {
  name: 'NavigationBar',
  props: { userId: { type: String, default: '' }},

  data() {
    return {
      id: this.userId,
      searchWord: '',
      result: [],
      boardCd: '',
    }
  },

  methods: {
    /* 홈 버튼 클릭 시 */
    goMain() { // 보드 목록 페이지로 이동
      this.$router.push({ path: '/main' });
    },

    /* 보드 목록 클릭 시 */
    goBoard(boardCd) { // 선택된 보드로 이동
      this.boardCd = boardCd;
      this.$router.push({ path: '/board' });
    },

    /* 로그아웃 */
    logout() {
      let router = this.$router;
      alert('로그아웃');
      router.push('/');
    },

    /** 카드 검색 */
    search() {
      axios.get(`${baseURI}/c/search`,{
        params: { 
          userId: this.id,
          searchWord: this.searchWord
        }
      })
      .then(res=> {
        this.result = res.data;
        console.log('검색결과: ',this.result);

      })
      .catch(err =>{
        console.log(err);
      })
    }
  }
}
</script>

<style scoped>
.navigation-bar {
  background-color: #6591af;
  border-bottom: 1px solid #6591af;
  height: 60px;
  padding: 0 30px;
}

.logo-area {
  height: 60px;
  float: left;
  display: flex;
  align-items: center;
    justify-content: flex-start;
}
.board-select {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: flex-start;
}

.profile-area {
  float: right;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: flex-end;
}
.search-box {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: flex-end;
}

.select-style {
  padding: 0 15px 0 30px;
}

.button-sm{
  background-color: rgb(245, 245, 245);
}
.button-sm:hover{
  color: #6591af;
}
</style>
