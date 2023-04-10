<template>
    <div class="modal" :class="{ 'is-active': isActive } ">
      <div class="modal-background" @click="close()"/>
        <template v-if="this.show">
          <div class="modal-content">
            <div class="card-banner">
              <i class="el-icon-close close-icon-area" @click="close()"/>
            </div>

            <div class="card-header">
                <i class="el-icon-receiving icon-area"/>
                <div class="card-title">
                  <h3 v-show="!tActive" @click="tActive=true">{{cardForm.cardTitle}}</h3>
                  <div v-show="tActive">
                    <el-input v-model="cardForm.cardTitle"/>
                    <el-button-group>
                      <el-button type="default" size="mini" @click="tActive = false">취소</el-button>
                      <el-button type="primary" size="mini" @click="modifyCard">수정</el-button>
                    </el-button-group>
                  </div>
                </div>
                <div class="card-title-inline-content">
                  <p class="inline-block">::: {{listName}}</p>
                </div>
            </div>

            <div class="card-main">
              <div class="card-desc card-main-module">
                <h4 style="margin: 3px;">Description</h4>
                <i class="el-icon-edit" @click="inputActive=true">edit</i>
                <!--editor area-->
                <div class="edit-description">
                  <div v-show="inputActive==false"
                  @click="inputActive = true"
                  v-html="cardForm.cardInfo"
                  style="white-space: pre-line"/>
                  <div v-show="inputActive==true">
                    <el-input
                      type="textarea"
                      rows="4"
                      placeholder="Enter the description"
                      v-model="cardForm.cardInfo"/>
                      <el-button-group>
                        <el-button type="default" @click="inputActive = false">취소</el-button>
                        <el-button type="primary" @click="modifyCard">수정</el-button>
                      </el-button-group>
                    </div>
                </div>
              </div>
              <hr />
              <div class="attachment card-main-module">
                <h4>Attachment</h4>
                <div class="file-attachment-area">
                  <file-load ref="fileLoading" :cd="this.cardCode"/>
                </div>
              </div>
            </div>
          </div>
        </template>
    </div>
</template>

<script>
import axios from "axios";
import Vue from "vue";
import _ from 'lodash';

import FileLoad from '../components/FileLoad.vue'

Vue.prototype.$http = axios;
const baseURI = 'http://localhost:8080';

export default {
  name: 'cardPopup',
  components: { FileLoad },
  props: {
    isActive: { type: Boolean, default: false },
    cardCode: { type: String, default: ''},
    listName: { type: String, default: ''},
    },
  data() {
    return {
      cardForm: {
        cardCode: null,
        cardTitle: null,
        cardInfo: '',
        imgCover: null,
      },
      getData: {},
      show: false,
      inputActive: false,
      tActive: false
    }
  },

  watch: {
    'isActive' () {
      console.log('isActive');
      this.getCardInfo(this.cardCode);
    },
    'listName' (e) {
      this.listName = e;
    },
    'cardCode' (e) {
      this.cardForm.cardCode = e;
    }
  },

  methods: {
    /** 카드 정보 가져오기 */
    async getCardInfo(cardCode) {
      await axios.get(`${baseURI}/c/${cardCode}`)
      .then(res => {
        this.getData = _.cloneDeep(res.data);
        console.log("GET Data : ", this.getData);
        this.cardForm.cardTitle = this.getData[0].cardTitle;
        this.cardForm.cardInfo = this.getData[0].cardInfo;
        this.show = true;
        this.$refs.fileLoading.getAttch(cardCode); // 업로드 된 파일 목록 호출
      })
      .catch(err => {
        console.log(err);
      })
    },

    /** 카드 제목, 설명 수정 */
    modifyCard() {
      axios.patch(`${baseURI}/c/modify`, this.cardForm)
      .then(res => {
        console.log(res.data);
        console.log('update Card');
        this.getCardInfo(this.cardCode);
        this.inputActive = false;
        this.tActive = false;
      })
      .catch(err => {
        console.log(err);
      })
    },

    close() {
      this.inputActive = false;
      this.$emit('close');
    },
  }
  
}
</script>

<style scoped>
  a:link {
    color: #6591af;
  }
  a:active {
    color:#e66677;
  }
  a:visited {
    color:#98a79b;
  }
  a:hover {
    color:#e66677;
  }
  .el-icon-edit {
    padding-bottom: 5px;
    margin-bottom: 10px;
    color:#6591af;
  }
  .el-icon-edit:hover {
    color: #e66677;
    cursor: pointer;
  }
  .button-small {
    background-color:#ffffff;
    border-color: #6591af;
    color: #6591af;
  }
  .button-small :hover {
    color: #e66677;
    outline-color: #e66677;
  }
  .button-medium {
    background-color:#6591af;
    color: white;
  }
  .icon-area {
  left: 30px;
  top: 35px;
}
</style>
