<template>
<div>
  <div class="file-list" v-if="attchList">
    <div class="file-list-wrapper" v-for="file in attchList" :key="file.attachSeq">
      <!-- <img v-if="file.attachLoc" :src="'http://localhost:8080'+(file.attachLoc).substr(2)" style="height: 80px; width: 112x;"/> -->
      <span style="font-size: 12px;">{{ file.orgName }}</span>
      <el-button type="text" size="small" @click="downloadAtt(file)">download</el-button>
      <el-popconfirm
            title="파일을 삭제하시겠습니까?"
            @confirm="deleteAtt(file.attachName)">
            <el-button slot="reference" type="text" size="small" style="color=#e66677;" icon="el-icon-delete"/>
      </el-popconfirm>
    </div>
  </div>
  <div class="file-upload">
    <hr />
    <form @submit.prevent="formSubmit" method="post">
      <input type="file" name="selectFile" ref="selectFile" multiple = "multiple" @change="previewFile" />
      <img v-if="previewImgUrl" :src="previewImgUrl" style="height: 80px; width: 112x;"/>
      <div>
        <hr />
        <button type="submit" :disabled="isUploading">Upload</button>
        <!-- response : {{ response }} -->
      </div>
    </form>
  </div>
</div>
</template>



<script>
import axios from "axios";
import Vue from "vue";
Vue.prototype.$http = axios;

const baseURI = 'http://localhost:8080';

  export default {
    name: "FileLoad",
    components: {},
    props: { cd: { type: String, default: '' }},

    data() {
      return {
        selectFile: null, // 파일 객체
        previewImgUrl: null, // 미리보기 이미지 URL
        isUploading: false, // 파일 업로드 체크
        response: null, // 파일 업로드후 응답값
        attchList: [], // 파일 목록 저장
        attach:{
          attachName: ''
        }
      }
    },
    mounted() {
      this.getAttch(this.cd);
    },

    watch: {
    },

    methods: {
      /** 파일 목록 조회 */
      async getAttch(cardCd) {
        const cd = cardCd
        await axios.get(`${baseURI}/attach/list`,{
          params: { cardCode: cd }
        })
        .then(res => {
          this.attchList = _.cloneDeep(res.data);
          console.log('GET file list: ',this.attchList);
        })
        .catch(err => {
          console.log(err);
        })
      },

      /** 파일 업로드 전 이미지 미리 보기 */
      previewFile() {
        // 선택된 파일이 있는지 확인
        if (0 < this.$refs.selectFile.files.length) {
          // 0 번째 파일을 가져 온다.
          this.selectFile = this.$refs.selectFile.files[0]
          // 마지막 . 위치를 찾고 + 1 하여 확장자 명을 가져온다.
          let fileExt = this.selectFile.name.substring(
            this.selectFile.name.lastIndexOf(".") + 1
          )
          // 소문자로 변환
          fileExt = fileExt.toLowerCase()
          // 이미지 확장자 체크, 1메가 바이트 이하 인지 체크
          if (
            ["jpeg", "png", "gif", "bmp"].includes(fileExt) &&
            this.selectFile.size <= 1048576
          ) {
            // FileReader 를 활용하여 파일을 읽기
            var reader = new FileReader()
            reader.onload = e => {
              // base64
              this.previewImgUrl = e.target.result
            }
            reader.readAsDataURL(this.selectFile)
          } else if (this.selectFile.size <= 1048576) {
            // 이미지 외 파일
            this.previewImgUrl = null
          } else {
            alert("파일을 다시 선택해 주세요.")
            this.selectFile = null
            this.previewImgUrl = null
          }
        } else {
          // 파일을 선택하지 않았을 때
          this.selectFile = null
          this.previewImgUrl = null
        }
        console.log(this.selectFile)
      },

      /** 파일 업로드 */
      async formSubmit() {
        if (this.selectFile) {
          // Form 필드 생성
          const form = new FormData()
          const files = this.$refs.selectFile.files
          _.forEach(files,(v,i) => {
            form.append("files", v)
          })
          form.append("cardCode", this.cd)

          //데이터 확인용
          let entries = form.entries();
          for (const pair of entries) {
              console.log(pair[0]+ ', ' + pair[1]); 
          }

          this.isUploading = true

          axios.post(`${baseURI}/attach/upload`, form, {
              headers: {
                "Content-Type": "multipart/form-data",
              },
            })
            .then(res => {
              this.response = res
              console.log('upload success');
              this.isUploading = false
              this.selectFile = null
              this.previewImgUrl = null
              this.getAttch(this.cd)
            })
            .catch(error => {
              this.response = error
              this.isUploading = false
            })
        } else {
          alert("파일을 선택해 주세요.")
        }

        return true
      },

      /** 파일 다운로드 */
      downloadAtt(file) {
        // const cd = file.cardCode;
        const an = file.attachName;
        const a = document.createElement('a');
        const downloadURL = '/attach/att/download?attachName='+an;

        a.href = `${baseURI}` + downloadURL;
        a.click();
      },

      /** 파일 삭제 */
      deleteAtt(file) {
        console.log('확인',file);
        this.attach.attachName = file;
        axios.patch(`${baseURI}/attach/delete`,this.attach)
        .then(res => {
          console.log('delete attachment');
          this.getAttch(this.cd);
        })
        .catch(err => {
          console.log(err);
        })
      }

    },
  }
</script>

<style scoped>
a :hover{
  color: #6591af;
  cursor: pointer;
}
.file-list-wrapper {
  height: auto;
  margin-bottom: 10px;
}
</style>