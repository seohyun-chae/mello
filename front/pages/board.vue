<template>
<div class="container">
  <div class="header">
      <Navbar/>
  </div>

    <div class="board-page">
      <!-- 콘텐츠 헤더 -->
      <div class="board-header">
        <h4 style="color: white; margin: 8px; margin-left: 10px;">{{ this.boardName }}</h4>
      </div>

      <div class="board-canvas">
        <div class="board-content">
          <draggable
            v-if="brdInfo"
            :list="brdInfo.list"
            ghost-class="ghost"
            group="groupB"
            @change="updateListSort()"
          >
            <!-- 리스트 -->
            <div class="list-wrapper" v-for="list in brdInfo.list" :key=list.listCode>
              <el-card class="list">
                <div class="list-title" v-if="inputBtnLst!==list.listCode">
                  <span><h4>{{ list.listName }}</h4></span>
                </div>
                <!-- 수정 -->
                <el-button v-if="inputBtnLst!==list.listCode" size="mini" icon="el-icon-edit" @click="listInputYn(list.listCode)"/>
                  <div class="list-edit" v-show="inputBtnLst===list.listCode">
                      <div style="text-align: right; margin: 0;">
                        <el-input v-model="upList.listName"></el-input><br/>
                        <el-button size="mini" type="text" @click="inputBtnLst = null">취소</el-button>
                        <el-button size="mini" type="primary" @click="edtLst(list)">수정</el-button>
                      </div>
                  </div>
                <!-- 삭제 -->
                <div style="margin-left: 3px;">
                  <el-popconfirm
                    title="리스트를 삭제하시겠습니까?"
                    @confirm="deleteLst(list)">
                    <el-button slot="reference" type="text" class="delete-list-btn" icon="el-icon-delete-solid"/>
                  </el-popconfirm>
                </div>
                <draggable
                  :list="list.card"
                  group="groupA"
                  ghost-class="ghost"
                  @change="updateCardSort(list)"
                >
                <!-- 카드 -->
                  <div
                    v-for="card in list.card"
                    :key="card.cardCode"
                  >
                    <div class="card-background" v-show="card.cardCode !== 'null'" v-on:mouseover="cdHover(card.cardCode)" v-on:mouseleave="editBtnHide">
                      <div class="list-card-title" @click="showPopup(card,list)" ><span class="card-text">{{ card.cardTitle }}</span></div>
                      <!-- 카드 수정 버튼 -->
                      <div class="card-btn" v-show="edtBtnCd===card.cardCode">
                        <el-button icon="el-icon-edit" @click="inputBtnCd=true" size="mini"/>
                        <!-- 삭제 -->
                        <el-popconfirm
                          title="카드를 삭제하시겠습니까?"
                          @confirm="deleteCard(card)">
                          <el-button slot="reference" type="text" style="color:#e66677; font-weight: 800;" icon="el-icon-delete"/>
                        </el-popconfirm>
                      </div>
                    </div>
                    <div class="card-background-none" v-show="card.cardCode == 'null'" >ㅤ</div>
                  </div>
                </draggable>
                <!-- 카드 추가 버튼 -->
                <el-button type="text" style="color:black;" @click="crdBtnClick(list.listCode)">+ add card</el-button>
                <div v-if="crdBtn === list.listCode">
                  <el-input
                    v-model="cardForm.cardTitle"
                    placeholder="카드 제목 입력"
                    size="small"
                    clearable/>
                  <div style="margin: 3px; padding-top: 5px;">
                    <el-button @click="addCard(list.listCode)" size="small">Add</el-button>
                    <el-button @click="btnCancel" size="small">Cancel</el-button>
                  </div>
                </div>
              </el-card>
            </div>
          </draggable>
          <!-- 리스트 추가 버튼 -->
          <div class="add-list-btn">
            <el-button type="text" v-show="(!lstBtn)" @click="lstBtnClick">+ add list</el-button>
            <div v-show="lstBtn">
              <el-input
                v-model="listForm.listName"
                placeholder="리스트 이름 입력"
                size="small"
                clearable/>
              <el-button @click="addList" size="small">Add</el-button>
              <el-button @click="btnCancel" size="small">Cancel</el-button>
            </div>
          </div>
        </div>
      </div>

      <!-- 카드 상세 팝업 -->
      <card-popup
      :is-active="popupVisible"
      :card-code="this.cardCode"
      :list-name="this.listName"
      @close="hidePopup"
      />
    </div>

</div>
</template>

<script>
import axios from "axios";
import Vue from "vue";
import draggable from 'vuedraggable';
import _ from 'lodash';
Vue.prototype.$http = axios;

import Navbar from '../components/Navbar.vue';
import CardPopup from '../pages/cardPopup.vue';

const baseURI = 'http://localhost:8080';

export default {
  name: 'board',
  layout: 'common',
  components: { draggable, Navbar, CardPopup },

  mounted() {
    this.boardCode = this.$route.params.brdCode;
    this.boardName = this.$route.params.brdName;
    this.getBoardInfo();
  },

  data() {
    return {
      boardCode: '',
      boardName: '',

      brdInfo: [], // 보드 데이터 저장
      
      // 리스트 추가시
      listForm: {
        listName: '',
        listIdx: null,
        brdCode: ''
      },
      //카드 추가시
      cardForm: {
        cardTitle: '',
        cardIdx: null,
        listCode: ''
      },
      // 리스트 순서, 이름 변경 parameter.
      upList: {
        // listCode: null,
        // listIdx: null,
        // listName: '',
        brdCode: '',
        list: {}
      },
      // 카드 순서, 이름 수정 parameter
      // 후에 보드 코드 추가
      upCard: {
        listCode: null,
        cardCode: null,
        cardIdx: null,
        cardTitle: ''
      },
      //카드 팝업 변수
      cardCode: null,
      listName: null,
      popupVisible: false, // 카드 상세 팝업

      openYn: true, // 보드 공개 여부 스위치
      // drawerShow: false, // 사이드 메뉴 토글
      lstBtn: false, // 리스트 추가 버튼 전환
      crdBtn: null, // 카드 추가 버튼 전환
      inputBtnCd: false,
      inputBtnLst: null,
      edtBtnCd: false, // 카드 수정 버튼 보기 전환
      edtBtnLst: false, // 리스트 삭제 버튼 보기 전환
      deleteOKcd: false, // 카드 삭제 확인 버튼
    };
  },

  watch: {

  },

  methods: {
    /** 데이터 초기화 */
    initForm() {
      console.log('init form');
      this.upCard.listCode = '';
      this.upCard.cardCode = '';
      this.upCard.cardIdx = null;
      this.upCard.cardTitle = '';
      this.listForm.listName = '';
      this.listForm.listIdx = null;
      this.listForm.brdCode = '';
      this.cardForm.cardTitle = '';
      this.cardForm.cardIdx = null;
      this.cardForm.listCode = '';
    },

    /** 리스트, 카드 데이터 조회 */
    async getBoardInfo() {
      await axios.get(`${baseURI}/b/boardR`, {  // `${baseURI}/b/${brdCode}`
        params: {
          brdCode: this.boardCode
        }
      })
      .then(res => {
        this.brdInfo = null;
        this.brdInfo = _.cloneDeep(res.data);
        console.log("GET Data : ", this.brdInfo);
      })
      .catch(err => {
        console.log(err);
      })
    },

    /** 리스트 추가 */
    addList() {
      if(_.isEmpty(this.brdInfo.list)){
        this.listForm.listIdx = 0;
      } else {
        this.listForm.listIdx = _.size(this.brdInfo.list);
      }
      this.listForm.brdCode = this.boardCode;
      console.log('listForm: ',this.listForm);

      axios.post(`${baseURI}/b/AddList`, this.listForm)
        .then(res => {
          console.log('list insert');
          this.initForm();
          this.getBoardInfo();
          this.btnCancel();
        })
        .catch(err => {
          console.log(err);
        })
    },

    /** 카드 추가 */
    addCard(lstCd) {
      let tmp;
      tmp = _.filter(this.brdInfo.list,{listCode:lstCd});
      console.log("tmp 확인",tmp);
      if(tmp[0].cardCode == null) {
        this.cardForm.cardIdx = 0;
      } else {
        this.cardForm.cardIdx = _.size(tmp);
      }
      this.cardForm.listCode = lstCd;
      console.log('cardForm: ',this.cardForm);

      axios.post(`${baseURI}/b/AddCard`, this.cardForm)
        .then(res => {
          console.log('card insert');
          this.initForm();
          this.getBoardInfo();
          this.btnCancel();
        })
        .catch(err => {
          console.log(err);
        })
    },

    /** 카드 정렬 변경 */
    async onAxiosCard() {
      await axios.patch(`${baseURI}/b/card`, this.upCard)
        .then(res => {
          console.log(res.data);
          console.log('updateCardSort 확인');
        })
        .catch(err => {
          console.log(err);
        })
    },
    updateCardSort(lst) {
      console.log(lst.listName,': 카드 정렬 변경');
      console.log('listCode: ', lst.listCode);

      for(let i = 0; i < _.size(lst.card); i++){
        // 카드의 인덱스와 리스트 코드를 업데이트 한다.
        this.initForm();
        this.upCard.listCode = lst.listCode;
        this.upCard.cardCode = lst.card[i].cardCode;
        this.upCard.cardIdx = i;
        console.log('현재 수정하는 카드: ',this.upCard.cardCode,i);
        // axios 호출
        this.onAxiosCard();
      }
    },

    /** 리스트 정렬 변경 */
    updateListSort() {
      console.log('리스트 정렬 변경');
      this.upList.brdCode = this.brdCode;
      this.upList.list = _.cloneDeep(this.brdInfo.list);
      console.log("parameter 확인", this.upList.list);

      axios.post(`${baseURI}/b/sortList`, this.upList)
      .then(res => {
        console.log('update complete');
        this.getBoardInfo();
        this.upList.list = null;
      })
      .catch(err => {
        console.log(err);
      })
    },

    /** 리스트 이름 변경  */
    edtLst(lst) {
      this.upList.listCode = lst.listCode;
      console.log(this.upList);

      axios.patch(`${baseURI}/b/listName`, this.upList)
      .then(res => {
        console.log('rename list');
        this.initForm();
        this.getBoardInfo();
        this.inputBtnLst = null;
        
      })
      .catch(err => {
        console.log(err);
      })
    },

    /** 카드 삭제 */
    async deleteCard(cd) {
      this. deleteOKcd = false;
      this.cardForm.cardCode = cd.cardCode;
      this.cardForm.cardTitle = cd.cardTitle;
      this.cardForm.listCode = cd.listCode;

      await axios.post(`${baseURI}/b/deleteCard`, this.cardForm)
        .then(res => {
          console.log(res.data);
          console.log('delete complete');
          this.initForm();
          this.getBoardInfo();
        })
        .catch(err => {
          console.log(err);
        })
    },

    /** 리스트 삭제 */
    async deleteLst(cd) {
      console.log(cd);
      this.listForm.listCode = cd.listCode;
      this.listForm.listName = cd.listName;
      this.listForm.brdCode = cd.brdCode;

      await axios.post(`${baseURI}/b/deleteList`,this.listForm)
        .then(res => {
          console.log(res.data);
          console.log('delete complete');
          this.initForm();
          this.getBoardInfo();
        })
        .catch(err => {
          console.log(err);
        })
    },

    lstBtnClick() {
      this.lstBtn = true;
    },
    deleteCK(lstCd) {
      this.edtBtnLst = lstCd;
    },
    crdBtnClick(lst) {
      this.crdBtn = lst;
    },
    btnCancel() {
      this.lstBtn = false;
      this.crdBtn = null;
    },
    listInputYn(lst) {
      this.inputBtnLst = lst;
    },
    cdHover(cd) {
      this.edtBtnCd = cd;
    },
    editBtnHide() {
      this.edtBtnCd = '';
      this.deleteOKcd = false;
    },
    /* 카드 클릭시 팝업창 띄우기 */
    showPopup(cd,list) {
      this.cardCode = cd.cardCode;
      this.listName = list.listName;
      this.popupVisible = true;
    },
    hidePopup() {
      this.popupVisible = false;
      this.getBoardInfo();
    }
  },
};
</script>

<style scoped>
  .button-sm {
    background-color:#6591af;
  }
  .button-sm:hover {
    background-color: rgb(245, 245, 245);
    color: #6591af;
  }
  .icon-area {
    font-size: 20px;
    position: absolute;
    right: 6px;
    top:20px;
  }
  [v-cloak] {
    display: none;
  }
  .el-switch__label.is-active {
    color: #ffffff !important;
    font-weight: 800 !important;
  }
</style>
